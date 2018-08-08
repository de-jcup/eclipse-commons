package de.jcup.eclipse.commons.keyword;
/*
 * Copyright 2018 Albert Tregnaghi
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
 
/**
 * A document keyword can be used for syntax highlighting and also for automated tooltip support
 * @author Albert Tregnaghi
 *
 */
public interface DocumentKeyWord{

	/**
	 * @return text of this key word to identify
	 */
	String getText();

	/**
	 * @return true when keyword is not recognized when EOF is following
	 */
	boolean isBreakingOnEof();
	
	/**
	 * 
	 * @return tool tip text or <code>null</code> if none defined
	 */
	String getTooltip();
	
	/**
	 * @return link to documentation or <code>null</code> if none defined
	 */
	String getLinkToDocumentation();
	
}
