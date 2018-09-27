package de.jcup.eclipse.commons.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.WindowEvent;

import de.jcup.eclipse.commons.PluginContextProvider;

/**
 * Use this listener when you want to have all links shown in external browser.
 * Normally you are not able to skip normal behaviour of embedded SWT browser on opening internal/external links.
 * @author Albert Tregnaghi
 *
 */
public class OpenLinksInExternalBrowserListener implements BrowserInformationListener{
	/**
	 * this is an special way to path through, because normally you got no possiblity to handle "open in new window" link usage  
	 */
	private static final Browser DELEGATION_BROWSER = new Browser(EclipseUtil.getActiveWorkbenchShell(), SWT.EMBEDDED);
	
	static{
		DELEGATION_BROWSER.addLocationListener(new LocationListener() {
			
			@Override
			public void changing(LocationEvent event) {
				// <A2> stop the real change and do now open the real external browser again
				event.doit=false;
				URL url;
				PluginContextProvider provider = PluginContextProviderRegistry.getProvider();
				try {
					url = new URL(event.location);
					EclipseUtil.openInExternalBrowser(url, provider);
				} catch (MalformedURLException e) {
					EclipseUtil.logError("Cannot setup url:"+event.location, e, provider);
				}
				
			}
			
			@Override
			public void changed(LocationEvent event) {
				
			}
		});
		
		DELEGATION_BROWSER.addOpenWindowListener(new OpenWindowListener() {
			
			@Override
			public void open(WindowEvent event) {
				// delegation done - now at <A1>, next <A2>
				event.required=true; // stop showing window - changing will force to external browser
			}
		});
	}

	public OpenLinksInExternalBrowserListener(){
	}

	@Override
	public void changing(LocationEvent event) {
		
	}

	@Override
	public void changed(LocationEvent event) {
		
	}

	@Override
	public void open(WindowEvent event) {
		event.browser=DELEGATION_BROWSER; // instead of using browser implementation (system dependent) to open, we use delegation browser (see <A1> for next steps)
	}
	
	
	
}