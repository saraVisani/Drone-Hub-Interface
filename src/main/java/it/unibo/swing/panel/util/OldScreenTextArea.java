package it.unibo.swing.panel.util;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.List;

public class OldScreenTextArea extends JTextArea {

    private final Random rnd = new Random();
    private final int padding = 5;

    public OldScreenTextArea() {
        setBackground(Color.BLACK);
        setForeground(new Color(0, 255, 80));
        setFont(new Font("Monospaced", Font.PLAIN, 14));
        setLineWrap(true);
        setWrapStyleWord(true);
        setEditable(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // sfondo nero
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        FontMetrics fm = g2.getFontMetrics();
        int lineHeight = fm.getHeight();

        // gestisco solo il testo visibile, rispettando wrap automatico
        int startOffset = 0;
        int endOffset = getDocument().getLength();
        int y = fm.getAscent() + padding;

        try {
            int pos = startOffset;
            while (pos < endOffset) {
                int lineEnd = Utilities.getRowEnd(this, pos);
                String line = getDocument().getText(pos, lineEnd - pos);

                int x = padding;

                // ================= GLOW =================
                BufferedImage glow = new BufferedImage(getWidth(), lineHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D gg = glow.createGraphics();
                gg.setFont(getFont());
                gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                gg.setColor(new Color(0, 255, 80, 90));
                gg.drawString(line, 0, fm.getAscent());
                gg.dispose();

                for (int dx = -2; dx <= 2; dx++) {
                    for (int dy = -2; dy <= 2; dy++) {
                        if (dx*dx + dy*dy <= 5) {
                            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.12f));
                            g2.drawImage(glow, x + dx, y - fm.getAscent() + dy, null);
                        }
                    }
                }

                // ================= GHOSTING =================
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.18f));
                g2.drawString(line, x - 1, y);

                // ================= TESTO PRINCIPALE =================
                float flicker = 0.85f + rnd.nextFloat() * 0.15f;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, flicker));
                g2.setColor(getForeground());
                g2.drawString(line, x, y);

                pos = lineEnd + 1; // vai alla riga successiva
                y += lineHeight;
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        // ================= SCANLINES =================
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.08f));
        g2.setColor(getForeground());
        for (int i = 0; i < getHeight(); i += 2) {
            g2.drawLine(0, i, getWidth(), i);
        }

        g2.dispose();
    }

    // Metodi utili per modificare il testo
    public void appendLine(String line) {
        setText(getText() + (getText().isEmpty() ? "" : "\n") + line);
        repaint();
    }

    public void setLines(List<String> newLines) {
        setText(String.join("\n", newLines));
        repaint();
    }
}
