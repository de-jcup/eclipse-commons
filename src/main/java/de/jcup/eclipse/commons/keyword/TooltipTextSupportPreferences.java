package de.jcup.eclipse.commons.keyword;

public interface TooltipTextSupportPreferences {

	/**
	 * @return <code>true</code> when tooltips are enabled
	 */
	boolean areTooltipsForKeyWordsEnabled();

	/**
	 *
	 * @return web color for comment in tooltip text
	 */
	String getCommentColorWeb();

	/**
	 * @return all supported keywords supported by {@link TooltipTextSupport} instance
	 */
	DocumentKeyWord[] getAllKeywords();

	default boolean isIgnoreCaseOnKeywordText() {
	    return false;
	}

}
