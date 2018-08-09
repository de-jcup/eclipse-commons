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
package de.jcup.eclipse.commons.codeassist;

import static java.util.Collections.*;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import de.jcup.eclipse.commons.WordListBuilder;

public class SimpleWordCodeCompletion extends AbstractWordCodeCompletition implements ProposalProviderSupport {

	private Set<ProposalProvider> additionalWordsCache = new HashSet<>();

	private SortedSet<ProposalProvider> allWordsCache = new TreeSet<>();

	private WordListBuilder wordListBuilder;

	/**
	 * Adds an additional word - will be removed on all of {@link #reset()}
	 * 
	 * @param word
	 */
	public void add(String word) {
		if (word == null) {
			return;
		}
		if (!allWordsCache.isEmpty()) {
			allWordsCache.clear(); // reset the all words cache so rebuild will
									// be triggered
		}
		additionalWordsCache.add(new SimpleWordProposalProvider(word.trim()));
	}

	@Override
	public Set<ProposalProvider> calculate(String source, int offset) {
		rebuildCacheIfNecessary(source);
		if (offset == 0) {
			return unmodifiableSet(allWordsCache);
		}
		String wanted = getTextbefore(source, offset);
		return filter(allWordsCache, wanted);
	}

	@Override
	public void reset() {
		allWordsCache.clear();
		additionalWordsCache.clear();
	}

	Set<ProposalProvider> filter(SortedSet<ProposalProvider> allWords, String wanted) {
		if (wanted == null || wanted.isEmpty()) {
			return allWords;
		}
		LinkedHashSet<ProposalProvider> filtered = new LinkedHashSet<>();
		LinkedHashSet<ProposalProvider> addAfterEnd = new LinkedHashSet<>();
		String wantedLowerCase = wanted.toLowerCase();

		for (ProposalProvider info : allWords) {
			String wordLowerCase = info.getLabel().toLowerCase();
			if (wordLowerCase.startsWith(wantedLowerCase)) {
				filtered.add(info);
			} else if (wordLowerCase.indexOf(wantedLowerCase) != -1) {
				addAfterEnd.add(info);
			}
		}
		filtered.addAll(addAfterEnd);
		/* remove wanted itself */
		filtered.remove(wanted);
		return filtered;
	}

	private void rebuildCacheIfNecessary(String source) {
		if (allWordsCache.isEmpty()) {
			allWordsCache.addAll(additionalWordsCache);
			List<String> allWordsBuild = getWordListBuilder().build(source);
			for (String word: allWordsBuild){
				if (word.isEmpty()){
					// we do not want an empty String
					continue;
				}
				allWordsCache.add(new SimpleWordProposalProvider(word));
			}
		}
	}

	public WordListBuilder getWordListBuilder() {
		if (wordListBuilder == null) {
			wordListBuilder = new SimpleWordListBuilder();
		}
		return wordListBuilder;
	}

	public void setWordListBuilder(WordListBuilder wordListBuilder) {
		this.wordListBuilder = wordListBuilder;
	}
}
