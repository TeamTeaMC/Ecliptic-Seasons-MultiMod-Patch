# [Ecliptic Seasons: MultiMod Patch](https://www.curseforge.com/minecraft/mc-mods/ecliptic-seasons-multimod-patch)

![icon](icon.png)

通过一组兼容性补丁，使节气模组能够更好地与其他模组协同工作。

A collection of mixins for mod compatibility with Ecliptic Seasons.

### LICENSE 许可证

* For code: BSD-3
* 对于代码：BSD-3
* For resources: CC BY-NC-SA 4.0
* 对于资源文件：署名-非商业性使用-相同方式共享 4.0 国际

| Mod                  | 1.21.1 | 1.20.1 | Patched Content                                                                                                           |
|----------------------|--------|--------|---------------------------------------------------------------------------------------------------------------------------|
| *In Control*         | ✔️     | ✔️     | Overrides season and weather (requires BlockPos support), and adds surface biome definitions and valid solar term checks. |
| *Pretty Rain*        | ✔️     | ✔️     | Correctly applies biome acquisition and level-dependent precipitation; desert rain is modified into sandstorms.           |
| *Presence Footsteps* | ✔️     | ✔️     | Stepping on snowy blocks now uses the snow layer sound effect instead.                                                    |
| *Snowy Spirit*       | ✔️     | ✔️     | Enables skiing on snowy blocks during wintertime.                                                                         |
| *JourneyMap*         | ✔️     | ✔️     | Displays snow-covered blocks properly on the map.                                                                         |
| *Cold Sweat*         | ✔️     | ✔️     | Adds seasonal temperature variation and fixes rainfall calculation.                                                       |
| *Dynamic Trees*      | ✔️     | ✔️     | Adds a seasonal provider; tree growth now follows seasonal growth cycles.                                                 |
| *Haunted Harvest*    | ✔️     | ✔️     | Enables Halloween and pumpkin zombie seasonal events.                                                                     |
| *Minecolonies*       | ✔️     | ✔️     | Dynamically adjusts citizens' sleep time according to the season.                                                         |
| *Ambient Sounds*     | ❌️     | ✔️     | Adapts biome temperature, thunder, and snowfall calculations.                                                             |
| *Touhou Little Maid* | ✔️     | ✔️     | Allows the maid to sweep snowy blocks.                                                                                    |
| *Diagonal Blocks*    | ✔️     | ✔️     | Limited snowy block definition support for diagonal walls and fences.                                                     |
| *Simple Clouds*      | ❓︎     | ❓︎     | Adapts biome detection and precipitation data.                                                                            |

### Examples for InControl

```json
[
  {
    "mob": [
      "minecraft:bat",
      "minecraft:cod",
      "minecraft:salmon",
      "minecraft:tropical_fish"
    ],
    "valid_terms": [
      // season or solar term name
      "spring",
      // add '-' to mark start term and end term
      "light_snow-winter_solstice"
    ],
    // check global season, or agro climatic season with position provided
    "spring": true,
    // a check for surface biome to ignore small biomes like river, support tag or biome id
    "surface_biomes": [
      "#minecraft:is_forest"
    ],
    // support checks for biome weathers with position, if without position the check would be igonored.
    "weather": "rain",
    "mincount": {
      "mob": [
        "minecraft:bat",
        "minecraft:cod",
        "minecraft:salmon",
        "minecraft:tropical_fish"
      ],
      "amount": 5
    },
    "result": "deny"
  }
]
```

