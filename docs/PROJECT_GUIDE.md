# Chaotic Mouse Java guide

A Java Swing desktop cursor-effects application with a click-speed game. It uses desktop windowing and pointer-control APIs, so it requires a graphical session.

## Build and run

Compile all four source files with an installed JDK:

```sh
javac ChaoticMouseApp.java ControlPanel.java FakeCursor.java ClickSpeedGame.java
java ChaoticMouseApp
```

The checked-in `.class` files are build artifacts; compile the source for your JDK rather than depending on those binaries. No Maven, Gradle, or external Java library is required by the source.

## Module responsibilities

- `ChaoticMouseApp.java`: application lifecycle, shared state, and cursor movement.
- `ControlPanel.java`: effect toggles, intensity/threshold controls, and game launch.
- `FakeCursor.java`: overlay cursor windows and rendering.
- `ClickSpeedGame.java`: interactive clicking challenge.

Use Pause All Effects to suspend pointer effects and the application close controls to exit. The desktop may require input-control/accessibility permissions. Headless sessions cannot run Swing windows, and desktop environments may restrict pointer control or overlay behavior.

## Checks

Check pause/resume, toggles, cursor count/shape, game start/end, and process exit manually in a dedicated desktop session. No automated tests are included. `FUTURISTIC_UI_CHANGES.md` records presentation changes; runtime behavior comes from the four Java sources.

## More documentation

- [Source, command, and configuration reference](REFERENCE.md)
- [Contributing](../CONTRIBUTING.md)
