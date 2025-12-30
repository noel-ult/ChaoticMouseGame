import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.ArrayList;
import java.util.List;

/**
 * Chaotic Mouse Application - Java Edition
 * 
 * This application creates a chaotic and unpredictable mouse experience.
 * Features:
 * - Main thread that moves the mouse cursor randomly across the screen
 * - Flicker effect that subtly shakes the cursor when it moves quickly
 * - Multiple "fake cursors" that move independently around the real cursor
 * - Modern Swing-based control panel with enhanced UI
 */
public class ChaoticMouseApp {
    private JFrame mainFrame;
    private ControlPanel controlPanel;
    private AppState appState;
    private List<FakeCursor> fakeCursors;
    private Robot robot;
    private Thread mouseThread;
    private Thread flickerThread;
    private Dimension screenSize;
    
    // Game related fields
    private JFrame gameFrame;
    private JPanel gamePanel;
    private JPanel scorePanel;
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private JButton targetButton;
    private int score = 0;
    private int timeRemaining = 60;
    private Timer gameTimer;
    private boolean gameActive = false;
    private Random gameRandom = new Random();
    private static final int TARGET_SIZE = 50;
    private static final int GAME_DURATION = 60;

    public ChaoticMouseApp() {
        try {
            robot = new Robot();
            screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            appState = new AppState();
            fakeCursors = new ArrayList<>();
            
            // Set modern look and feel
            setModernLookAndFeel();
            
            // Create main invisible frame
            mainFrame = new JFrame("Chaotic Mouse App");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setUndecorated(true);
            mainFrame.setSize(0, 0);
            mainFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    shutdown();
                }
            });
            
            // Create control panel
            controlPanel = new ControlPanel(this);
            
            // Initialize fake cursors
            updateNumCursors(appState.numCursors);
            
            mainFrame.setVisible(false);
            
        } catch (AWTException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Failed to initialize Robot: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    private void setModernLookAndFeel() {
        try {
            // Try to set Nimbus look and feel for modern appearance
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
            // Fallback to system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void run() {
        System.out.println("Starting Chaotic Mouse. Close the Control Panel to exit.");
        
        // Start chaotic mouse movement thread
        mouseThread = new Thread(this::chaoticMouseMovement);
        mouseThread.setDaemon(true);
        mouseThread.start();
        
        // Start flicker effect thread
        flickerThread = new Thread(this::flickerEffect);
        flickerThread.setDaemon(true);
        flickerThread.start();
        
        // Start fake cursor movements
        for (FakeCursor cursor : fakeCursors) {
            startFakeCursorMovement(cursor);
        }
    }
    
    public void shutdown() {
        System.out.println("Shutting down...");
        appState.stopFlag.set(true);
        
        if (mouseThread != null) {
            try {
                mouseThread.join(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        for (FakeCursor cursor : fakeCursors) {
            cursor.dispose();
        }
        
        System.out.println("Exited.");
        System.exit(0);
    }
    
    public void updateNumCursors(int newCount) {
        SwingUtilities.invokeLater(() -> {
            // Add new cursors
            while (fakeCursors.size() < newCount) {
                FakeCursor newCursor = new FakeCursor(appState.fakeCursorShape);
                fakeCursors.add(newCursor);
                startFakeCursorMovement(newCursor);
            }
            
            // Remove excess cursors
            while (fakeCursors.size() > newCount) {
                FakeCursor cursor = fakeCursors.remove(fakeCursors.size() - 1);
                cursor.dispose();
            }
        });
    }
    
    public void updateFakeCursorShapes(String newShape) {
        SwingUtilities.invokeLater(() -> {
            for (FakeCursor cursor : fakeCursors) {
                cursor.setShape(newShape);
            }
        });
    }
    
    private void startFakeCursorMovement(FakeCursor cursor) {
        Timer timer = new Timer(new Random().nextInt(120) + 30, null);
        timer.addActionListener(e -> {
            if (appState.stopFlag.get()) {
                timer.stop();
                return;
            }
            
            if (!appState.paused) {
                Point mousePos = MouseInfo.getPointerInfo().getLocation();
                Random rand = new Random();
                int x = mousePos.x + rand.nextInt(301) - 150;
                int y = mousePos.y + rand.nextInt(301) - 150;
                cursor.moveTo(x, y);
            }
        });
        timer.start();
    }
    
    private boolean isEffectActive() {
        if (appState.paused || appState.stopFlag.get()) {
            return false;
        }
        
        // Check if control panel or any of its components have focus
        Window activeWindow = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        if (activeWindow == controlPanel) {
            return false;
        }
        
        // Check if mouse is over the control panel
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        Rectangle controlPanelBounds = controlPanel.getBounds();
        controlPanelBounds.setLocation(controlPanel.getLocationOnScreen());
        if (controlPanelBounds.contains(mouseLocation)) {
            return false;
        }
        
        // ENABLE effects during game (this makes the game challenging!)
        if (gameActive && gameFrame != null && gameFrame.isVisible()) {
            return true;
        }
        
        return true;
    }
    
    private void flickerEffect() {
        Point lastPos = null;
        
        while (!appState.stopFlag.get()) {
            try {
                if (isEffectActive() && appState.flickerEnabled) {
                    Point currentPos = MouseInfo.getPointerInfo().getLocation();
                    
                    if (lastPos != null) {
                        double dist = currentPos.distance(lastPos);
                        
                        if (dist > appState.speedThreshold) {
                            int offset = appState.flickerIntensity;
                            robot.mouseMove(currentPos.x + offset, currentPos.y);
                            Thread.sleep(10);
                            robot.mouseMove(currentPos.x - offset, currentPos.y);
                        }
                    }
                    
                    lastPos = currentPos;
                }
                
                Thread.sleep(50);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
    
    private void chaoticMouseMovement() {
        Random rand = new Random();
        
        while (!appState.stopFlag.get()) {
            try {
                if (!isEffectActive() || !appState.chaoticEnabled) {
                    Thread.sleep(100);
                    continue;
                }
                
                Point currentPos = MouseInfo.getPointerInfo().getLocation();
                int x = currentPos.x;
                int y = currentPos.y;
                
                // Occasionally jump to random location
                if (rand.nextDouble() < 0.15) {
                    x = rand.nextInt(screenSize.width + 200) - 100;
                    y = rand.nextInt(screenSize.height + 200) - 100;
                } else {
                    x = Math.max(0, Math.min(screenSize.width - 1, 
                        x + rand.nextInt(301) - 150));
                    y = Math.max(0, Math.min(screenSize.height - 1, 
                        y + rand.nextInt(301) - 150));
                }
                
                // Occasionally perform jitter motion
                if (rand.nextDouble() < 0.25) {
                    int jitterCount = rand.nextInt(16) + 10;
                    for (int i = 0; i < jitterCount && isEffectActive(); i++) {
                        int jitterX = Math.max(0, Math.min(screenSize.width - 1, 
                            x + rand.nextInt(41) - 20));
                        int jitterY = Math.max(0, Math.min(screenSize.height - 1, 
                            y + rand.nextInt(41) - 20));
                        robot.mouseMove(jitterX, jitterY);
                        Thread.sleep(5);
                    }
                    Thread.sleep(50);
                } else {
                    robot.mouseMove(x, y);
                }
                
                Thread.sleep((long)(rand.nextDouble() * 1150 + 50));
                
            } catch (InterruptedException e) {
                break;
            }
        }
    }
    
    public void startGame() {
        if (gameFrame != null && gameFrame.isVisible()) {
            gameFrame.toFront();
            return;
        }
        
        // Create game window
        gameFrame = new JFrame("🎯 CYBER COMBAT ARENA - CHAOS PROTOCOL ENGAGED!");
        gameFrame.setSize(800, 600);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setLayout(new BorderLayout());
        gameFrame.getContentPane().setBackground(new Color(10, 10, 25));
        
        // Create score panel
        createGameScorePanel();
        gameFrame.add(scorePanel, BorderLayout.NORTH);
        
        // Create game panel
        createGamePanel();
        gameFrame.add(gamePanel, BorderLayout.CENTER);
        
        gameFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (gameActive) {
                    endGame();
                }
            }
        });
        
        gameFrame.setVisible(true);
        
        // Start game immediately
        beginGame();
    }
    
    private void createGameScorePanel() {
        scorePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Futuristic gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(20, 0, 40),
                    0, getHeight(), new Color(10, 10, 25)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Glowing border
                g2d.setColor(new Color(0, 255, 255));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(0, getHeight() - 2, getWidth(), getHeight() - 2);
            }
        };
        scorePanel.setPreferredSize(new Dimension(800, 90));
        scorePanel.setLayout(new GridLayout(1, 2, 20, 0));
        scorePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        scoreLabel = new JLabel("<html><b>SCORE: 0</b></html>", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Courier New", Font.BOLD, 36));
        scoreLabel.setForeground(new Color(0, 255, 255));
        
        timerLabel = new JLabel("<html><b>TIME: 60s</b></html>", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Courier New", Font.BOLD, 36));
        timerLabel.setForeground(new Color(255, 0, 255));
        
        scorePanel.add(scoreLabel);
        scorePanel.add(timerLabel);
    }
    
    private void createGamePanel() {
        gamePanel = new JPanel();
        gamePanel.setLayout(null);
        gamePanel.setBackground(new Color(10, 10, 25));
        
        targetButton = new JButton("🎯") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Glowing effect
                g2d.setColor(new Color(255, 0, 100, 100));
                g2d.fillOval(-5, -5, getWidth() + 10, getHeight() + 10);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 0, 100),
                    0, getHeight(), new Color(255, 0, 200)
                );
                g2d.setPaint(gradient);
                g2d.fillOval(0, 0, getWidth(), getHeight());
                
                super.paintComponent(g);
            }
        };
        targetButton.setFont(new Font("Arial", Font.BOLD, 26));
        targetButton.setFocusPainted(false);
        targetButton.setBorderPainted(false);
        targetButton.setContentAreaFilled(false);
        targetButton.setOpaque(false);
        targetButton.setForeground(Color.WHITE);
        targetButton.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 3));
        targetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        targetButton.addActionListener(e -> onTargetClicked());
        
        gamePanel.add(targetButton);
    }
    
    private void beginGame() {
        score = 0;
        timeRemaining = GAME_DURATION;
        gameActive = true;
        
        scoreLabel.setText("<html><b>SCORE: 0</b></html>");
        timerLabel.setText("<html><b>TIME: " + GAME_DURATION + "s</b></html>");
        timerLabel.setForeground(new Color(255, 0, 255));
        
        // Enable chaotic effects for maximum difficulty
        appState.chaoticEnabled = true;
        appState.flickerEnabled = true;
        appState.paused = false;
        controlPanel.updateGameStatus(true);
        
        moveTargetToRandomPosition();
        
        gameTimer = new Timer(1000, e -> updateGameTimer());
        gameTimer.start();
    }
    
    private void onTargetClicked() {
        if (!gameActive) return;
        
        score++;
        scoreLabel.setText("<html><b>SCORE: " + score + "</b></html>");
        moveTargetToRandomPosition();
        
        // Visual feedback
        targetButton.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 0), 5));
        Timer flashTimer = new Timer(100, e -> {
            targetButton.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 3));
            ((Timer) e.getSource()).stop();
        });
        flashTimer.setRepeats(false);
        flashTimer.start();
    }
    
    private void moveTargetToRandomPosition() {
        int maxX = gamePanel.getWidth() - TARGET_SIZE - 20;
        int maxY = gamePanel.getHeight() - TARGET_SIZE - 20;
        
        int x = gameRandom.nextInt(Math.max(1, maxX)) + 10;
        int y = gameRandom.nextInt(Math.max(1, maxY)) + 10;
        
        targetButton.setBounds(x, y, TARGET_SIZE, TARGET_SIZE);
    }
    
    private void updateGameTimer() {
        timeRemaining--;
        timerLabel.setText("<html><b>TIME: " + timeRemaining + "s</b></html>");
        
        if (timeRemaining <= 10) {
            timerLabel.setForeground(new Color(255, 0, 100));
        } else if (timeRemaining <= 30) {
            timerLabel.setForeground(new Color(255, 200, 0));
        }
        
        if (timeRemaining <= 0) {
            endGame();
        }
    }
    
    private void endGame() {
        gameActive = false;
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        controlPanel.updateGameStatus(false);
        
        String message = String.format(
            "=== MISSION COMPLETE ===\n\n" +
            ">> FINAL SCORE: %d HITS\n" +
            ">> PRECISION: %.2f hits/sec\n\n" +
            "// CHAOS PROTOCOL SURVIVED! //",
            score,
            score / (double) GAME_DURATION
        );
        
        JOptionPane.showMessageDialog(
            gameFrame,
            message,
            "🏆 CYBER COMBAT COMPLETE",
            JOptionPane.INFORMATION_MESSAGE
        );
        
        if (gameFrame != null) {
            gameFrame.dispose();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChaoticMouseApp app = new ChaoticMouseApp();
            app.run();
        });
    }
    
    // Inner class for application state
    class AppState {
        volatile boolean paused = false;
        volatile boolean chaoticEnabled = true;
        volatile boolean flickerEnabled = true;
        volatile int flickerIntensity = 5;
        volatile int speedThreshold = 15;
        volatile String fakeCursorShape = "dot";
        volatile int numCursors = 5;
        AtomicBoolean stopFlag = new AtomicBoolean(false);
    }
    
    public AppState getAppState() {
        return appState;
    }
    
    public ControlPanel getControlPanel() {
        return controlPanel;
    }
}
