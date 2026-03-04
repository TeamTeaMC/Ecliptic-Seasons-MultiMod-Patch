**1.0**

* Resolve several compatibility issues now handled by **Serene Seasons API Stub (Ecliptic Seasons Bridge)**.
  To avoid conflicts introduced by other modules, installing this module is recommended.

**Changes**

* Remove multiple module-specific patches and related configs:

    * AmbientSounds
    * Haunted Harvest
    * Snowy Spirit
    * Subtle Effects
    * Cold Sweat
    * Dynamic Trees
* Delete corresponding module patch classes and hooks.
* Simplify several mixins and reduce conditional checks.
* `MixinWorldHelper` now always uses **EclipticSeasonsApi** (removed conditional `CS.Config` logic).
* Remove `SS.Config` gating in `MixinSnowySpiritGroundStatus`; snow checks now directly use the API.
* Bump `mod_version` in `gradle.properties` from **0.27.0 → 1.0**.

This update removes several optional module integrations and streamlines mixin registrations to match the new simplified patch structure.
