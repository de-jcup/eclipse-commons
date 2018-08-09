package de.jcup.eclipse.commons.codeassist;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Image;

public interface ProposalInfoProvider {

	Object getProposalInfo(IProgressMonitor monitor, Object target);

	Image getImage(Object target);

}
