package testcase.de.jcup.eclipse.commons;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.jcup.eclipse.commons.PluginContextProvider;
import de.jcup.eclipse.commons.keyword.TooltipTextSupport;
import de.jcup.eclipse.commons.resource.EclipseResourceInputStreamProvider;
import de.jcup.eclipse.commons.tasktags.AbstractConfigurableTaskTagsSupportProvider;
import de.jcup.eclipse.commons.templates.TemplateSupportProvider;
import de.jcup.eclipse.commons.ui.PluginContextProviderRegistry;
import testcase.de.jcup.eclipse.commons.tasktags.TestcaseTaskTagsSupportProvider;
import testcase.de.jcup.eclipse.commons.template.TestCaseTemplateSupportConfig;
import testcase.de.jcup.eclipse.commons.workspacemodel.TestCaseModel;
import testcase.de.jcup.eclipse.commons.workspacemodel.TestCaseModelSupportProvider;

/**
 * The activator class controls the plug-in life cycle.
 */
public class TestcaseActivator extends AbstractUIPlugin implements PluginContextProvider {

	// The plug-in ID
	public static final String PLUGIN_ID = "testcase.de.jcup.eclipse.commons"; //$NON-NLS-1$

	// The shared instance
	private static TestcaseActivator plugin;
	
	private TemplateSupportProvider templateSupportProvider;

	private AbstractConfigurableTaskTagsSupportProvider taskSupportProvider;

	private TestCaseColorManager testCaseColorManager;

	private TestCaseModelSupportProvider testCaseModelSupportProvider;

	/**
	 * The constructor
	 */
	public TestcaseActivator() {
		testCaseColorManager = new TestCaseColorManager();
		taskSupportProvider = new TestcaseTaskTagsSupportProvider(this) ;
		testCaseModelSupportProvider = new TestCaseModelSupportProvider(this);
		templateSupportProvider = new TemplateSupportProvider(new TestCaseTemplateSupportConfig(),this);
		TooltipTextSupport.setTooltipInputStreamProvider(new EclipseResourceInputStreamProvider(PLUGIN_ID));
		PluginContextProviderRegistry.register(this);
		
	}
	public TestCaseModelSupportProvider getTestCaseModelSupportProvider() {
		return testCaseModelSupportProvider;
	}
	
	public TestCaseColorManager getTestCaseColorManager() {
		return testCaseColorManager;
	}

	public AbstractConfigurableTaskTagsSupportProvider getTaskSupportProvider() {
		return taskSupportProvider;
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		taskSupportProvider.getTodoTaskSupport().install();
		testCaseModelSupportProvider.getWorkspaceModelSupport().install();
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		taskSupportProvider.getTodoTaskSupport().uninstall();
		testCaseModelSupportProvider.getWorkspaceModelSupport().uninstall();
		super.stop(context);
	}
	
	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static TestcaseActivator getDefault() {
		return plugin;
	}
	
	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 *
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	@Override
	public AbstractUIPlugin getActivator() {
		return this;
	}

	@Override
	public String getPluginID() {
		return PLUGIN_ID;
	}
	public TestCaseModel getProjectModel() {
		return getTestCaseModelSupportProvider().getWorkspaceModelSupport().getModel();
	}
	
    public TemplateSupportProvider getTemplateSupportProvider() {
        return templateSupportProvider;
    }
}
