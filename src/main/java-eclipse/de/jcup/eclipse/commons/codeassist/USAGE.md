# About:
Here are different utilities for code assistance in eclipse

## MultipleContentAssistProcessor
This is a specialized assist processor which can provide multiple IContentAssistProcessor instances at same time. Interesting when having for example
templating and simple word code completion...

## Keyword content assistence 
A special Keyword content assistence with special proposal info for keywords

### Example:
Please look into `TestCaseSourceViewerConfiguration.java` which uses `TestcaseContentAssistProcessor.java` to provide this