package it.unibo.swing.panel.decoration;

import javax.swing.*;
import java.awt.*;

public class VentPanel extends JPanel {

    public enum Alignment { LEFT, RIGHT }

    private final Alignment alignment;
    private final int count = 4; // numero di slot

    private final float minWidthRatio = 0.35f; // larghezza min relativa
    private final float maxWidthRatio = 0.75f; // larghezza max relativa
    private final float heightRatio = 0.08f;   // altezza relativa
    private final float gapRatio = 0.05f;      // spazio tra slot

    public VentPanel(Alignment alignment) {
        this.alignment = alignment;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        int slotHeight = Math.max(6, (int)(h * heightRatio));
        int gap = Math.max(4, (int)(h * gapRatio));
        int totalHeight = count * slotHeight + (count - 1) * gap;
        int startY = (h - totalHeight) / 2;

        for (int i = 0; i < count; i++) {
            float t = (float)i / (count - 1);
            float widthRatio = minWidthRatio + t * (maxWidthRatio - minWidthRatio);
            int slotW = (int)(w * widthRatio);
            int y = startY + i * (slotHeight + gap);

            int x = (alignment == Alignment.LEFT) ? 0 : w - slotW;
            drawVentSlot(g2, x, y, slotW, slotHeight);
        }

        g2.dispose();
    }

    private void drawVentSlot(Graphics2D g2, int x, int y, int w, int h) {
        // foro base
        g2.setColor(Color.BLACK);
        g2.fillRoundRect(x, y, w, h, h, h);

        // ombra interna (sotto)
        GradientPaint bottomShadow = new GradientPaint(
                x, y + h,
                new Color(0,0,0,120),
                x, y + h/2f,
                new Color(0,0,0,0)
        );
        g2.setPaint(bottomShadow);
        g2.fillRoundRect(x, y, w, h, h, h);

        // highlight interno (sopra)
        GradientPaint topHighlight = new GradientPaint(
                x, y,
                new Color(255,255,255,40),
                x, y + h/2f,
                new Color(255,255,255,0)
        );
        g2.setPaint(topHighlight);
        g2.fillRoundRect(x, y, w, h, h, h);
    }
}
