import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * Click Speed Game
 * Click the target as many times as you can in 60 seconds!
 */
public class ClickSpeedGame extends JFrame {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int TARGET_SIZE = 50;
    private static final int GAME_DURATION = 60; // seconds
    
    private JPanel gamePanel;
    private JPanel scorePanel;
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private JButton targetButton;
    private JButton startButton;
    
    private int score = 0;
    private int timeRemaining = GAME_DURATION;
    private Timer gameTimer;
    private Random random;
    private boolean gameActive = false;
    
    public ClickSpeedGame() {
        random = new Random();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("⚡ CYBER COMBAT ARENA - CLICK SPEED PROTOCOL");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(10, 10, 25));
        
        // Main layout
        setLayout(new BorderLayout());
        
        // Score panel at the top
        createScorePanel();
        
        // Game panel in the center
        createGamePanel();
        
        setVisible(true);
    }
    
    private void createScorePanel() {
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
                
                // Glowing border effect
                g2d.setColor(new Color(0, 255, 255));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(0, getHeight() - 2, getWidth(), getHeight() - 2);
            }
        };
        scorePanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 90));
        scorePanel.setLayout(new GridLayout(1, 2, 20, 0));
        scorePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Score label
        scoreLabel = new JLabel("<html><b>SCORE: 0</b></html>", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Courier New", Font.BOLD, 36));
        scoreLabel.setForeground(new Color(0, 255, 255));
        
        // Timer label
        timerLabel = new JLabel("<html><b>TIME: 60s</b></html>", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Courier New", Font.BOLD, 36));
        timerLabel.setForeground(new Color(255, 0, 255));
        
        scorePanel.add(scoreLabel);
        scorePanel.add(timerLabel);
        
        add(scorePanel, BorderLayout.NORTH);
    }
    
    private void createGamePanel() {
        gamePanel = new JPanel();
        gamePanel.setLayout(null); // Absolute positioning for target
        gamePanel.setBackground(new Color(10, 10, 25));
        
        // Create target button with glow effect
        targetButton = new JButton("🎯") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Glowing outer effect
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
        targetButton.setVisible(false);
        targetButton.addActionListener(e -> onTargetClicked());
        
        gamePanel.add(targetButton);
        
        // Create start button with futuristic styling
        startButton = new JButton("<html><center>⚡ INITIATE COMBAT ⚡</center></html>") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 0, 100),
                    0, getHeight(), new Color(138, 43, 226)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                super.paintComponent(g);
            }
        };
        startButton.setFont(new Font("Courier New", Font.BOLD, 22));
        startButton.setForeground(Color.WHITE);
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 255, 255), 3),
            BorderFactory.createEmptyBorder(15, 30, 15, 30)
        ));
        startButton.setBounds(WINDOW_WIDTH / 2 - 150, WINDOW_HEIGHT / 2 - 40, 300, 80);
        startButton.addActionListener(e -> startGame());
        
        gamePanel.add(startButton);
        
        add(gamePanel, BorderLayout.CENTER);
    }
    
    private void startGame() {
        // Reset game state
        score = 0;
        timeRemaining = GAME_DURATION;
        gameActive = true;
        
        // Update UI
        scoreLabel.setText("<html><b>SCORE: 0</b></html>");
        timerLabel.setText("<html><b>TIME: " + GAME_DURATION + "s</b></html>");
        timerLabel.setForeground(new Color(255, 0, 255));
        startButton.setVisible(false);
        targetButton.setVisible(true);
        
        // Position target randomly
        moveTargetToRandomPosition();
        
        // Start game timer
        gameTimer = new Timer(1000, e -> updateTimer());
        gameTimer.start();
    }
    
    private void onTargetClicked() {
        if (!gameActive) return;
        
        score++;
        scoreLabel.setText("<html><b>SCORE: " + score + "</b></html>");
        
        // Move target to new random position
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
        
        int x = random.nextInt(Math.max(1, maxX)) + 10;
        int y = random.nextInt(Math.max(1, maxY)) + 10;
        
        targetButton.setBounds(x, y, TARGET_SIZE, TARGET_SIZE);
    }
    
    private void updateTimer() {
        timeRemaining--;
        timerLabel.setText("<html><b>TIME: " + timeRemaining + "s</b></html>");
        
        // Change timer color as time runs out
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
        gameTimer.stop();
        targetButton.setVisible(false);
        
        // Show game over message
        String message = String.format(
            "=== MISSION COMPLETE ===\n\n" +
            ">> FINAL SCORE: %d HITS\n" +
            ">> PRECISION: %.2f hits/sec\n\n" +
            "// PROTOCOL EXECUTED! //",
            score,
            score / (double) GAME_DURATION
        );
        
        JOptionPane.showMessageDialog(
            this,
            message,
            "🏆 CYBER COMBAT COMPLETE",
            JOptionPane.INFORMATION_MESSAGE
        );
        
        // Show start button again
        startButton.setText("<html><center>⚡ RETRY MISSION ⚡</center></html>");
        startButton.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set modern look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ClickSpeedGame();
        });
    }
}
