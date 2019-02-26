package testcase.de.jcup.eclipse.commons;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.jcup.eclipse.commons.PluginContextProvider;
import de.jcup.eclipse.commons.keyword.TooltipTextSupport;
import de.jcup.eclipse.commons.resource.EclipseResourceInputStreamProvider;
import de.jcup.eclipse.commons.tasktags.AbstractConfigurableTaskTagsSupportProvider;
import de.jcup.eclipse.commons.ui.PluginContextProviderRegistry;

/**
 * The activator class controls the plug-in life cycle.
 */
public class TestcaseActivator extends AbstractUIPlugin implements PluginContextProvider {

	// The plug-in ID
	public static final String PLUGIN_ID = "testcase.de.jcup.eclipse.commons"; //$NON-NLS-1$

	// The shared instance
	private static TestcaseActivator plugin;


	private AbstractConfigurableTaskTagsSupportProvider taskSupportProvider;

	private TestCaseColorManager testCaseColorManager;

	/**
	 * The constructor
	 */
	public TestcaseActivator() {
		testCaseColorManager = new TestCaseColorManager();
		taskSupportProvider = new TestcaseTaskTagsSupportProvider(this) ;
		TooltipTextSupport.setTooltipInputStreamProvider(new EclipseResourceInputStreamProvider(PLUGIN_ID));
		PluginContextProviderRegistry.register(this);
		
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
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		taskSupportProvider.getTodoTaskSupport().uninstall();
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
}
