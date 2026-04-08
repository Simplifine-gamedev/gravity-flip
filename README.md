# Gravity Flip

A Minecraft Fabric mod that adds a Gravity Flipper item. Right-click to flip gravity upside down so you float toward the ceiling. Right-click again to return to normal. The slow falling effect protects you from fall damage during transitions.

## Features

- **Gravity Flipper Item**: A single-use tool that toggles inverted gravity
- **Ceiling Walking**: Levitation effect pulls you upward when gravity is flipped
- **Safe Transitions**: Slow falling protects you when returning to normal gravity

## Requirements

- Minecraft 1.21.1
- Fabric Loader 0.16+
- Fabric API

## Installation

1. Install Fabric for Minecraft 1.21.1
2. Download the JAR from `build/libs/`
3. Place it in your `.minecraft/mods/` folder

## Usage

1. Give yourself the item: `/give @p gravity-flip:gravity_flipper`
2. Right-click to flip gravity upward
3. Right-click again to restore normal gravity

## Building

```
gradle build
```

The compiled JAR will be in `build/libs/`.
