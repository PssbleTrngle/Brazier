# Changelog
All notable changes to this project will be documented in this file.

# 1.16.1

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

# 1.16.3.

## [2.0.0] - 2020-11-29
Initial 1.16.3 release

## [2.0.1] - 2020-11-30
### Fixed
- Mod now requires correct forge & mc versions
- Should also work on 1.16.4

## [2.0.2] - 2020-12-02
### Fixed
- Wrong server config descriptions
  .

## [2.1.0] - 2020-12-02
### Changed
- Version range to allow usage with 1.16.1 & 1.16.2 (experimental).

## [2.1.1] - 2020-12-02
### Changed
- FML Loader version to allow 32.*.

## [2.1.2] - 2020-12-03
### Fixed
- Brazier runes not rendering for more than 9 blocks of the side
- Rendering code being a mess.

## [2.2.0] - 2020-12-03
### Added
- Living Lantern.

## [2.3.0] - 2021-01-05
### Added
- German Translation
- Korean Translation (thanks to @othuntgithub).

## [3.0.0] - 2021-03-23
### Changed
- Rebuild using the archtictury api
- First fabric version
- modified CI/CD pipeline.

## [3.0.1] - 2021-03-25
### Fixed
- Correct java build target version.

## [3.0.2] - 2021-03-26
### Fixed
- Mixin refmaps being generated.

## [3.0.3] - 2021-03-26
### Changed
- Spawn Powder recipe now accepts warped warts from Nether Extension
- Lantern recipe accepts iron_nugget tag from forge/fabric
- Recipe advancements.

## [3.0.4] - 2021-03-30
### Fixed
- Server not starting due to particle client code loading.

## [3.0.5] - 2021-04-26
### Fixed
- Updated architectury api to fix texture stitch bug.

## [3.0.6] - 2021-05-21
### Fixed
- Fabric crazed floating (#72)
- JEI Warning when trying to hide an empty list of items
### Added
- LitOnBrazier JEI Category

## [3.0.7] -  2021-05-31
### Fixed
- Missing texture bug #69
### Changed
- Cursed ash sound

## [3.0.8] - 2021-06-01
### Fixed
- Removed dev-code crashing servers

## [3.0.9] - 2021-06-15
### Fixed
- Strays ignoring braziers due to some other mod-
- Brazier activated sound played when only changing the height