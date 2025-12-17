package src.main.java.swing.panel.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ScrewPanel extends JPanel {

    // ---------- COLORI BORDO METALLO ----------
    private static final Color METAL_LIGHT = new Color(170, 170, 170);
    private static final Color METAL_DARK  = new Color(110, 110, 110);
    private static final Color PANEL_BG    = new Color(60, 60, 60);
    private static final Color SCREW_DARK  = new Color(80, 80, 80);
    private static final Color SCREW_LIGHT = new Color(150, 150, 150);

    private final int frame = 14;   // spessore bordo

    public ScrewPanel() {
        setBackground(PANEL_BG);
        setBorder(BorderFactory.createEmptyBorder(frame, frame, frame, frame));
    }

    // ---------- OVERRIDE paintComponent PER BORDO METALLO ----------
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        int frame = 14;     // spessore metallo
        int screwR = 6;     // raggio vite
        int offset = frame / 2;

        // --- METALLO ESTERNO ---
        GradientPaint metal = new GradientPaint(
                0, 0, METAL_LIGHT,
                w, h, METAL_DARK
        );
        g2.setPaint(metal);
        g2.fillRect(0, 0, w, h);

        // --- PANNELLO INTERNO ---
        g2.setColor(PANEL_BG);
        g2.fillRect(frame, frame, w - frame * 2, h - frame * 2);

        // --- BORDO INTERNO ---
        drawBorder(g2, frame, w, h);

        // --- VITI NEGLI ANGOLI ---
        drawScrew(g2, offset, offset, screwR);
        drawScrew(g2, w - offset, offset, screwR);
        drawScrew(g2, offset, h - offset, screwR);
        drawScrew(g2, w - offset, h - offset, screwR);

        g2.dispose();
    }

    private void drawScrew(Graphics2D g2, int cx, int cy, int r) {
        // corpo vite
        g2.setColor(SCREW_DARK);
        g2.fillOval(cx - r, cy - r, r * 2, r * 2);

        // highlight
        g2.setColor(SCREW_LIGHT);
        g2.drawOval(cx - r, cy - r, r * 2, r * 2);

        // croce
        g2.setStroke(new BasicStroke(1.4f));
        g2.setColor(Color.BLACK);
        g2.drawLine(cx - r / 2, cy, cx + r / 2, cy);
        g2.drawLine(cx, cy - r / 2, cx, cy + r / 2);
    }

    // Metodo protetto che disegna il bordo
    protected void drawBorder(Graphics2D g2, int frame, int w, int h) {
        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(40, 40, 40));
        g2.drawRect(frame, frame, w - frame * 2, h - frame * 2);
    }
}
