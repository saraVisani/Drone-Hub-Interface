package src.main.java.swing.panel.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class OldIndustrialButton extends JButton {

    private boolean pressed = false;

    public OldIndustrialButton() {
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int size = Math.min(w, h);

        int cx = w / 2;
        int cy = h / 2;

        int baseRadius   = size / 2 - 2;
        int buttonRadius = (int) (baseRadius * 0.72f);

        float pressScale = pressed ? 1.08f : 1.0f;
        int r = (int) (buttonRadius * pressScale);

        /* ================= METAL BASE ================= */

        RadialGradientPaint metal = new RadialGradientPaint(
                new Point2D.Float(cx, cy),
                baseRadius,
                new float[]{0f, 1f},
                new Color[]{
                        new Color(90, 90, 90),
                        new Color(30, 30, 30)
                }
        );

        g2.setPaint(metal);
        g2.fill(new Ellipse2D.Float(
                cx - baseRadius,
                cy - baseRadius,
                baseRadius * 2,
                baseRadius * 2
        ));

        /* ================= STATIC RED GLOW ================= */

        if (pressed) {
            int glowRadius = r + 10;

            RadialGradientPaint glow = new RadialGradientPaint(
                    new Point2D.Float(cx, cy),
                    glowRadius,
                    new float[]{0f, 1f},
                    new Color[]{
                            new Color(255, 60, 60, 120),
                            new Color(255, 60, 60, 0)
                    }
            );

            g2.setPaint(glow);
            g2.fillOval(
                    cx - glowRadius,
                    cy - glowRadius,
                    glowRadius * 2,
                    glowRadius * 2
            );
        }

        /* ================= RED BUTTON ================= */

        Color centerRed = pressed
                ? new Color(110, 0, 0)
                : new Color(90, 0, 0);

        Color edgeRed = pressed
                ? new Color(255, 150, 150)
                : new Color(180, 60, 60);

        RadialGradientPaint redButton = new RadialGradientPaint(
                new Point2D.Float(cx, cy),
                r,
                new float[]{0f, 0.65f, 1f},
                new Color[]{
                        centerRed,
                        centerRed,
                        edgeRed
                }
        );

        g2.setPaint(redButton);
        g2.fillOval(
                cx - r,
                cy - r,
                r * 2,
                r * 2
        );

        /* ================= INNER SHADOW (INCASSO) ================= */

        if (pressed) {

            int shadowInset = (int) (r * 0.08f);

            RadialGradientPaint innerShadow = new RadialGradientPaint(
                    new Point2D.Float(cx, cy),
                    r,
                    new float[]{0.70f, 1.0f},
                    new Color[]{
                            new Color(0, 0, 0, 0),
                            new Color(0, 0, 0, 60)
                    }
            );

            g2.setPaint(innerShadow);
            g2.fillOval(
                    cx - r + shadowInset,
                    cy - r + shadowInset,
                    (r - shadowInset) * 2,
                    (r - shadowInset) * 2
            );
        }

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(70, 70);
    }
}
