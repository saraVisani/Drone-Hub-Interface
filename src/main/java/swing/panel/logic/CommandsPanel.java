package src.main.java.swing.panel.logic;

import javax.swing.JPanel;

import src.main.java.controller.impl.MessageHandlerControllerImpl;

import java.awt.*;

public class CommandsPanel extends JPanel {
    private MessageHandlerControllerImpl controller;

    public CommandsPanel(MessageHandlerControllerImpl controller){
        this.controller = controller;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(250, 150));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        g.setFont(new Font("Monospaced", Font.PLAIN, 14));
        g.drawString("DRONE STATUS: READY", 10, 20);
        g.drawString("BATTERY: 100%", 10, 40);
    }
}
