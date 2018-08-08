/*
 * Copyright 2018 Albert Tregnaghi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 */
package de.jcup.eclipse.commons.tasktags;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.texteditor.MarkerUtilities;

/**
 * A full standalone todo task solution which can be copied into a plugin and
 * works... Only markertype and implementation for logging necessary on
 * consumser side. <br>
 * <br>
 * This class is part of de.jcup.eclipse.commons which are a "copy-waste"
 * friendly approach. Any changes shall be done inside
 * https://github.com/de-jcup/eclipse-commons and then copied to target plugins
 * again...
 * 
 * @author Albert Tregnaghi
 * @version 2.1.0 - 2018-07-01
 *
 */
public class TaskTagsSupport implements IResourceChangeListener {

	private TaskTagSupportProvider provider;

	/**
	 * Creates task support and assert provider provider and their content is
	 * not <code>null</code>
	 * 
	 * @param provider
	 */
	public TaskTagsSupport(TaskTagSupportProvider provider) {
		if (provider == null) {
			throw new IllegalArgumentException("provider may not be null");
		}
		if (provider.getTodoTaskMarkerId() == null) {
			throw new IllegalArgumentException("provider.getMarkerType() may not result in null");
		}
		if (provider.getTodoTaskMarkerId().isEmpty()) {
			throw new IllegalArgumentException("provider.getMarkerType() may not be empty!");
		}
		if (provider.getTaskTagDefinitions() == null) {
			throw new IllegalArgumentException("provider.getTodoTaskDefinitions() may not result in null");
		}
		this.provider = provider;
	}

	/**
	 * Installs the support. Should be done in start method of Plugin Activator
	 * class
	 */
	public void install() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		if (workspace == null) {
			return;
		}
		/* with resource change listener we get all changes on file */
		workspace.addResourceChangeListener(this);
		/* scan inside workspace for resources on startup */
		noResourceChangedScan(true);

	}

	/**
	 * Will remove all former existing markers, inspect full workspace for Todos
	 * and does rebuild them.
	 */
	public void fullRebuild() {
		noResourceChangedScan(false);
	}

	void noResourceChangedScan(boolean ignoreFilesWithExistingMarkers) {
		TaskTagContext initialContext = new TaskTagContext();
		initialContext.ignoreFilesWithExistingMarkers = ignoreFilesWithExistingMarkers;

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		if (workspace == null) {
			return;
		}
		try {
			noResourceChangedScan(initialContext, workspace.getRoot());
			triggerTodoTaskJobIfNecessary(initialContext);
		} catch (CoreException e) {
			provider.logError("was not able to process todos initial", e);
		}
	}

	void noResourceChangedScan(TaskTagContext context, IContainer container) throws CoreException {
		IResource[] members = container.members();
		for (IResource member : members) {
			if (member instanceof IContainer) {
				noResourceChangedScan(context, (IContainer) member);
			} else if (member instanceof IFile) {
				IFile file = (IFile) member;
				if (!isFileExtensionHandled(file)) {
					continue;
				}
				if (!provider.isTodoTaskSupportEnabled()) {
					/* just do clean up */
					context.resourcesToClean.add(file);
					continue;
				}
				if (context.ignoreFilesWithExistingMarkers) {
					IMarker[] markers = file.findMarkers(provider.getTodoTaskMarkerId(), false, IResource.DEPTH_ZERO);
					if (markers != null && markers.length > 0) {
						/*
						 * already an marker available - ignore. changes will be
						 * automatically handled by resource change mechanism of
						 * eclipse and this listener
						 */
						continue;
					}

				}
				/*
				 * when no markers found this means no resource changes handled,
				 * means was (maybe) never inspected. So do inspect it
				 */
				visitResource(context, file);
			}

		}
	}

	/**
	 * Uninstalls the support. Should be done in end method of Plugin Activator
	 * class
	 */
	public void uninstall() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		if (workspace == null) {
			return;
		}
		workspace.removeResourceChangeListener(this);

	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if (!provider.isTodoTaskSupportEnabled()) {
			return;
		}
		int type = event.getType();
		if (type != IResourceChangeEvent.POST_CHANGE) {
			return;
		}
		TaskTagContext context = new TaskTagContext();
		IResource resource = event.getResource();

		handleResource(context, resource);
		handleDelta(context, event.getDelta());

		triggerTodoTaskJobIfNecessary(context);
	}

	private void triggerTodoTaskJobIfNecessary(TaskTagContext context) {
		try {
			createTodoMarkers(context);
		} catch (CoreException e) {
			provider.logError("Was not able to create todo markers", e);
		}
	}

	protected void visitLines(TaskTagContext context, String[] lines, IFile file) throws CoreException {
		context.resourcesToClean.add(file);

		int lineNumber = 0;
		for (String line : lines) {
			lineNumber++;
			if (line == null || line.length() == 0 || !provider.isLineCheckforTodoTaskNessary(line, lineNumber, lines)) {
				continue;
			}
			for (TaskTagDefinition task : provider.getTaskTagDefinitions()) {
				addCreateMarkerAction(context, task, line, lineNumber, file);
			}
		}
	}

	protected void addCreateMarkerAction(TaskTagContext context, TaskTagDefinition definition, String line,
			int lineNumber, IResource editorResource) throws CoreException {
		if (context == null) {
			throw new IllegalArgumentException("context may not be null!");
		}
		if (definition == null) {
			return;
		}
		String taskIdentifier = definition.getIdentifier();
		if (taskIdentifier == null) {
			return;
		}
		int taskIdentifierLength = taskIdentifier.length();
		if (taskIdentifierLength == 0) {
			return;
		}
		int todoIndex = line.indexOf(taskIdentifier);
		if (todoIndex == -1) {
			return;
		}
		int end = todoIndex + taskIdentifierLength;
		String message = line.substring(end);
		if (message.length() == 0) {
			/* no content */
			return;
		}
		if (Character.isLetterOrDigit(message.charAt(0))) {
			/* TODOx is no todo... */
			return;
		}
		CreateMarkerAction action = new CreateMarkerAction();

		action.priority = definition.getPriority().getMarkerPriority();
		action.resource = editorResource;
		action.message = taskIdentifier + " " + message;
		action.lineNumber = lineNumber;

		context.actions.add(action);

	}

	private void createTodoMarkers(TaskTagContext context) throws CoreException {
		List<CreateMarkerAction> actions = context.actions;
		List<IResource> resourcesToClean = context.resourcesToClean;
		int totalTasks = actions.size() + resourcesToClean.size();
		if (totalTasks == 0) {
			return;
		}
		Job job = new Job("Update todos of type" + provider.getTodoTaskMarkerId()) {

			@Override
			protected IStatus run(IProgressMonitor monitor) {

				int totalTasks = actions.size() + resourcesToClean.size();

				int worked = 0;
				monitor.beginTask("Updating todo tasks", totalTasks);
				for (IResource resourceToClean : resourcesToClean) {
					removeMarkers(resourceToClean);
					monitor.worked(worked++);
				}
				for (CreateMarkerAction action : actions) {
					try {
						createTaskMarker(action.resource, action.message, action.lineNumber, action.priority);
					} catch (CoreException e) {
						return new Status(IStatus.ERROR, TaskTagsSupport.this.provider.getTodoTaskPluginId(),
								"Failed to create task markers", e);
					}
					monitor.worked(worked++);
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}

	private void createTaskMarker(IResource resource, String message, int lineNumber, int priority)
			throws CoreException {
		if (lineNumber <= 0)
			lineNumber = 1;
		HashMap<String, Object> map = new HashMap<>();
		map.put(IMarker.PRIORITY, new Integer(priority));
		map.put(IMarker.LOCATION, resource.getFullPath().toOSString());
		map.put(IMarker.MESSAGE, message);
		MarkerUtilities.setLineNumber(map, lineNumber);
		MarkerUtilities.setMessage(map, message);

		IMarker newMarker = resource.createMarker(provider.getTodoTaskMarkerId());
		newMarker.setAttributes(map);

	}

	private IMarker[] removeMarkers(IResource resource) {
		if (resource == null) {
			/* maybe sync problem - guard close */
			return new IMarker[] {};
		}
		IMarker[] tasks = null;
		if (resource != null) {
			try {
				tasks = resource.findMarkers(provider.getTodoTaskMarkerId(), true, IResource.DEPTH_ZERO);
				for (int i = 0; i < tasks.length; i++) {
					tasks[i].delete();
				}

			} catch (CoreException e) {
				provider.logError("Was not able to delete markers", e);
			}
		}
		if (tasks == null) {
			tasks = new IMarker[] {};
		}
		return tasks;
	}

	protected void visitResource(TaskTagContext context, IFile file) throws CoreException {
		if (file == null) {
			return;
		}
		if (!file.exists()) {
			return;
		}
		if (file.isDerived()) {
			return;
		}
		if (!file.isSynchronized(IResource.DEPTH_ZERO)) {
			return;
		}
		try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getContents(), "UTF-8"))) {
			String line = null;
			List<String> list = new ArrayList<>();
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
			String[] lines = list.toArray(new String[list.size()]);
			visitLines(context, lines, file);
		} catch (RuntimeException | IOException e) {
			throw new CoreException(
					new Status(Status.ERROR, provider.getTodoTaskPluginId(), "Not able to visit resource", e));
		}

	}

	private void handleResource(TaskTagContext context, IResource resource) {
		if (!(resource instanceof IFile)) {
			return;
		}
		IFile file = (IFile) resource;
		boolean isFileExtensionHandled = isFileExtensionHandled(file);

		if (!isFileExtensionHandled) {
			return;
		}
		try {
			visitResource(context, file);
		} catch (CoreException e) {
			provider.logError("Cannot visit resource:" + file, e);
		}
	}

	protected boolean isFileExtensionHandled(IFile file) {
		String fileExtension = file.getFileExtension();
		List<String> fileExtensions = provider.getTodoTaskFileExtensions();
		for (String supportedFileExtension : fileExtensions) {
			boolean isFileExtensionHandled = supportedFileExtension.equals(fileExtension);
			if (isFileExtensionHandled) {
				return true;
			}
		}
		return false;
	}

	private void handleDelta(TaskTagContext context, IResourceDelta delta) {
		if (delta == null) {
			return;
		}
		int flags = delta.getFlags();
		if (flags == IResourceDelta.MARKERS) {
			return;
		}
		IResource resource = delta.getResource();
		if (resource instanceof IFile) {
			handleResource(context, resource);
			return;
		}
		for (IResourceDelta childDelta : delta.getAffectedChildren()) {
			handleDelta(context, childDelta);
		}
	}

	public interface TaskTagSupportProvider {

		public void logError(String error, Throwable t);

		/**
		 * @return todo task definitions. Implementations decide if this is
		 *         configurable by users or a fixed list...
		 */
		List<TaskTagDefinition> getTaskTagDefinitions();

		/**
		 * Method to turn off the complete support
		 * 
		 * @return <code>true</code> when todo tasks are enabled, otherwise
		 *         <code>false</code>
		 */
		boolean isTodoTaskSupportEnabled();

		boolean isLineCheckforTodoTaskNessary(String line, int lineNumber, String[] lines);

		/**
		 * @return the file extensions (without a dot! e.g. "yaml" to handle all
		 *         "*.yaml" files) which are inspected for _TODO_ analyzing
		 */
		List<String> getTodoTaskFileExtensions();

		/**
		 * @return marker ID which is used for TODOs
		 */
		String getTodoTaskMarkerId();

		/**
		 * 
		 * @return plugin id for the plugin where {@link TaskTagsSupport} is
		 *         used for
		 */
		String getTodoTaskPluginId();
	}

	public static enum TaskTagPriority {
		HIGH(IMarker.PRIORITY_HIGH), NORMAL(IMarker.PRIORITY_NORMAL), LOW(IMarker.PRIORITY_LOW);

		public int getMarkerPriority() {
			return markerPriority;
		}

		private int markerPriority;

		private TaskTagPriority(int priority) {
			this.markerPriority = priority;
		}

		public String labelText() {
			return name();
		}
	}

	public static class TaskTagDefinition {
		private TaskTagPriority priority;
		private String identifier;

		public TaskTagDefinition() {
			this(null,null);
		}
		
		public TaskTagDefinition(String identifier, TaskTagPriority priority) {
			if (identifier==null){
				identifier="unknown";
			}
			if (priority==null){
				priority = TaskTagPriority.LOW;
			}
			this.identifier = identifier;
			this.priority = priority;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
			result = prime * result + ((priority == null) ? 0 : priority.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TaskTagDefinition other = (TaskTagDefinition) obj;
			if (identifier == null) {
				if (other.identifier != null)
					return false;
			} else if (!identifier.equals(other.identifier))
				return false;
			if (priority != other.priority)
				return false;
			return true;
		}

		public TaskTagPriority getPriority() {
			if (priority == null) {
				priority = TaskTagPriority.NORMAL;
			}
			return priority;
		}

		public void setPriority(TaskTagPriority priority) {
			this.priority = priority;
		}

		public void setPriority(String priority) {
			try{
				this.priority=TaskTagPriority.valueOf(priority);
			}catch(RuntimeException e){
				this.priority=TaskTagPriority.LOW;
			}
		}
		
		public String getIdentifier() {
			return identifier;
		}

		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}

		public boolean isHighPriority() {
			return TaskTagPriority.HIGH.equals(priority);
		}

		public boolean isNormalPriority() {
			return TaskTagPriority.NORMAL.equals(priority);
		}

		public boolean isLowPriority() {
			return TaskTagPriority.LOW.equals(priority);
		}

	}

	private class TaskTagContext {
		List<IResource> resourcesToClean = new ArrayList<>();
		List<CreateMarkerAction> actions = new ArrayList<>();
		boolean ignoreFilesWithExistingMarkers;
	}

	private static class CreateMarkerAction {

		int priority;
		IResource resource;
		String message;
		int lineNumber;
	}

}