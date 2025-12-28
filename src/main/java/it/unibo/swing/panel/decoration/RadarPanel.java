package it.unibo.swing.panel.decoration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;

import it.unibo.swing.panel.util.ScrewRoundPanel;

public class RadarPanel extends ScrewRoundPanel implements ActionListener {

    private double radarAngle = 0;
    private final Timer timer;

    private static final Color RADAR_GREEN = new Color(0, 255, 100);
    private static final Color GRID_GREEN  = new Color(0, 120, 60);
    private static final Color BACKGROUND  = new Color(5, 32, 5);

    public RadarPanel() {
        setPreferredSize(new Dimension(150, 150));
        setBackground(new Color(85, 86, 89));
        timer = new Timer(40, this);
        timer.start();
    }

    // Forza quadrato
    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        int size = Math.min(d.width, d.height);
        return new Dimension(size, size);
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension d = super.getMinimumSize();
        int size = Math.min(d.width, d.height);
        return new Dimension(size, size);
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension d = super.getMaximumSize();
        int size = Math.min(d.width, d.height);
        return new Dimension(size, size);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int frame = super.getSpessor() + 60 * super.getSpessor() / 100;

        int innerW = getWidth() - frame * 2;
        int innerH = getHeight() - frame * 2;
        int size = Math.min(innerW, innerH);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        int radius = size / 2;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // sfondo radar
        g2.setColor(BACKGROUND);
        g2.fillOval(cx - radius, cy - radius, size, size);

        // griglia
        g2.setColor(GRID_GREEN);
        for (int i = 1; i <= 4; i++) {
            int r = radius * i / 4;
            g2.drawOval(cx - r, cy - r, r * 2, r * 2);
        }
        for (int a = 0; a < 360; a += 30) {
            double rad = Math.toRadians(a);
            g2.drawLine(cx, cy,
                        (int) (cx + Math.cos(rad) * radius),
                        (int) (cy + Math.sin(rad) * radius));
        }

        // settore radar
        drawRadarWedge(g2, cx, cy, radius, radarAngle);

        // linea radar
        drawRadarLine(g2, cx, cy, radius, radarAngle);

        // bordo radar
        g2.setStroke(new BasicStroke(2));
        g2.setColor(RADAR_GREEN);
        g2.drawOval(cx - radius, cy - radius, size - 1, size - 1);

        g2.dispose();
    }

    private void drawRadarWedge(Graphics2D g2, int cx, int cy, int radius, double angleDeg) {
        int steps = 1500;
        double spread = 360;

        double center = -angleDeg; // Arc2D = antiorario

        for (int i = 0; i < steps; i++) {
            double t = (double) i / steps;

            // fade dietro la linea
            float alpha = (float) Math.pow(1.0 - t, 2.8);
            g2.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, alpha * 0.45f));

            double start = center + (t * spread);
            double extent = spread / steps;

            Shape slice = new Arc2D.Double(
                    cx - radius, cy - radius,
                    radius * 2, radius * 2,
                    start,
                    extent,
                    Arc2D.PIE
            );

            g2.setColor(RADAR_GREEN);
            g2.fill(slice);
        }

        g2.setComposite(AlphaComposite.SrcOver);
    }

    private void drawRadarLine(Graphics2D g2, int cx, int cy, int radius, double angleDeg) {
        double rad = Math.toRadians(angleDeg);
        int x = (int) (cx + Math.cos(rad) * radius);
        int y = (int) (cy + Math.sin(rad) * radius);

        // glow
        g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(0, 255, 100, 60));
        g2.drawLine(cx, cy, x, y);

        // linea principale
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(RADAR_GREEN);
        g2.drawLine(cx, cy, x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        radarAngle = (radarAngle + 1.5) % 360;
        repaint();
    }
}
