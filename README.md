# Foot Loot Mod (Fabric 1.20.1)

A simple mod that makes the block under your feet spawn its own item without breaking. Drops only occur when the player walks or jumps on the block.

## Features

- Duplicate drop: the block stays intact but spawns its item
- Drops only while walking or jumping; idle means no drops
- No Fabric API required (standalone)
- Safe: ignores bedrock and spectator players

## Requirements

- Java 17 (JDK)
- Fabric Loader >= 0.16.5 for Minecraft 1.20.1

## Build (Developer)

On Windows:

```
.\gradlew.bat clean build
```

Build output:

```
build\libs\foot-loot-mc-1.0.0.jar
```

## Installation (TLauncher)

- Select and run a Fabric 1.20.1 profile
- Copy `build\libs\foot-loot-mc-1.0.0.jar` into the `mods` folder
- Enter a world; a chat message confirms the mod is active

## Usage

- Stand on a block, then walk or jump → the block’s item will drop
- Example: Grass Block drops Grass Block (not dirt)

## Troubleshooting

- Mod not listed: ensure Fabric 1.20.1 profile and the jar exists in `mods`
- “Incompatible mods”: ensure you’re using the standalone jar without `fabric-api` dependency
- No drops: you must move or jump; spectator players are ignored

## Quick Configuration

- Drop interval: throttled every 5 ticks in `ServerTickMixin`
- Movement sensitivity: velocity/position thresholds in `ServerTickMixin`

Main implementation:

- `src/main/java/com/sakunoki/mixin/ServerTickMixin.java`
- `src/main/java/com/sakunoki/FootLootMod.java`

## License

CC0-1.0 — free to use and learn from.
