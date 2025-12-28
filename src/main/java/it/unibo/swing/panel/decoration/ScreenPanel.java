package it.unibo.swing.panel.decoration;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;

import it.unibo.controller.impl.MessageHandlerControllerImpl;
import it.unibo.swing.panel.logic.CommandsPanel;
import it.unibo.swing.panel.logic.LogsPanel;
import it.unibo.swing.panel.util.ScrewRoundPanel;

public class ScreenPanel extends ScrewRoundPanel {

    public ScreenPanel(MessageHandlerControllerImpl controller) {

        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        // IMPORTANTISSIMO:
        // niente spazio orizzontale → si toccano
        c.insets = new Insets(6, 6, 6, 6);

        // ---------- COMMANDS (SINISTRA) ----------
        CommandsPanel commands = new CommandsPanel(controller);

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 3.0;   // 3 parti
        c.weighty = 1.0;

        add(commands, c);

        // ---------- LOGS (DESTRA) ----------
        LogsPanel logs = new LogsPanel(controller);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1.0;   // 1 parte

        // azzera solo l'inset SINISTRO così si attaccano
        c.insets = new Insets(6, 0, 6, 6);

        add(logs, c);
    }

    @Override
    protected void paintInnerPanel(Graphics2D g2, int w, int h) {
        g2.setColor(Color.BLACK);
        g2.fillRoundRect(
                frame, frame,
                w - frame * 2, h - frame * 2,
                super.getArc(), super.getArc()
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // sfondo e bordo

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Disegna linea tra Commands e Logs
        if (getComponentCount() >= 2) {
            // supponendo che Commands sia 0 e Logs sia 1
            Component commands = getComponent(0);

            int lineX = commands.getX() + commands.getWidth() + 2; // bordo destro di Commands
            int lineTop = frame + 2;
            int lineBottom = getHeight() - frame - 2;

            g2.setColor(Color.DARK_GRAY);
            g2.setStroke(new BasicStroke(2f));
            g2.drawLine(lineX, lineTop, lineX, lineBottom);
        }

        g2.dispose();
    }

}
