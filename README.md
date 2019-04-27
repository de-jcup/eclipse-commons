# de-jcup-eclipse-commons
[![Build status:](https://travis-ci.org/de-jcup/eclipse-commons.svg?branch=master)](https://travis-ci.org/de-jcup/eclipse-commons)
*You will find the sources under https://github.com/de-jcup/eclipse-commons*

A library containing reuseable eclipse solutions. This is a simple approach to resolve the
problem of too much copy & waste in de-jcup plugins. Some bigger and trick parts are bundled 
here as a simple library which can be used by plugins.

## Current version
0.6.1
- added ChangeableComboFieldEditor as component

## Howto use
See Testcase plugin - there are all features used.

## way to build
- currently build-library.jardesc is used (ugly but it works)
- version is not used (currently) in filenames to avoid version hopping (and versions
  are not 100% setup because not build by tool like maven or gradle)


# History:
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


