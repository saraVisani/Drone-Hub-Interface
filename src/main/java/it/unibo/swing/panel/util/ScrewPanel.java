package it.unibo.swing.panel.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ScrewPanel extends JPanel {

    protected static final Color METAL_LIGHT = new Color(170, 170, 170);
    protected static final Color METAL_DARK  = new Color(110, 110, 110);
    protected static final Color PANEL_BG    = new Color(60, 60, 60);
    protected static final Color SCREW_DARK  = new Color(80, 80, 80);
    protected static final Color SCREW_LIGHT = new Color(150, 150, 150);

    protected final int frame = 14;

    public ScrewPanel() {
        setOpaque(false); // IMPORTANTISSIMO
        setMargin(frame);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        paintMetal(g2, w, h);
        paintInnerPanel(g2, w, h);
        paintBorder(g2, w, h);
        paintScrews(g2, w, h);

        g2.dispose();
        super.paintComponent(g); // i figli vengono sopra
    }

    /* ================= HOOKS ================= */
    /**
     * Paint the metal background
     * @param g2 rendering context
     * @param w width
     * @param h height
     */
    protected void paintMetal(Graphics2D g2, int w, int h) {
        GradientPaint metal = new GradientPaint(
                0, 0, METAL_LIGHT,
                w, h, METAL_DARK
        );
        g2.setPaint(metal);
        g2.fillRect(0, 0, w, h);
    }

    /**
     * Paint the inner panel
     * @param g2 rendering context
     * @param w width
     * @param h height
     */
    protected void paintInnerPanel(Graphics2D g2, int w, int h) {
        g2.setColor(PANEL_BG);
        g2.fillRect(frame, frame, w - frame * 2, h - frame * 2);
    }

    /**
     * Paint the border around the inner panel
     * @param g2 rendering context
     * @param w width
     * @param h height
     */
    protected void paintBorder(Graphics2D g2, int w, int h) {
        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(40, 40, 40));
        g2.drawRect(frame, frame, w - frame * 2, h - frame * 2);
    }

    /**
     * Paint the screws at the corners
     * @param g2 rendering context
     * @param w width
     * @param h height
     */
    protected void paintScrews(Graphics2D g2, int w, int h) {
        int r = 6;
        int o = frame / 2;
        drawScrew(g2, o, o, r);
        drawScrew(g2, w - o, o, r);
        drawScrew(g2, o, h - o, r);
        drawScrew(g2, w - o, h - o, r);
    }

    /**
     * Draw a single screw
     * @param g2 rendering context
     * @param cx center x
     * @param cy center y
     * @param r radius
     */
    protected void drawScrew(Graphics2D g2, int cx, int cy, int r) {
        g2.setColor(SCREW_DARK);
        g2.fillOval(cx - r, cy - r, r * 2, r * 2);

        g2.setColor(SCREW_LIGHT);
        g2.drawOval(cx - r, cy - r, r * 2, r * 2);

        g2.setStroke(new BasicStroke(1.4f));
        g2.setColor(Color.BLACK);
        g2.drawLine(cx - r / 2, cy, cx + r / 2, cy);
        g2.drawLine(cx, cy - r / 2, cx, cy + r / 2);
    }

    /**
     * Set the margin of the panel
     * @param margin the margin to set
     */
    protected void setMargin(int margin) {
        setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
    }

    /**
     * @return the spessor of the panel border
     */
    public int getSpessor() {
        return frame;
    }
}
