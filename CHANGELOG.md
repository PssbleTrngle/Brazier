# Changelog
All notable changes to this project will be documented in this file.

## [1.1.0] 2020-08-17
### Added
- Cyclinder distance calculation
- Config option to choose distance calculation

## [1.1.1] - 2020-08-17
### Added
- Auto generated changelog

## [1.1.1] - 2020-08-19
### Fixed
- Particle factory causing crash on startup

## [1.2.0] - 2020-08-28
### Added
- Living torches to indicate the brazier range

### Fixed
- Braziers not loading with the correct range after world reload

## [1.2.1] - 2020-09-01
### Fixed
- Missing torch texture (closes #22)
- Moved brazier to misc tab (closes #21)

### Added
- Crazed spawn egg
- Config option for crazed spawn chance

### Changed
- Increased default crazed spawn chance

## [1.2.2] - 2020-09-04
### Fixed
- Missing block drops

### Changed
- Torch input for living torches now an item tag

## [1.2.3] - 2020-09-04
### Fixed
- Don't subscribe to `ItemColors` event on server-side
