# de-jcup-eclipse-commons
*You will find the sources under https://github.com/de-jcup/eclipse-commons*

A library containing reuseable eclipse solutions. This is a simple approach to resolve the
problem of too much copy & waste in de-jcup plugins. Some bigger and trick parts are bundled 
here as a simple library which can be used by plugins.

## Current version
0.5.1 Bugfix on Template support

## Howto use
See Testcase plugin - there are all features used.

## way to build
- currently build-library.jardesc is used (ugly but it works)
- version is not used (currently) in filenames to avoid version hopping (and versions
  are not 100% setup because not build by tool like maven or gradle)


# History:
0.5.0
- ReplaceTabBySpaceSupport
- CaretInfosupport
- Template support
- Handling utf-8 in tooltips
- Eclipse util improvments

0.4.1 implemented hyperlinking colors using colors from jface, see https://github.com/de-jcup/eclipse-commons/issues/7
0.4.0 improved html links, improved testcase
0.3.x task support
0.4.2 Common EclipseResourceHelper

