package it.unibo.swing.panel.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

public class ScrewRoundPanel extends ScrewPanel {

    private final int arc = 28;

    @Override
    protected void paintMetal(Graphics2D g2, int w, int h) {
        GradientPaint metal = new GradientPaint(
                0, 0, METAL_LIGHT.brighter(),
                0, h, METAL_DARK
        );
        g2.setPaint(metal);
        g2.fillRoundRect(0, 0, w, h, arc * 2, arc * 2);
    }

    @Override
    protected void paintInnerPanel(Graphics2D g2, int w, int h) {
        g2.setColor(new Color(75, 75, 75));
        g2.fillRoundRect(
                frame, frame,
                w - frame * 2, h - frame * 2,
                arc, arc
        );
    }

    @Override
    protected void paintBorder(Graphics2D g2, int w, int h) {
        g2.setColor(new Color(120, 120, 120));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(
                frame, frame,
                w - frame * 2, h - frame * 2,
                arc, arc
        );
    }

    @Override
    protected void paintScrews(Graphics2D g2, int w, int h) {

        int r = 6;

        int inset = frame + arc / 2;

        drawScrew(g2, inset, inset, r);
        drawScrew(g2, w - inset, inset, r);
        drawScrew(g2, inset, h - inset, r);
        drawScrew(g2, w - inset, h - inset, r);
    }

    public int getArc() {
        return arc;
    }
}
