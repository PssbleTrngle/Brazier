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
.

## [1.2.4] - 2020-09-04
### Fixed
- Server error preventing players from joining.

## [1.3.0] - 2020-11-20
### Added
- Cursed ash: placeable powder to bypass braziers, allowing mob farms
- Warped netherwarts

### Modified
- Brazier can now be configured to only prevent spawns below
- Reduced indication particles 

### Fixed
- Wrong particle offset on living torches
- Slimes and magma cubes spawns inside brazier.

## [1.3.1] - 2020-11-25
### Added
- Moved injected loot tables into data folder

### Fixed
- Correct warped wart loot pool
- Config option for jungle loot no longer ignored