# Chaotic Mouse Application - Java Edition

<!-- documentation-navigation -->
[Developer guide](docs/PROJECT_GUIDE.md) · [Source reference](docs/REFERENCE.md) · [Contributing](CONTRIBUTING.md)
<!-- /documentation-navigation -->


A fun and chaotic mouse experience application converted from Python to Java with enhanced GUI design.

## 📋 Overview

This application creates unpredictable mouse movements, fake cursor effects, and flickering animations that you can control through a modern, stylish interface.

## ✨ New Features & Improvements

### Enhanced User Interface
- **Modern Dark Theme**: Sleek dark color scheme with accent colors
- **Emoji Icons**: Visual indicators for better user experience
- **Smooth Gradients**: Custom slider designs with filled progress indication
- **Custom Checkboxes**: Modern checkbox styling with smooth animations
- **Glow Effects**: Fake cursors now have radial glow effects
- **Better Layout**: Organized sections with clear visual hierarchy
- **Responsive Design**: Scrollable interface for smaller screens

### Technical Improvements
- Converted from Python/Tkinter to Java/Swing
- Multi-threaded architecture for smooth performance
- Atomic operations for thread-safe state management
- Enhanced cursor shapes with gradient fills and glow effects
- Better resource cleanup and memory management
- Cross-platform compatibility (Windows, macOS, Linux)

## 🎨 Features

1. **Chaotic Mouse Movement**: Random cursor movements across the screen
2. **Flicker Effect**: Subtle cursor shake when moving quickly
3. **Fake Cursors**: Multiple ghost cursors that follow your mouse (1-15 cursors)
4. **Customizable Shapes**: Choose between Dot, Square, or Cross cursor shapes
5. **🎯 Click Speed Game**: Test your clicking skills while chaos is active!
6. **Real-time Controls**: 
   - Pause all effects
   - Toggle individual effects
   - Adjust flicker intensity (1-20)
   - Control speed threshold (1-50)
   - Change number of fake cursors (1-15)

## 🚀 Compilation & Running

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Terminal/Command Prompt access

### Compile the Project

Open a terminal in the project directory and run:

```bash
javac ChaoticMouseApp.java ControlPanel.java FakeCursor.java
```

### Run the Application

```bash
java ChaoticMouseApp
```

### Alternative: One-Line Compile and Run

```bash
javac ChaoticMouseApp.java ControlPanel.java FakeCursor.java && java ChaoticMouseApp
```

## 📦 Project Structure

```
December Project/
├── ChaoticMouseApp.java    # Main application with game integration
├── ControlPanel.java        # Modern control panel with game button
├── FakeCursor.java          # Transparent window for fake cursor display
├── ClickSpeedGame.java      # Standalone game (not used in integrated version)
├── Final.py                 # Original Python version
└── README.md                # This file
```

## 🎮 Usage Instructions

### Basic Controls

1. **Launch the application** - The control panel will appear
2. **Configure settings** - Use sliders and checkboxes to customize behavior
3. **Pause effects** - Check "Pause All Effects" when you need control
4. **Change cursor shape** - Select your preferred fake cursor shape
5. **Close application** - Simply close the Control Panel window

### 🎯 How to Play the Click Speed Game

1. **Locate the Game Section** - In the Control Panel, scroll down to find the **"🎮 Challenge Mode"** section
2. **Click "🎯 Start Click Speed Game"** - A new game window will open
3. **Game Rules**:
   - You have **60 seconds** to click as many targets as possible
   - The target (🎯) moves to a random position after each click
   - **Chaotic mouse effects remain active** - making it super challenging!
   - Your score and remaining time are displayed at the top
4. **Game Status** - Watch the status indicator below the button:
   - "⚠️ Ready" - Game not started
   - "✅ ACTIVE" - Game is running
5. **View Results** - After 60 seconds, see your final score and clicks per second

### Tips
- Effects automatically pause when the Control Panel has focus
- Start with fewer cursors (3-5) and increase gradually
- Higher flicker intensity creates more dramatic effects
- Lower speed threshold means flicker activates more easily
- **For the game**: Try to anticipate where the target will appear next!
- **Challenge yourself**: Increase fake cursors and flicker intensity before starting the game

## 🎨 Color Scheme

- **Background**: Dark charcoal (#2D2D30)
- **Panels**: Darker charcoal (#252526)
- **Text**: Light gray (#DCDCDC)
- **Accent**: Microsoft blue (#007ACC)
- **Hover**: Medium gray (#3E3E40)
- **Fake Cursors**: Red with glow effects

## 🔧 System Requirements

- **OS**: Windows, macOS, or Linux
- **Java**: JDK 8 or higher
- **RAM**: 256 MB minimum
- **Display**: Any resolution (tested on 1920x1080)

## ⚠️ Important Notes

- The application requires permission to control the mouse
- Effects may be intense - start with lower settings
- Not recommended for users with photosensitivity
- Close the Control Panel to exit completely

## 🐛 Troubleshooting

**Application won't start:**
- Ensure Java is properly installed: `java -version`
- Check all three .java files are in the same directory
- Recompile all files together

**Mouse not moving:**
- Check "Pause All Effects" is unchecked
- Verify "Chaotic Movement" is enabled
- Ensure Control Panel doesn't have focus

**Fake cursors not visible:**
- Increase the number of cursors slider
- Check if they're behind other windows
- Try changing cursor shape

## 📝 Differences from Python Version

| Feature | Python (Original) | Java (Enhanced) |
|---------|------------------|-----------------|
| GUI Framework | Tkinter | Swing with custom styling |
| Look & Feel | Basic | Modern dark theme |
| Color Scheme | Default | Custom dark palette |
| Checkboxes | Standard | Custom drawn with icons |
| Sliders | Basic | Gradient-filled custom design |
| Icons | None | Emoji throughout |
| Fake Cursors | Simple shapes | Gradients + glow effects |
| Threading | threading module | Java Thread API |
| Mouse Control | pyautogui | java.awt.Robot |

## 🎯 Future Enhancement Ideas

- Add color customization for fake cursors
- Implement cursor trails effect
- Add sound effects (optional)
- Create preset configurations
- Add keyboard shortcuts
- Implement cursor patterns (circle, spiral, etc.)

## 👨‍💻 Developer Notes

The application uses Java's `Robot` class for mouse control and Swing for the GUI. Thread safety is managed through volatile variables and atomic operations. The UI uses custom painting for enhanced visual effects.

## 📄 License

This project is for educational and entertainment purposes.

---

**Enjoy the chaos! 🎉**

<!-- developer-handbook-20261028 -->
[Developer guide](docs/PROJECT_GUIDE.md) · [Source reference](docs/REFERENCE.md) · [Verification and operations](docs/OPERATIONS.md)
<!-- /developer-handbook-20261028 -->
