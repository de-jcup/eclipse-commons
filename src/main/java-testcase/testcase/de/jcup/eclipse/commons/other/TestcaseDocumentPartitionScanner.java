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
package testcase.de.jcup.eclipse.commons.other;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

public class TestcaseDocumentPartitionScanner extends RuleBasedPartitionScanner {

	public TestcaseDocumentPartitionScanner() {
		IToken boldText = createToken(TestcaseDocumentIdentifiers.MAKE_ME_BOLD);
		IToken italicText = createToken(TestcaseDocumentIdentifiers.MAKE_ME_ITALIC);

		List<IPredicateRule> rules = new ArrayList<>();
		rules.add(new SingleLineRule("**", "**", boldText, (char) -1, true));
		rules.add(new SingleLineRule("<", ">", italicText, (char) -1, true));

		setPredicateRules(rules.toArray(new IPredicateRule[rules.size()]));
	}

	private IToken createToken(TestcaseDocumentIdentifiers identifier) {
		return new Token(identifier.getId());
	}
}
