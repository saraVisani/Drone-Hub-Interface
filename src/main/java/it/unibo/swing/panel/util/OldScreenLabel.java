package it.unibo.swing.panel.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class OldScreenLabel extends JLabel {

    private final Random rnd = new Random();

    public OldScreenLabel(String text) {
        super(text);
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
        setForeground(new Color(0, 255, 80));
        setOpaque(true);
        setBackground(Color.BLACK);

        // repaint periodico per flicker / jitter
        Timer t = new Timer(40, e -> repaint());
        t.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        Insets insets = getInsets();

        String text = getText();
        FontMetrics fm = g2.getFontMetrics();

        // disponibile width e height
        int availableWidth = w - insets.left - insets.right;
        int availableHeight = h - insets.top - insets.bottom;

        // coordinate centraggio
        int textWidth = fm.stringWidth(text);
        int textX = insets.left + (availableWidth - textWidth) / 2;
        int textY = insets.top + (availableHeight + fm.getAscent() - fm.getDescent()) / 2;

        // ================= GLOW BUFFER =================
        BufferedImage glow = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gg = glow.createGraphics();
        gg.setFont(getFont());
        gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        gg.setColor(new Color(0, 255, 80, 90));
        gg.drawString(text, textX, textY);
        gg.dispose();

        // disegna glow con blur
        for (int dx = -3; dx <= 3; dx++) {
            for (int dy = -3; dy <= 3; dy++) {
                if (dx*dx + dy*dy <= 12) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.12f));
                    g2.drawImage(glow, dx, dy, null);
                }
            }
        }

        // ================= GHOSTING =================
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.18f));
        g2.drawString(text, textX - 2, textY);

        // ================= TESTO PRINCIPALE =================
        float flicker = 0.85f + rnd.nextFloat() * 0.15f;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, flicker));
        g2.setColor(getForeground());
        g2.drawString(text, textX, textY);

        // ================= SCANLINES =================
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.08f));
        g2.setColor(getForeground());
        for (int i = 0; i < h; i += 2) {
            g2.drawLine(0, i, w, i);
        }

        g2.dispose();
    }
}
