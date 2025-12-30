import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

/**
 * Modern Control Panel with enhanced UI design
 */
public class ControlPanel extends JFrame {
    private ChaoticMouseApp app;
    private JCheckBox pauseCheckbox;
    private JCheckBox chaoticCheckbox;
    private JCheckBox flickerCheckbox;
    private JSlider numCursorsSlider;
    private JSlider flickerIntensitySlider;
    private JSlider speedThresholdSlider;
    private ButtonGroup shapeButtonGroup;
    private JLabel gameStatusLabel;
    
    // Futuristic Cyberpunk Color Scheme
    private static final Color BACKGROUND_COLOR = new Color(10, 10, 25);
    private static final Color PANEL_COLOR = new Color(15, 15, 35);
    private static final Color TEXT_COLOR = new Color(0, 255, 255);
    private static final Color ACCENT_COLOR = new Color(255, 0, 255);
    private static final Color NEON_CYAN = new Color(0, 255, 255);
    private static final Color NEON_PINK = new Color(255, 0, 255);
    private static final Color NEON_PURPLE = new Color(138, 43, 226);
    private static final Color GLOW_COLOR = new Color(0, 255, 255, 80);
    private static final Color HOVER_COLOR = new Color(30, 30, 60);
    
    public ControlPanel(ChaoticMouseApp app) {
        this.app = app;
        
        setTitle("⚡ CYBER CHAOS CONTROL v2.0");
        setSize(420, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setAlwaysOnTop(true);
        
        // Modern dark theme
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                app.shutdown();
            }
        });
        
        createUI();
        setVisible(true);
    }
    
    private void createUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main control panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Add control sections
        mainPanel.add(createMasterControlPanel());
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(createEffectsPanel());
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(createSlidersPanel());
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(createGamePanel());
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(createFooterPanel());
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel() {
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
                g2d.setColor(NEON_CYAN);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(0, getHeight() - 2, getWidth(), getHeight() - 2);
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("<html><b>⚡ CYBER CHAOS CONTROL ⚡</b></html>");
        titleLabel.setFont(new Font("Courier New", Font.BOLD, 22));
        titleLabel.setForeground(NEON_CYAN);
        
        JLabel subtitleLabel = new JLabel("<html><i>// NEURAL INTERFACE v2.0 //</i></html>");
        subtitleLabel.setFont(new Font("Courier New", Font.PLAIN, 11));
        subtitleLabel.setForeground(NEON_PINK);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);
        
        panel.add(textPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createMasterControlPanel() {
        JPanel panel = createStyledPanel("⚙️ SYSTEM CONTROL");
        
        pauseCheckbox = createStyledCheckbox("⏸️ SUSPEND ALL OPERATIONS");
        pauseCheckbox.addActionListener(e -> {
            app.getAppState().paused = pauseCheckbox.isSelected();
        });
        
        panel.add(pauseCheckbox);
        
        return panel;
    }
    
    private JPanel createEffectsPanel() {
        JPanel panel = createStyledPanel("⚡ NEURAL MODULES");
        
        chaoticCheckbox = createStyledCheckbox("🌀 CHAOS ENGINE ACTIVE");
        chaoticCheckbox.setSelected(true);
        chaoticCheckbox.addActionListener(e -> {
            app.getAppState().chaoticEnabled = chaoticCheckbox.isSelected();
        });
        
        flickerCheckbox = createStyledCheckbox("⚡ QUANTUM FLICKER ENABLED");
        flickerCheckbox.setSelected(true);
        flickerCheckbox.addActionListener(e -> {
            app.getAppState().flickerEnabled = flickerCheckbox.isSelected();
        });
        
        panel.add(chaoticCheckbox);
        panel.add(Box.createVerticalStrut(10));
        panel.add(flickerCheckbox);
        
        return panel;
    }
    
    private JPanel createSlidersPanel() {
        JPanel panel = createStyledPanel("🎚️ SYSTEM PARAMETERS");
        
        // Number of cursors
        panel.add(createSliderSection("👻 GHOST ENTITIES", 1, 15, 5, e -> {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                int value = source.getValue();
                app.getAppState().numCursors = value;
                app.updateNumCursors(value);
            }
        }));
        
        panel.add(Box.createVerticalStrut(15));
        
        // Flicker intensity
        panel.add(createSliderSection("💥 QUANTUM FLUX", 1, 20, 5, e -> {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                app.getAppState().flickerIntensity = source.getValue();
            }
        }));
        
        panel.add(Box.createVerticalStrut(15));
        
        // Speed threshold
        panel.add(createSliderSection("🚀 VELOCITY TRIGGER", 1, 50, 15, e -> {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                app.getAppState().speedThreshold = source.getValue();
            }
        }));
        
        return panel;
    }
    
    private JPanel createSliderSection(String label, int min, int max, int initial, 
                                       ChangeListener listener) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PANEL_COLOR);
        
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setBackground(PANEL_COLOR);
        
        JLabel nameLabel = new JLabel(label);
        nameLabel.setFont(new Font("Courier New", Font.BOLD, 12));
        nameLabel.setForeground(NEON_CYAN);
        
        JLabel valueLabel = new JLabel(String.valueOf(initial));
        valueLabel.setFont(new Font("Courier New", Font.BOLD, 13));
        valueLabel.setForeground(NEON_PINK);
        
        labelPanel.add(nameLabel, BorderLayout.WEST);
        labelPanel.add(valueLabel, BorderLayout.EAST);
        
        JSlider slider = new JSlider(min, max, initial);
        slider.setBackground(PANEL_COLOR);
        slider.setForeground(ACCENT_COLOR);
        slider.setPaintTicks(true);
        slider.setPaintLabels(false);
        slider.setMajorTickSpacing((max - min) / 4);
        
        // Style the slider
        slider.setUI(new javax.swing.plaf.basic.BasicSliderUI(slider) {
            @Override
            public void paintTrack(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                
                Rectangle trackBounds = trackRect;
                g2d.setColor(BACKGROUND_COLOR);
                g2d.fillRoundRect(trackBounds.x, trackBounds.y + trackBounds.height / 2 - 3,
                                 trackBounds.width, 6, 6, 6);
                
                // Paint filled portion with glow
                int filledWidth = (int)((slider.getValue() - slider.getMinimum()) / 
                                       (double)(slider.getMaximum() - slider.getMinimum()) * 
                                       trackBounds.width);
                
                // Glow effect
                g2d.setColor(GLOW_COLOR);
                g2d.fillRoundRect(trackBounds.x, trackBounds.y + trackBounds.height / 2 - 5,
                                 filledWidth, 10, 8, 8);
                
                // Main bar
                g2d.setColor(NEON_CYAN);
                g2d.fillRoundRect(trackBounds.x, trackBounds.y + trackBounds.height / 2 - 3,
                                 filledWidth, 6, 6, 6);
            }
        });
        
        slider.addChangeListener(e -> {
            valueLabel.setText(String.valueOf(slider.getValue()));
            listener.stateChanged(e);
        });
        
        panel.add(labelPanel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(slider);
        
        return panel;
    }
    
    private JPanel createShapePanel() {
        JPanel panel = createStyledPanel("🎨 Cursor Shape");
        
        shapeButtonGroup = new ButtonGroup();
        
        String[] shapes = {"⚫ Dot", "⬛ Square", "✖️ Cross"};
        String[] shapeValues = {"dot", "square", "cross"};
        
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        radioPanel.setBackground(PANEL_COLOR);
        
        for (int i = 0; i < shapes.length; i++) {
            final String shapeValue = shapeValues[i];
            JRadioButton radio = createStyledRadioButton(shapes[i]);
            
            if (i == 0) radio.setSelected(true);
            
            radio.addActionListener(e -> {
                app.getAppState().fakeCursorShape = shapeValue;
                app.updateFakeCursorShapes(shapeValue);
            });
            
            shapeButtonGroup.add(radio);
            radioPanel.add(radio);
        }
        
        panel.add(radioPanel);
        
        return panel;
    }
    
    private JPanel createGamePanel() {
        JPanel panel = createStyledPanel("🎮 CHAOS ARENA");
        
        JButton gameButton = new JButton("<html><center>🎯 INITIATE COMBAT MODE 🎯</center></html>") {
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
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                super.paintComponent(g);
            }
        };
        gameButton.setFont(new Font("Courier New", Font.BOLD, 13));
        gameButton.setForeground(Color.WHITE);
        gameButton.setOpaque(false);
        gameButton.setContentAreaFilled(false);
        gameButton.setFocusPainted(false);
        gameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gameButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(NEON_PINK, 2),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        gameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        gameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                gameButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(NEON_CYAN, 3),
                    BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                gameButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(NEON_PINK, 2),
                    BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
            }
        });
        
        gameButton.addActionListener(e -> app.startGame());
        
        gameStatusLabel = new JLabel("⚠️ STATUS: STANDBY");
        gameStatusLabel.setFont(new Font("Courier New", Font.BOLD, 11));
        gameStatusLabel.setForeground(new Color(100, 100, 150));
        gameStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel infoLabel = new JLabel("<html><center>// SURVIVE THE CHAOS PROTOCOL //</center></html>");
        infoLabel.setFont(new Font("Courier New", Font.ITALIC, 10));
        infoLabel.setForeground(new Color(150, 150, 200));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(gameButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(gameStatusLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(infoLabel);
        
        return panel;
    }
    
    public void updateGameStatus(boolean active) {
        SwingUtilities.invokeLater(() -> {
            if (active) {
                gameStatusLabel.setText("✅ STATUS: COMBAT ENGAGED!");
                gameStatusLabel.setForeground(NEON_CYAN);
            } else {
                gameStatusLabel.setText("⚠️ STATUS: STANDBY");
                gameStatusLabel.setForeground(new Color(100, 100, 150));
            }
        });
    }
    
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        
        JLabel infoLabel = new JLabel("<html><center><i>// NEURAL SUSPENSION ON WINDOW FOCUS //<br>" +
                                      "// TERMINATE: CLOSE CONTROL INTERFACE //</i></center></html>");
        infoLabel.setFont(new Font("Courier New", Font.ITALIC, 9));
        infoLabel.setForeground(new Color(100, 150, 150));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(infoLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStyledPanel(String title) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Holographic border glow
                g2d.setColor(GLOW_COLOR);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 8, 8);
                
                g2d.setColor(NEON_PURPLE);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 8, 8);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));
        
        if (title != null && !title.isEmpty()) {
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Courier New", Font.BOLD, 13));
            titleLabel.setForeground(NEON_PINK);
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(titleLabel);
            panel.add(Box.createVerticalStrut(12));
        }
        
        return panel;
    }
    
    private JCheckBox createStyledCheckbox(String text) {
        JCheckBox checkbox = new JCheckBox(text);
        checkbox.setFont(new Font("Courier New", Font.BOLD, 12));
        checkbox.setForeground(NEON_CYAN);
        checkbox.setBackground(PANEL_COLOR);
        checkbox.setFocusPainted(false);
        checkbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Custom checkbox icon for futuristic look
        checkbox.setIcon(new CheckBoxIcon(false));
        checkbox.setSelectedIcon(new CheckBoxIcon(true));
        
        return checkbox;
    }
    
    private JRadioButton createStyledRadioButton(String text) {
        JRadioButton radio = new JRadioButton(text);
        radio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        radio.setForeground(TEXT_COLOR);
        radio.setBackground(PANEL_COLOR);
        radio.setFocusPainted(false);
        
        return radio;
    }
    
    // Custom checkbox icon
    private class CheckBoxIcon implements Icon {
        private boolean selected;
        
        public CheckBoxIcon(boolean selected) {
            this.selected = selected;
        }
        
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw box with glow
            if (selected) {
                // Glow effect
                g2d.setColor(GLOW_COLOR);
                g2d.fillRoundRect(x - 1, y - 1, 18, 18, 5, 5);
                
                // Main box
                g2d.setColor(NEON_PINK);
                g2d.fillRoundRect(x, y, 16, 16, 4, 4);
                
                // Draw checkmark
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.drawLine(x + 4, y + 8, x + 7, y + 11);
                g2d.drawLine(x + 7, y + 11, x + 12, y + 5);
            } else {
                g2d.setColor(HOVER_COLOR);
                g2d.fillRoundRect(x, y, 16, 16, 4, 4);
                g2d.setColor(NEON_PURPLE);
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(x, y, 16, 16, 4, 4);
            }
        }
        
        @Override
        public int getIconWidth() {
            return 16;
        }
        
        @Override
        public int getIconHeight() {
            return 16;
        }
    }
}
