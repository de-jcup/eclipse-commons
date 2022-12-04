package de.jcup.eclipse.commons.ui;

/**
 * This color manager can be used in standalone SWT applications
 *
 */
public class StandaloneColorManager extends DefaultColorManager {

    private static ColorManager registeredStandaloneColorManager;

    /**
     * @return color manager for registeredStandaloneColorManager SWT programs,
     *         never <code>null</code>.
     * @throws IllegalStateException when no registeredStandaloneColorManager color
     *                               manager set but used
     */
    public static ColorManager getInstance() {
        if (registeredStandaloneColorManager == null) {
            throw new IllegalStateException("No registeredStandaloneColorManager color manager registered!");
        }
        return registeredStandaloneColorManager;
    }

    /**
     * Register color manager for registeredStandaloneColorManager SWT programs
     * 
     * @param registeredStandaloneColorManager
     */
    public static void register(ColorManager standalone) { // NO_UCD (test only)
        StandaloneColorManager.registeredStandaloneColorManager = standalone;
    }
}
