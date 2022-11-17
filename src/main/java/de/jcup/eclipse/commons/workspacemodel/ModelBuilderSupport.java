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
package de.jcup.eclipse.commons.workspacemodel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
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

import de.jcup.eclipse.commons.EclipseResourceHelper;
import de.jcup.eclipse.commons.ui.EclipseUtil;

/**
 * A full standalone workspace model builder solution which can be copied into a
 * plugin and works...<br>
 * . Just create the an instance and call install on it.
 * 
 * @author Albert Tregnaghi
 * @version 1.0
 *
 */
public class ModelBuilderSupport<M> implements IResourceChangeListener {

    private ModelBuilderSupportProvider<M> provider;
    private M model;
    private ModelBuilder<M> builder;

    private ReentrantLock oneTimeBuildOnlyLock = new ReentrantLock();

    /**
     * Create support instance for given provider
     * 
     * @param provider may not be <code>null</code>
     */
    public ModelBuilderSupport(ModelBuilderSupportProvider<M> provider) {
        if (provider == null) {
            throw new IllegalArgumentException("provider may not be null");
        }
        this.provider = provider;

        builder = provider.createBuilder();
        if (builder == null) {
            throw new IllegalStateException("Provider returned builder being null!");
        }
        this.model = builder.create();
        if (model == null) {
            throw new IllegalStateException("Builder did create no model but returned null!");
        }
    }

    public M getModel() {
        return model;
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
        /* scan inside workspace for resources on startup */
        noResourceChangedScan(true);
        /* with resource change listener we get all changes on file */
        workspace.addResourceChangeListener(this);

    }

    /**
     * Will remove all former existing markers, inspect full workspace and does
     * rebuild.
     */
    public void fullRebuild() {
        noResourceChangedScan(false);
    }

    private boolean isBuildEnabled() {
        return provider.isModelBuilderSupportEnabled();
    }

    private void noResourceChangedScan(boolean ignoreFilesWithExistingMarkers) {
        if (!isBuildEnabled()) {
            return;
        }
        ProjectModelBuilderContext initialContext = new ProjectModelBuilderContext();

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        if (workspace == null) {
            return;
        }

        Job job = new Job("Initialization of " + provider.getModelName()) {

            @Override
            protected IStatus run(IProgressMonitor monitor) {
                try {
                    int worked = 0;
                    monitor.beginTask("Fetch and build existing resources", 2);
                    doNoResourceChangedScan(initialContext, workspace.getRoot());
                    monitor.worked(worked++);
                    triggerModelBuildIfNecessary(initialContext);
                    monitor.worked(worked++);
                } catch (CoreException e) {
                    return new Status(Status.ERROR, provider.getPluginContextProvider().getPluginID(), "Was not able to fetch and build existing resources for " + provider.getModelName(), e);
                }

                return Status.OK_STATUS;
            }
        };
        job.schedule();

    }

    void doNoResourceChangedScan(ProjectModelBuilderContext context, IContainer container) throws CoreException {
        if (!container.isAccessible()) {
            return;
        }
        IResource[] members = container.members();
        for (IResource member : members) {
            if (member instanceof IContainer) {
                doNoResourceChangedScan(context, (IContainer) member);
            } else if (member instanceof IFile) {
                IFile file = (IFile) member;
                if (!provider.isFileHandled(file)) {
                    continue;
                }
                if (!provider.isModelBuilderSupportEnabled()) {
                    /* just do clean up */
                    context.resourcesToClean.add(file);
                    continue;
                }
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
        if (!isBuildEnabled()) {
            return;
        }
        int type = event.getType();
        if (type != IResourceChangeEvent.POST_CHANGE) {
            return;
        }
        ProjectModelBuilderContext context = new ProjectModelBuilderContext();
        IResource resource = event.getResource();
        handleResource(context, resource);
        handleDelta(context, event.getDelta());

        triggerModelBuildIfNecessary(context);
    }

    private void triggerModelBuildIfNecessary(ProjectModelBuilderContext context) {
        try {
            rebuildModel(context);
        } catch (CoreException e) {
            logError("Was not able to rebuild model", e);
        }
    }

    protected void visitLines(ProjectModelBuilderContext context, String[] lines, IFile file) throws CoreException {
        context.resourcesToClean.add(file);

        int lineNumber = 0;
        for (String line : lines) {
            lineNumber++;
            if (line == null || line.length() == 0 || !provider.isLineCheckforModelNessary(line, lineNumber, lines)) {
                continue;
            }
            addModelEntry(context, line, lineNumber, file);
        }
    }

    private void addModelEntry(ProjectModelBuilderContext context, String line, int lineNumber, IFile file) {
        CreateModelEntryAction action = new CreateModelEntryAction(file);
        action.lineNumber = lineNumber;
        action.line = line;

        context.actions.add(action);
    }

    private void rebuildModel(ProjectModelBuilderContext context) throws CoreException {
        if (!isBuildEnabled()) {
            return;
        }
        List<CreateModelEntryAction> actions = context.actions;
        List<IResource> resourcesToClean = context.resourcesToClean;
        int totalTasks = actions.size() + resourcesToClean.size();
        if (totalTasks == 0) {
            return;
        }

        Job job = new Job("Rebuild model of type" + provider.getModelName()) {

            @Override
            protected IStatus run(IProgressMonitor monitor) {
                oneTimeBuildOnlyLock.lock();
                try {
                    int totalTasks = actions.size() + resourcesToClean.size();

                    int worked = 0;
                    monitor.beginTask("Updating model", totalTasks);
                    for (IResource resourceToClean : resourcesToClean) {
                        try {
                            removeFromModel(resourceToClean);
                        } catch (CoreException e) {
                            return new Status(IStatus.ERROR, ModelBuilderSupport.this.provider.getPluginContextProvider().getPluginID(), "Failed to remove from model:" + resourceToClean, e);
                        }
                        monitor.worked(worked++);
                    }
                    for (CreateModelEntryAction action : actions) {
                        try {
                            addToModel(action);
                        } catch (CoreException e) {
                            return new Status(IStatus.ERROR, ModelBuilderSupport.this.provider.getPluginContextProvider().getPluginID(), "Failed to add to model:" + action.getResource(), e);
                        }
                        monitor.worked(worked++);
                    }
                } finally {
                    oneTimeBuildOnlyLock.unlock();
                }

                return Status.OK_STATUS;
            }
        };
        job.schedule();
    }

    protected void addToModel(CreateModelEntryAction action) throws CoreException {
        builder.update(model, action);
    }

    protected void removeFromModel(IResource resourceToClean) throws CoreException {
        builder.update(model, new RemoveModelEntryAction(resourceToClean));
    }

    protected void visitResource(ProjectModelBuilderContext context, IFile file) throws CoreException {
        if (file == null) {
            return;
        }
        if (!file.exists()) {
            context.resourcesToClean.add(file);
            return;
        }
        if (file.isDerived()) {
            return;
        }
        if (!file.isSynchronized(IResource.DEPTH_ZERO)) {
            return;
        }

        int amountOfLinesToCheck = provider.getAmountOfLinesToCheck();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getContents(), "UTF-8"))) {
            String line = null;
            int linesRead = 0;
            List<String> list = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                linesRead++;
                list.add(line);
                if (linesRead == amountOfLinesToCheck) {
                    /*
                     * -1 is all and so never reached. 1 is reached on first read... B and so on..
                     */
                    break;
                }
            }
            String[] lines = list.toArray(new String[list.size()]);

            visitLines(context, lines, file);

        } catch (RuntimeException | IOException e) {
            if (EclipseResourceHelper.DEFAULT.isFileNotfoundException(e)) {
                // IFile.exists() at the beginning of this method does not work properly when
                // eclipse workspace is out of sync with real file system.
                // To avoid annoying error dialogs in such cases, we handle this here again
                context.resourcesToClean.add(file);
                return;
            }
            throw new CoreException(new Status(Status.ERROR, provider.getPluginContextProvider().getPluginID(), "Not able to visit resource", e));
        }

    }

    private void handleResource(ProjectModelBuilderContext context, IResource resource) {
        if (!(resource instanceof IFile)) {
            return;
        }
        IFile file = (IFile) resource;
        if (!provider.isFileHandled(file)) {
            return;
        }
        try {
            visitResource(context, file);
        } catch (CoreException e) {
            logError("Cannot visit resource:" + file, e);
        }
    }

    private void logError(String string, Throwable t) {
        EclipseUtil.logError(string, t, provider.getPluginContextProvider());

    }

    private void handleDelta(ProjectModelBuilderContext context, IResourceDelta delta) {
        if (delta == null) {
            return;
        }
        int flags = delta.getFlags();
        if (flags == IResourceDelta.MARKERS) {
            /* markers are ignored */
            return;
        }
        IResource resource = delta.getResource();
        if (resource instanceof IFile) {
            handleResource(context, resource);
            return;
        }
        /* go recursive into */
        for (IResourceDelta childDelta : delta.getAffectedChildren()) {
            handleDelta(context, childDelta);
        }
    }

    private class ProjectModelBuilderContext {

        private ProjectModelBuilderContext() {
        }

        List<IResource> resourcesToClean = new ArrayList<>();
        List<CreateModelEntryAction> actions = new ArrayList<>();

    }

    private abstract class AbstractModelUpdateData implements ModelUpdateAction {
        IResource resource;
        String line;
        String message;
        int lineNumber;

        public String getLine() {
            return line;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public String getMessage() {
            return message;
        }

        AbstractModelUpdateData(IResource resource) {
            this.resource = resource;
        }

        @Override
        public IResource getResource() {
            return resource;
        }

    }

    private class CreateModelEntryAction extends AbstractModelUpdateData {

        CreateModelEntryAction(IResource resource) {
            super(resource);
        }

        @Override
        public ActionType getType() {
            return ActionType.ADD;
        }

    }

    private class RemoveModelEntryAction extends AbstractModelUpdateData {

        RemoveModelEntryAction(IResource resource) {
            super(resource);
        }

        @Override
        public ActionType getType() {
            return ActionType.DELETE;
        }

    }

}