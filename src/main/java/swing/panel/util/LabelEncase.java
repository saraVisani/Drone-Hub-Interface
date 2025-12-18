package src.main.java.swing.panel.util;

import javax.swing.*;
import java.awt.*;

public class LabelEncase extends ScrewPanel {

    private static final int frame = 6; // spessore del bordo
    private OldScreenLabel screenLabel;

    public LabelEncase(String text) {
        super();
        setLayout(new BorderLayout());

        screenLabel = new OldScreenLabel(text);
        screenLabel.setBorder(BorderFactory.createEmptyBorder(frame, frame, frame, frame));
        screenLabel.setOpaque(false);
        add(screenLabel, BorderLayout.CENTER);
    }

    @Override
    protected void paintMetal(Graphics2D g2, int w, int h) {
        // Lato sinistro chiaro
        g2.setColor(new Color(120, 120, 120));
        g2.fillPolygon(new Polygon(
                new int[]{0, frame, frame, 0},
                new int[]{0, frame, h - frame, h},
                4
        ));

        // Lato basso leggermente più chiaro
        g2.setColor(new Color(160, 160, 160));
        g2.fillPolygon(new Polygon(
                new int[]{frame, w - frame, w, 0},
                new int[]{h - frame, h - frame, h, h},
                4
        ));

        // Lato destro scuro
        g2.setColor(new Color(50, 50, 50));
        g2.fillPolygon(new Polygon(
                new int[]{w - frame, w, w, w - frame},
                new int[]{frame, 0, h, h - frame},
                4
        ));

        // Lato alto più scuro
        g2.setColor(new Color(30, 30, 30));
        g2.fillPolygon(new Polygon(
                new int[]{0, w, w - frame, frame},
                new int[]{0, 0, frame, frame},
                4
        ));
    }

    @Override
    protected void paintInnerPanel(Graphics2D g2, int w, int h) {
        g2.setColor(Color.BLACK);
        g2.fillRect(frame, frame, w - frame * 2, h - frame * 2);
    }

    @Override
    protected void paintBorder(Graphics2D g2, int w, int h) {
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.BLACK);
        g2.drawRect(frame, frame, w - frame * 2, h - frame * 2);
    }

    @Override
    protected void paintScrews(Graphics2D g2, int w, int h) {
        // opzionale: viti negli angoli
    }

    @Override
    protected void setMargin(int margin) {
        super.setMargin(frame);
    }

    // Metodo per aggiornare il testo dinamicamente
    public void setText(String text) {
        screenLabel.setText(text);
    }
}
