/*
 * Copyright 2019 Albert Tregnaghi
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
package de.jcup.eclipse.commons;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.manipulation.ContainerCreator;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.FileInfoMatcherDescription;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceFilterDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;

public class EclipseResourceHelper {
    public static final EclipseResourceHelper DEFAULT = new EclipseResourceHelper();

    private static String FILE_FILTER_ID = "org.eclipse.ui.ide.patternFilterMatcher";

    private final IProgressMonitor NULL_MONITOR = new NullProgressMonitor();

    /*
     * This method is a dirty workaround to check if an exception is about a file
     * not found problem. Eclipse does throw an internal resource exception
     * (internal package) when a IFile content is fetched, based on a core
     * exception, containing as cause an exception with a message
     * "File not found: ..." There is just no dedicated FileNotFoundException or
     * something ...
     */
    public boolean isFileNotfoundException(Exception e) {

        if (e instanceof CoreException) {
            CoreException ce = (CoreException) e;
            IStatus status = ce.getStatus();
            if (status == null) {
                return false;
            }
            Throwable exception = status.getException();
            if (exception == null) {
                return false;
            }
            String message = exception.getMessage();
            if (message == null) {
                return false;
            }
            if (message.toLowerCase().indexOf("file not found") != -1) {
                return true;
            }
        }
        return false;
    }

    public void addFileFilter(IProject newProject, String pattern, IProgressMonitor monitor) throws CoreException {
        FileInfoMatcherDescription matcherDescription = new FileInfoMatcherDescription(FILE_FILTER_ID, pattern);
        /*
         * ignore the generated files - .project and .gitignore at navigator etc.
         */
        newProject.createFilter(IResourceFilterDescription.EXCLUDE_ALL | IResourceFilterDescription.FILES, matcherDescription, IResource.BACKGROUND_REFRESH, monitor);
    }

    public IFile createFile(IFolder folder, String name, String contents) throws CoreException {
        return createFile(folder.getFile(name), name, contents);
    }

    public IFile createFile(IProject project, String name, String contents) throws CoreException {
        return createFile(project.getFile(name), name, contents);
    }

    public IFolder createFolder(IPath path) throws CoreException {
        return createFolder(path, null);
    }

    public IFolder createFolder(String portableFolderPath) throws CoreException {
        return createFolder(portableFolderPath, null);
    }

    public IFolder createFolder(String portableFolderPath, IProgressMonitor monitor) throws CoreException {
        Path fullPath = new Path(portableFolderPath);
        return createFolder(fullPath, monitor);
    }

    /**
     * Reads file as text. when not existing or null an empty text will be returned
     * 
     * @param file
     * @param provider
     * @param failureInfo
     * @return text, or <code>null</code>
     * @throws CoreException
     */
    public String readAsText(IFile file, PluginContextProvider provider, String failureInfo) throws CoreException {
        if (file == null) {
            return null;
        }
        if (!file.exists()) {
            return null;
        }
        return readAsText(file.getContents(), provider, failureInfo);
    }

    /**
     * Reads stream as text. When stream null an empty text will be returned
     * 
     * @param stream
     * @param provider
     * @param path
     * @return text, or <code>null</code>
     * @throws CoreException
     */
    public String readAsText(InputStream stream, PluginContextProvider provider, String path) throws CoreException {
        if (stream == null) {
            return null;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            StringBuilder sb = new StringBuilder();
            String line = null;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (!firstLine) {
                    sb.append("\n");
                }
                firstLine = false;
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            throw new CoreException(new Status(IStatus.ERROR, provider.getPluginID(), "Cannot get file in plugin from path:" + path, e));
        }
    }

    public IFolder createFolder(IPath path, IProgressMonitor monitor) throws CoreException {
        if (monitor == null) {
            monitor = NULL_MONITOR;
        }
        ContainerCreator creator = new ContainerCreator(ResourcesPlugin.getWorkspace(), path);
        IContainer container = creator.createContainer(monitor);
        if (container instanceof IFolder) {
            return (IFolder) container;
        }
        return null;
    }

    public IFile createLinkedFile(IContainer container, IPath linkPath, File linkedFileTarget) throws CoreException {
        IFile iFile = container.getFile(linkPath);
        iFile.createLink(new Path(linkedFileTarget.getAbsolutePath()), IResource.ALLOW_MISSING_LOCAL, NULL_MONITOR);
        return iFile;
    }

    public IFile createLinkedFile(IContainer container, IPath linkPath, PluginContextProvider plugin, IPath linkedFileTargetPath) throws CoreException {
        File file = getFileInPlugin(plugin, linkedFileTargetPath);
        IFile iFile = container.getFile(linkPath);
        iFile.createLink(new Path(file.getAbsolutePath()), IResource.ALLOW_MISSING_LOCAL, NULL_MONITOR);
        return iFile;
    }

    public IFolder createLinkedFolder(IContainer container, IPath linkPath, File linkedFolderTarget) throws CoreException {
        IFolder folder = container.getFolder(linkPath);
        folder.createLink(new Path(linkedFolderTarget.getAbsolutePath()), IResource.ALLOW_MISSING_LOCAL, NULL_MONITOR);
        return folder;
    }

    public IFolder createLinkedFolder(IContainer container, IPath linkPath, PluginContextProvider plugin, IPath linkedFolderTargetPath) throws CoreException {
        File file = getFileInPlugin(plugin, linkedFolderTargetPath);
        IFolder iFolder = container.getFolder(linkPath);
        iFolder.createLink(new Path(file.getAbsolutePath()), IResource.ALLOW_MISSING_LOCAL, NULL_MONITOR);
        return iFolder;
    }

    public IProject createLinkedProject(String projectName, PluginContextProvider plugin, IPath linkPath) throws CoreException {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IProject project = workspace.getRoot().getProject(projectName);

        IProjectDescription desc = workspace.newProjectDescription(projectName);
        File file = getFileInPlugin(plugin, linkPath);
        IPath projectLocation = new Path(file.getAbsolutePath());
        if (Platform.getLocation().equals(projectLocation))
            projectLocation = null;
        desc.setLocation(projectLocation);

        project.create(desc, NULL_MONITOR);
        if (!project.isOpen())
            project.open(NULL_MONITOR);

        return project;
    }

    public File createTempFileInPlugin(Plugin plugin, IPath path) {
        IPath stateLocation = plugin.getStateLocation();
        stateLocation = stateLocation.append(path);
        return stateLocation.toFile();
    }

    /**
     * Returns the file or <code>null</code>
     * 
     * @param path
     * @return file or <code>null</code>
     * @throws CoreException
     */
    public File toFile(IPath path) throws CoreException {
        if (path == null) {
            return null;
        }
        IFileStore fileStore = FileBuffers.getFileStoreAtLocation(path);

        File file = null;
        file = fileStore.toLocalFile(EFS.NONE, NULL_MONITOR);
        return file;
    }

    public File toFile(IResource resource) throws CoreException {
        if (resource == null) {
            return toFile((IPath) null);
        }
        return toFile(resource.getLocation());
    }

    public File getFileInPlugin(PluginContextProvider contextProvider, IPath path) throws CoreException {
        if (path == null) {
            throw new CoreException(new Status(IStatus.ERROR, contextProvider.getPluginID(), "Path may not be null to get file in plugin:" + path));
        }
        try {
            AbstractUIPlugin activator = contextProvider.getActivator();
            Bundle bundle = activator.getBundle();
            URL installURL = bundle.getEntry(path.toString());
            if (installURL == null) {
                throw new CoreException(new Status(IStatus.ERROR, contextProvider.getPluginID(), "Install url may not be null for path:" + path));
            }
            URL localURL = FileLocator.toFileURL(installURL);
            return new File(localURL.getFile());
        } catch (IOException e) {
            throw new CoreException(new Status(IStatus.ERROR, contextProvider.getPluginID(), "Cannot get file in plugin from path:" + path, e));
        }
    }

    public File getFileInPlugin(String path, PluginContextProvider plugin) throws IOException {
        return getFileInPlugin(path, plugin.getPluginID());
    }

    public File getFileInPlugin(String path, String pluginId) throws IOException {
        if (path == null) {
            throw new IOException("Cannot get file for path not set!");
        }
        Bundle bundle = Platform.getBundle(pluginId);
        if (bundle == null) {
            throw new IOException("Bundle not found:" + pluginId);
        }
        URL url = bundle.getEntry(path);
        if (url == null) {
            /* PDE workaround */
            String path2 = "bin/" + path;
            url = bundle.getEntry(path2);
            if (url == null) {
                throw new IOException("Cannot find file at path:" + path);
            }

        }
        URL resolvedFileURL = FileLocator.toFileURL(url);
        if (resolvedFileURL == null) {
            throw new FileNotFoundException("Cannot convert URL to file:" + resolvedFileURL);
        }

        // We need to use the 3-arg constructor of URI in order to properly
        // escape file system chars
        URI resolvedURI;
        try {
            resolvedURI = new URI(resolvedFileURL.getProtocol(), resolvedFileURL.getPath(), null);
            File file = new File(resolvedURI);
            if (!file.exists()) {
                throw new FileNotFoundException("Cannot convert URL to file:" + resolvedFileURL);
            }
            return file;
        } catch (URISyntaxException e) {
            throw new IOException("Cannot find file at resolvedFileURL:" + resolvedFileURL, e);
        }
    }

    /**
     * Returns java.util file of current editor or <code>null</code>
     * 
     * @param editor
     * @return file of current editor or <code>null</code>
     * @throws CoreException
     */
    public File getFileOfEditor(IEditorPart editor) throws CoreException {
        if (editor == null) {
            return null;
        }
        IEditorInput input = editor.getEditorInput();
        File editorFile = null;

        if (input instanceof FileEditorInput) {
            /* standard opening with eclipse IDE inside */
            FileEditorInput finput = (FileEditorInput) input;
            IFile iFile = finput.getFile();
            editorFile = EclipseResourceHelper.DEFAULT.toFile(iFile);
        } else if (input instanceof FileStoreEditorInput) {
            /*
             * command line : eclipse xyz.adoc does use file store editor input ....
             */
            FileStoreEditorInput fsInput = (FileStoreEditorInput) input;
            editorFile = fsInput.getAdapter(File.class);
        }
        return editorFile;
    }

    public IFile toIFile(File file) {
        IFileStore fileStore = EFS.getLocalFileSystem().getStore(file.toURI());

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IFile[] fileResults = workspace.getRoot().findFilesForLocationURI(fileStore.toURI());
        if (fileResults == null || fileResults.length == 0) {
            return null;
        }
        return fileResults[0];
    }

    public IFile toIFile(IPath path) {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IFile fileResult = workspace.getRoot().getFile(path);
        return fileResult;
    }

    public IFile toIFile(String pathString) {
        IPath path = Path.fromOSString(pathString);
        return toIFile(path);
    }

    public IPath toPath(File tempFolder) {
        if (tempFolder == null) {
            throw new IllegalArgumentException("'tempFolder' may not be null");
        }
        IPath path = Path.fromOSString(tempFolder.getAbsolutePath());
        return path;
    }

    private IFile createFile(IFile file, String name, String contents) throws CoreException {
        if (contents == null)
            contents = "";
        InputStream inputStream = new ByteArrayInputStream(contents.getBytes());
        file.create(inputStream, true, NULL_MONITOR);
        return file;
    }

    void deleteRecursive(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                deleteRecursive(child);
            }
            file.delete();
        } else {
            file.delete();
        }
    }

    public IEditorPart openInEditor(File file) throws PartInitException {
        IFileStore fileStore = EFS.getLocalFileSystem().getStore(new Path(file.getAbsolutePath()));

        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        return IDE.openEditorOnFileStore(page, fileStore);
    }

    public IEditorPart openInEditor(IFile file) throws PartInitException {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        return IDE.openEditor(page, file);
    }

}