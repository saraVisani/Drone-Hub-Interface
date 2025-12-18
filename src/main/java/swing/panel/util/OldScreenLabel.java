package src.main.java.swing.panel.util;

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
        String text = getText();
        FontMetrics fm = g2.getFontMetrics();

        int x = (w - fm.stringWidth(text)) / 2;
        int y = (h + fm.getAscent() - fm.getDescent()) / 2;

        /* ================= GLOW BUFFER ================= */

        BufferedImage glow = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gg = glow.createGraphics();
        gg.setFont(getFont());
        gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        gg.setColor(new Color(0, 255, 80, 90));
        gg.drawString(text, x, y);
        gg.dispose();

        // blur CRT
        for (int dx = -3; dx <= 3; dx++) {
            for (int dy = -3; dy <= 3; dy++) {
                if (dx * dx + dy * dy <= 12) { //radius glow
                    g2.setComposite(
                            AlphaComposite.getInstance(
                                    AlphaComposite.SRC_OVER, 0.12f //strength of glow
                            )
                    );
                    g2.drawImage(glow, dx, dy, null);
                }
            }
        }

        /* ================= GHOSTING ================= */

        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.18f));
        g2.drawString(text, x - 2, y);

        /* ================= TESTO PRINCIPALE ================= */

        float flicker = 0.85f + rnd.nextFloat() * 0.15f;
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, flicker));
        g2.setColor(getForeground());
        g2.drawString(text, x, y);

        /* ================= SCANLINES ================= */

        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.08f));
        g2.setColor(getForeground());
        for (int i = 0; i < h; i += 2) {
            g2.drawLine(0, i, w, i);
        }

        g2.dispose();
    }
}
