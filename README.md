# de-jcup-eclipse-commons
[![Java CI with Maven](https://github.com/de-jcup/eclipse-commons/actions/workflows/maven.yml/badge.svg)](https://github.com/de-jcup/eclipse-commons/actions/workflows/maven.yml)

*You will find the sources under https://github.com/de-jcup/eclipse-commons*

A library containing reuseable eclipse solutions. This is a simple approach to resolve the
problem of too much copy & waste in de-jcup plugins. Some bigger and trick parts are bundled 
here as a simple library which can be used by plugins.

## Current version
1.0.0
- changes to provide maven build with tycho

## Howto develop/test
For testing and development you can start it as a normal eclipse plugin inside your PDT

## Howto use/integrate
For integration you should use the library simply as a normal maven or gradle dependency.

## Howto build with maven

```
./mvn clean verify
```


# History:
0.7.1
- speedup workspace model build (possibility) #158

0.7.0
- added SimpleStringUtils.trimRight impl
- multiple changes for workspace model
- resource helper can now open editor
- workspace model support final
- added some ui parts for preferences + UIMasterSlaveSupport
0.6.1
- added ChangeableComboFieldEditor as component
- fixed tab problems #21 and #22
0.6.0 
- refactored index calculation
  introducing index calclator
- adding automatically sorting of multiproposals
  corresponding to their relevance
0.5.3
- Simple completion proposal will not add a new line at end automatically any more (except overridden)
- ReducedBrowserControl had a bug on plain text, did always set only text information
- introduced CSS and PlainTextToHTML interfaces plus default implementations


0.5.2 
- Improved testcase
- bugfix, see #15
- documentation added in different USAGE.md files

0.5.1
- Bugfix on Template support

0.5.0
- ReplaceTabBySpaceSupport
- CaretInfosupport
- Template support
- Handling utf-8 in tooltips
- Eclipse util improvments

0.4.2
- Common EclipseResourceHelper

0.4.1 
- implemented hyperlinking colors using colors from jface, see https://github.com/de-jcup/eclipse-commons/issues/7

0.4.0
- improved html links, improved testcase

0.3.x
- task support


