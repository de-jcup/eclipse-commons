package de.jcup.eclipse.commons;

import java.util.ArrayList;
import java.util.List;

public class NoWordListBuilder implements WordListBuilder {

		public static NoWordListBuilder INSTANCE = new NoWordListBuilder();
	
		private NoWordListBuilder() {

		}

		private List<String> list = new ArrayList<>(0);

		@Override
		public List<String> build(String source) {
			return list;
		}

	}