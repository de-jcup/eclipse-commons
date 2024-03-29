/*
 * Copyright 2017 Albert Tregnaghi
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
package de.jcup.eclipse.commons.ui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;

import de.jcup.eclipse.commons.PluginContextProvider;

public class EclipseUtil {

    private static Font monoFont;

    public static void openInEditor(File file) throws PartInitException {
        IFileStore fileStore = EFS.getLocalFileSystem().getStore(new Path(file.getAbsolutePath()));

        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IDE.openEditorOnFileStore(page, fileStore);
    }

    public static void openInEditor(IFile file) throws PartInitException {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IDE.openEditor(page, file);
    }

    public static ImageDescriptor createImageDescriptor(String path, PluginContextProvider provider) {
        return createImageDescriptor(path, provider.getPluginID());
    }
    
    public static ImageDescriptor createImageDescriptor(String path, String pluginId) {
        if (path == null) {
            /* fall back if path null , so avoid NPE in eclipse framework */
            return ImageDescriptor.getMissingImageDescriptor();
        }
        if (pluginId == null) {
            /* fall back if pluginId null , so avoid NPE in eclipse framework */
            return ImageDescriptor.getMissingImageDescriptor();
        }
        Bundle bundle = Platform.getBundle(pluginId);
        if (bundle == null) {
            /*
             * fall back if bundle not available, so avoid NPE in eclipse framework
             */
            return ImageDescriptor.getMissingImageDescriptor();
        }
        URL url = FileLocator.find(bundle, new Path(path), null);

        ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);
        return imageDesc;
    }

    public static IEditorPart getActiveEditor() {
        IWorkbenchPage page = getActivePage();
        if (page==null) {
            return null;
        }
        IEditorPart activeEditor = page.getActiveEditor();
        return activeEditor;
    }

    /**
     * Returns active page or <code>null</code>
     * 
     * @return active page or <code>null</code>
     */
    public static IWorkbenchPage getActivePage() {
        if (!PlatformUI.isWorkbenchRunning()) {
            return null;
        }
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (window == null) {
            return null;
        }
        return window.getActivePage();
    }

    /**
     * Returns active workbench shell - or <code>null</code>
     * 
     * @return active workbench shell - or <code>null</code>
     */
    public static Shell getActiveWorkbenchShell() {
        IWorkbench workbench = getWorkbench();
        if (workbench == null) {
            return null;
        }
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        if (window == null) {
            return null;
        }
        Shell shell = window.getShell();
        return shell;
    }

    public static IWorkbenchWindow getActiveWorkbenchWindow() {
        IWorkbench workbench = getWorkbench();
        if (workbench == null) {
            return null;
        }
        IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();

        if (workbenchWindow != null) {
            return workbenchWindow;
        }
        /* fall back - try to execute in UI */
        WorkbenchWindowRunnable wwr = new WorkbenchWindowRunnable();
        getSafeDisplay().syncExec(wwr);
        return wwr.workbenchWindowFromUI;
    }

    public static IProject[] getAllProjects() {
        IProject[] projects = getWorkspace().getRoot().getProjects();
        return projects;
    }

    /**
     * Get image by path from image registry. If not already registered a new image
     * will be created and registered. If not createable a fallback image is used
     * instead
     * 
     * @param path
     * @param provider
     * @return image
     */
    public static Image getImage(String path, PluginContextProvider provider) {
        ImageRegistry imageRegistry = getImageRegistry(provider);
        if (imageRegistry == null) {
            return null;
        }
        Image image = imageRegistry.get(path);
        if (image == null) {
            ImageDescriptor imageDesc = createImageDescriptor(path, provider.getPluginID());
            image = imageDesc.createImage();
            if (image == null) {
                image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
            }
            imageRegistry.put(path, image);
        }
        return image;
    }

    public static Display getSafeDisplay() {
        Display display = Display.getCurrent();
        if (display == null) {
            display = Display.getDefault();
        }
        return display;
    }

    public static IWorkspace getWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }

    public static void safeAsyncExec(Runnable runnable) {
        getSafeDisplay().asyncExec(runnable);
    }

    public static void safeSyncExec(Runnable runnable) {
        getSafeDisplay().syncExec(runnable);
    }

    public static void throwCoreException(String message, PluginContextProvider provider) throws CoreException {
        throw new CoreException(new Status(IStatus.ERROR, provider.getPluginID(), message));

    }

    public static void throwCoreException(String message, Exception e, PluginContextProvider provider) throws CoreException {
        throw new CoreException(new Status(IStatus.ERROR, provider.getPluginID(), message, e));

    }

    public static String resolveMessageIfNotSet(String message, Throwable cause) {
        if (message == null) {
            if (cause == null) {
                message = "Unknown";
            } else {
                message = cause.getMessage();
            }
        }
        return message;
    }

    public static final Font getMonospaceFont() {
        if (monoFont == null) {
            monoFont = createMonospaceFont();
        }
        return monoFont;
    }

    private static Font createMonospaceFont() {
        // to get monospaced font is not really simple,
        // see https://bugs.eclipse.org/bugs/show_bug.cgi?id=48055
        // see also https://bugs.eclipse.org/bugs/attachment.cgi?id=238603
        int size = 14;
        int style = SWT.None;
        List<FontData> fontDataList = new ArrayList<FontData>();
        fontDataList.add(new FontData("Consolas", size, style)); // windows +
                                                                 // general
        fontDataList.add(new FontData("Monospace", size, style)); // linux_gtk
        fontDataList.add(new FontData("adobe-courier", size, style));// linux
        fontDataList.add(new FontData("Courier New", size, style));
        Display device = EclipseUtil.getSafeDisplay();
        FontData[] data = fontDataList.toArray(new FontData[fontDataList.size()]);
        return new Font(device, data);
    }

    private static ImageRegistry getImageRegistry(PluginContextProvider provider) {
        if (provider == null) {
            return null;
        }
        AbstractUIPlugin mainActivator = provider.getActivator();
        if (mainActivator == null) {
            return null;
        }
        return mainActivator.getImageRegistry();
    }

    /**
     * Returns workbench or <code>null</code>
     * 
     * @return workbench or <code>null</code>
     */
    private static IWorkbench getWorkbench() {
        if (!PlatformUI.isWorkbenchRunning()) {
            return null;
        }
        IWorkbench workbench = PlatformUI.getWorkbench();
        return workbench;
    }

    private static class WorkbenchWindowRunnable implements Runnable {
        IWorkbenchWindow workbenchWindowFromUI;

        @Override
        public void run() {
            IWorkbench workbench = getWorkbench();
            if (workbench == null) {
                return;
            }
            workbenchWindowFromUI = workbench.getActiveWorkbenchWindow();
        }

    }

    public static void logInfo(String info, PluginContextProvider provider) {
        if (provider == null) {
            return;
        }
        getLog(provider).log(new Status(IStatus.INFO, provider.getPluginID(), info));
    }

    public static void logWarn(String warning, PluginContextProvider provider) {
        if (provider == null) {
            return;
        }
        getLog(provider).log(new Status(IStatus.ERROR, provider.getPluginID(), warning));
    }

    public static void logError(String error, Throwable t, PluginContextProvider provider) {
        if (provider == null) {
            return;
        }
        getLog(provider).log(new Status(IStatus.ERROR, provider.getPluginID(), error, t));
    }

    private static ILog getLog(PluginContextProvider provider) {
        ILog log = provider.getActivator().getLog();
        return log;
    }

    public static void openInExternalBrowser(URL url, PluginContextProvider provider) {
        if (url == null) {
            return;
        }
        try {
            // Open default external browser
            IWorkbenchBrowserSupport browserSupport = getWorkbench().getBrowserSupport();
            IWebBrowser externalBrowser = browserSupport.getExternalBrowser();
            externalBrowser.openURL(url);

        } catch (Exception ex) {
            logError("Cannot open external browser url", ex, provider);
        }
    }

}
