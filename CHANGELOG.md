# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added 
- introduce `CHANGELOG.md`
- added ColorManager and implementation + some minor rules, detectors etc.

### Fixed
- Handle "File not found" errors on building model #45

## [1.1.1]

### Fixed 
- Handled missing class files in release 1.1.0

### Changed
- Add and use new maven wrapper

## [1.1.0]

### Changed
- Export all relevant packages in eclipse-commons

## [1.0.0]

### Changed 
- changes to provide maven build with tycho

## [0.7.1]

### Fixed 
- speedup workspace model build (possibility) #158

## [0.7.0]

### Added
- added SimpleStringUtils.trimRight impl
- multiple changes for workspace model
- resource helper can now open editor
- workspace model support final
- added some ui parts for preferences + UIMasterSlaveSupport

## [0.6.1]

### Added
- added ChangeableComboFieldEditor as component
 
### Fixed 
- fixed tab problems #21 and #22

## [0.6.0]
 
### Added
-  introducing index calclator
- adding automatically sorting of multiproposals
  corresponding to their relevance
  
### Changed
- refactored index calculation

## [0.5.3]

### Added
- introduced CSS and PlainTextToHTML interfaces plus default implementations

### Fixed
- Simple completion proposal will not add a new line at end automatically any more (except overridden)
- ReducedBrowserControl had a bug on plain text, did always set only text information

## [0.5.2]

### Added
- Improved testcase
- documentation added in different `USAGE.md` files

### Fixed
- bugfix, see #15

## [0.5.1]

### Fixed
- Bugfix on Template support

## [0.5.0]

### Added
- introduce ReplaceTabBySpaceSupport
- CaretInfosupport
- Template support
- Handling UTF-8 in tooltips
- Eclipse util improvments

## [0.4.2]

### Added
- Common EclipseResourceHelper

## [0.4.1]

### Changed
- improved html links, improved testcase

## [0.3.0]

### Added
- task support

