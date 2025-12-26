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
        setTitle("Click Speed Game");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main layout
        setLayout(new BorderLayout());
        
        // Score panel at the top
        createScorePanel();
        
        // Game panel in the center
        createGamePanel();
        
        setVisible(true);
    }
    
    private void createScorePanel() {
        scorePanel = new JPanel();
        scorePanel.setBackground(new Color(45, 45, 45));
        scorePanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 80));
        scorePanel.setLayout(new GridLayout(1, 2, 20, 0));
        scorePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Score label
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 32));
        scoreLabel.setForeground(Color.WHITE);
        
        // Timer label
        timerLabel = new JLabel("Time: 60s", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 32));
        timerLabel.setForeground(new Color(0, 255, 100));
        
        scorePanel.add(scoreLabel);
        scorePanel.add(timerLabel);
        
        add(scorePanel, BorderLayout.NORTH);
    }
    
    private void createGamePanel() {
        gamePanel = new JPanel();
        gamePanel.setLayout(null); // Absolute positioning for target
        gamePanel.setBackground(new Color(30, 30, 30));
        
        // Create target button
        targetButton = new JButton("🎯");
        targetButton.setFont(new Font("Arial", Font.BOLD, 24));
        targetButton.setFocusPainted(false);
        targetButton.setBorderPainted(false);
        targetButton.setContentAreaFilled(false);
        targetButton.setOpaque(true);
        targetButton.setBackground(new Color(255, 50, 50));
        targetButton.setForeground(Color.WHITE);
        targetButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        targetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        targetButton.setVisible(false);
        targetButton.addActionListener(e -> onTargetClicked());
        
        gamePanel.add(targetButton);
        
        // Create start button
        startButton = new JButton("START GAME");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setBackground(new Color(0, 200, 0));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startButton.setBounds(WINDOW_WIDTH / 2 - 100, WINDOW_HEIGHT / 2 - 30, 200, 60);
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
        scoreLabel.setText("Score: 0");
        timerLabel.setText("Time: " + GAME_DURATION + "s");
        timerLabel.setForeground(new Color(0, 255, 100));
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
        scoreLabel.setText("Score: " + score);
        
        // Move target to new random position
        moveTargetToRandomPosition();
        
        // Visual feedback
        targetButton.setBackground(new Color(255, 150, 0));
        Timer flashTimer = new Timer(100, e -> {
            targetButton.setBackground(new Color(255, 50, 50));
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
        timerLabel.setText("Time: " + timeRemaining + "s");
        
        // Change timer color as time runs out
        if (timeRemaining <= 10) {
            timerLabel.setForeground(new Color(255, 50, 50));
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
            "Game Over!\n\nFinal Score: %d clicks\nClicks per second: %.2f",
            score,
            score / (double) GAME_DURATION
        );
        
        JOptionPane.showMessageDialog(
            this,
            message,
            "Game Over",
            JOptionPane.INFORMATION_MESSAGE
        );
        
        // Show start button again
        startButton.setText("PLAY AGAIN");
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
