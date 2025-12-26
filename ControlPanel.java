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
    
    // Color scheme
    private static final Color BACKGROUND_COLOR = new Color(45, 45, 48);
    private static final Color PANEL_COLOR = new Color(37, 37, 38);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    private static final Color ACCENT_COLOR = new Color(0, 122, 204);
    private static final Color HOVER_COLOR = new Color(62, 62, 64);
    
    public ControlPanel(ChaoticMouseApp app) {
        this.app = app;
        
        setTitle("🎮 Chaotic Mouse Control Center");
        setSize(400, 540);
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
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("⚡ Chaotic Mouse Controller");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(ACCENT_COLOR);
        
        JLabel subtitleLabel = new JLabel("Control the chaos with style");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(TEXT_COLOR);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(PANEL_COLOR);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);
        
        panel.add(textPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createMasterControlPanel() {
        JPanel panel = createStyledPanel("🎛️ Master Controls");
        
        pauseCheckbox = createStyledCheckbox("⏸️ Pause All Effects");
        pauseCheckbox.addActionListener(e -> {
            app.getAppState().paused = pauseCheckbox.isSelected();
        });
        
        panel.add(pauseCheckbox);
        
        return panel;
    }
    
    private JPanel createEffectsPanel() {
        JPanel panel = createStyledPanel("✨ Effect Toggles");
        
        chaoticCheckbox = createStyledCheckbox("🌀 Chaotic Movement");
        chaoticCheckbox.setSelected(true);
        chaoticCheckbox.addActionListener(e -> {
            app.getAppState().chaoticEnabled = chaoticCheckbox.isSelected();
        });
        
        flickerCheckbox = createStyledCheckbox("⚡ Flickering Effect");
        flickerCheckbox.setSelected(true);
        flickerCheckbox.addActionListener(e -> {
            app.getAppState().flickerEnabled = flickerCheckbox.isSelected();
        });
        
        panel.add(chaoticCheckbox);
        panel.add(Box.createVerticalStrut(8));
        panel.add(flickerCheckbox);
        
        return panel;
    }
    
    private JPanel createSlidersPanel() {
        JPanel panel = createStyledPanel("🎚️ Fine Tuning");
        
        // Number of cursors
        panel.add(createSliderSection("👻 Fake Cursors", 1, 15, 5, e -> {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                int value = source.getValue();
                app.getAppState().numCursors = value;
                app.updateNumCursors(value);
            }
        }));
        
        panel.add(Box.createVerticalStrut(12));
        
        // Flicker intensity
        panel.add(createSliderSection("💥 Flicker Intensity", 1, 20, 5, e -> {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                app.getAppState().flickerIntensity = source.getValue();
            }
        }));
        
        panel.add(Box.createVerticalStrut(12));
        
        // Speed threshold
        panel.add(createSliderSection("🚀 Speed Threshold", 1, 50, 15, e -> {
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
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(TEXT_COLOR);
        
        JLabel valueLabel = new JLabel(String.valueOf(initial));
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        valueLabel.setForeground(ACCENT_COLOR);
        
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
                
                // Paint filled portion
                int filledWidth = (int)((slider.getValue() - slider.getMinimum()) / 
                                       (double)(slider.getMaximum() - slider.getMinimum()) * 
                                       trackBounds.width);
                g2d.setColor(ACCENT_COLOR);
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
        JPanel panel = createStyledPanel("🎮 Challenge Mode");
        
        JButton gameButton = new JButton("🎯 Start Click Speed Game");
        gameButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gameButton.setBackground(new Color(0, 200, 0));
        gameButton.setForeground(Color.WHITE);
        gameButton.setFocusPainted(false);
        gameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gameButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        gameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        gameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                gameButton.setBackground(new Color(0, 230, 0));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                gameButton.setBackground(new Color(0, 200, 0));
            }
        });
        
        gameButton.addActionListener(e -> app.startGame());
        
        gameStatusLabel = new JLabel("⚠️ Game Status: Ready");
        gameStatusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        gameStatusLabel.setForeground(new Color(150, 150, 150));
        gameStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel infoLabel = new JLabel("<html><center>💡 Click targets while chaos is active!</center></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoLabel.setForeground(new Color(180, 180, 180));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(gameButton);
        panel.add(Box.createVerticalStrut(8));
        panel.add(gameStatusLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(infoLabel);
        
        return panel;
    }
    
    public void updateGameStatus(boolean active) {
        SwingUtilities.invokeLater(() -> {
            if (active) {
                gameStatusLabel.setText("✅ Game Status: ACTIVE - Good luck!");
                gameStatusLabel.setForeground(new Color(0, 255, 100));
            } else {
                gameStatusLabel.setText("⚠️ Game Status: Ready");
                gameStatusLabel.setForeground(new Color(150, 150, 150));
            }
        });
    }
    
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        JLabel infoLabel = new JLabel("<html><center>💡 Tip: Effects pause when this window is focused<br>" +
                                      "Close this window to exit the application</center></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        infoLabel.setForeground(new Color(150, 150, 150));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(infoLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStyledPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(HOVER_COLOR, 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        if (title != null && !title.isEmpty()) {
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(titleLabel);
            panel.add(Box.createVerticalStrut(10));
        }
        
        return panel;
    }
    
    private JCheckBox createStyledCheckbox(String text) {
        JCheckBox checkbox = new JCheckBox(text);
        checkbox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        checkbox.setForeground(TEXT_COLOR);
        checkbox.setBackground(PANEL_COLOR);
        checkbox.setFocusPainted(false);
        checkbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Custom checkbox icon for modern look
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
            
            // Draw box
            if (selected) {
                g2d.setColor(ACCENT_COLOR);
                g2d.fillRoundRect(x, y, 16, 16, 4, 4);
                
                // Draw checkmark
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(x + 4, y + 8, x + 7, y + 11);
                g2d.drawLine(x + 7, y + 11, x + 12, y + 5);
            } else {
                g2d.setColor(HOVER_COLOR);
                g2d.fillRoundRect(x, y, 16, 16, 4, 4);
                g2d.setColor(TEXT_COLOR);
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
