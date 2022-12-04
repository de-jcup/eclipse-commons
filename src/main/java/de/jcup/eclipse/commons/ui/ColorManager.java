package de.jcup.eclipse.commons.ui;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public interface ColorManager {

    /**
     * Resolve color by rgb. If the color is not existing it will be created,
     * otherwise the color instance is reused
     * 
     * @param rgb rgb color or <code>null</code>
     * @return color or <code>null</code> when given rgb is <code>null</code>
     */
    Color getColor(RGB rgb);

    /**
     * Dispose all colors
     */
    void dispose();
}