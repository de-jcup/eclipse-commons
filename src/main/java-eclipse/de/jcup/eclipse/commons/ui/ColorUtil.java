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

import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class ColorUtil {

	/**
	 * @param jfaceColorName
	 * @return converted jface color name from {@link JFacePreferences} or <code>null</code> if not found
	 */
	public static String convertJFaceColorToWeb(String jfaceColorName) {
		ColorDescriptor descriptor = JFaceResources.getColorRegistry().getColorDescriptor(jfaceColorName);
		if (descriptor==null){
			return null;
		}
		return ColorUtil.convertToHexColor(descriptor);
	}
	
	/**
	 * Returns a web color in format "#RRGGBB"
	 * 
	 * @param color
	 * @return web color as string or <code>null</code> when given color was null
	 */
	public static String convertToHexColor(Color color) {
		if (color == null) {
			return null;
		}
		return convertToHexColor(color.getRGB());
	}

	/**
	 * @param rgb
	 * @return hex color (e.g. #000000 for black) or <code>null</code> when RGB was null
	 */
	public static String convertToHexColor(RGB rgb) {
		if (rgb == null) {
			return null;
		}
		String hex = String.format("#%02x%02x%02x", rgb.red, rgb.green, rgb.blue);
		return hex;
	}
	

	public static String convertToHexColor(ColorDescriptor descriptor) {
		return convertToHexColor(resolveRGB(descriptor));
	}

	/**
	 * @param descriptor
	 * @return RGB or <code>null</code> when descriptor was <code>null</code>
	 */
	public static RGB resolveRGB(ColorDescriptor descriptor) {
		if (descriptor==null){
			return null;
		}
		Color color = descriptor.createColor(EclipseUtil.getSafeDisplay());
		RGB rgb = color.getRGB();
		descriptor.destroyColor(color);
		return rgb;
	}
}
