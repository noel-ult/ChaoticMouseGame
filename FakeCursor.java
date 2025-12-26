import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * FakeCursor - A transparent window that displays a fake cursor
 * that looks like the actual system cursor
 */
public class FakeCursor extends JWindow {
    private String shape;
    private CursorPanel cursorPanel;
    private Dimension screenSize;
    private static final int SIZE = 20;
    
    // Color scheme for realistic cursor appearance
    private static final Color CURSOR_WHITE = new Color(255, 255, 255);
    private static final Color CURSOR_BLACK = new Color(0, 0, 0);
    private static final Color CURSOR_SHADOW = new Color(0, 0, 0, 100);
    
    public FakeCursor(String shape) {
        this.shape = shape;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        setSize(SIZE * 2, SIZE * 2);
        setAlwaysOnTop(true);
        
        // Make window transparent
        setBackground(new Color(0, 0, 0, 0));
        
        cursorPanel = new CursorPanel();
        add(cursorPanel);
        
        setVisible(true);
    }
    
    public void setShape(String shape) {
        this.shape = shape;
        cursorPanel.repaint();
    }
    
    public void moveTo(int x, int y) {
        // Center the cursor on the target point
        x = Math.max(0, Math.min(screenSize.width - SIZE * 2, x - SIZE));
        y = Math.max(0, Math.min(screenSize.height - SIZE * 2, y - SIZE));
        setLocation(x, y);
    }
    
    private class CursorPanel extends JPanel {
        public CursorPanel() {
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            // Enable anti-aliasing for smooth shapes
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                                RenderingHints.VALUE_RENDER_QUALITY);
            
            // Draw cursor pointer (always looks like actual cursor)
            drawCursorPointer(g2d);
        }
        
        private void drawCursorPointer(Graphics2D g2d) {
            // Create the classic arrow cursor shape
            int[] xPoints = {5, 5, 9, 12, 15, 12, 10};
            int[] yPoints = {5, 22, 18, 24, 23, 19, 17};
            
            // Draw shadow for depth
            g2d.setColor(CURSOR_SHADOW);
            int[] xShadow = new int[xPoints.length];
            int[] yShadow = new int[yPoints.length];
            for (int i = 0; i < xPoints.length; i++) {
                xShadow[i] = xPoints[i] + 2;
                yShadow[i] = yPoints[i] + 2;
            }
            g2d.fillPolygon(xShadow, yShadow, xPoints.length);
            
            // Draw black outline
            g2d.setColor(CURSOR_BLACK);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawPolygon(xPoints, yPoints, xPoints.length);
            
            // Draw white fill
            g2d.setColor(CURSOR_WHITE);
            g2d.fillPolygon(xPoints, yPoints, xPoints.length);
            
            // Draw black outline again for crisp edges
            g2d.setColor(CURSOR_BLACK);
            g2d.setStroke(new BasicStroke(1.2f));
            g2d.drawPolygon(xPoints, yPoints, xPoints.length);
        }
    }
}
