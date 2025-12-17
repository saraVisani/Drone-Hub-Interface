package src.main.java.swing.panel.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

public class ScrewRoundPanel extends ScrewPanel {

    private final int arc = 20;

    @Override
    protected void drawBorder(Graphics2D g2, int frame, int w, int h) {
        RoundRectangle2D border = new RoundRectangle2D.Double(
                frame, frame,
                w - frame * 2, h - frame * 2,
                arc, arc
        );
        g2.setColor(new Color(200, 200, 200)); // colore chiaro del bordo
        g2.fill(border); // riempie il bordo arrotondato
    }
}
