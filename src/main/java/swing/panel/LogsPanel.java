package src.main.java.swing.panel;

import javax.swing.JPanel;

import src.main.java.controller.impl.MessageHandlerControllerImpl;

import java.awt.*;

public class LogsPanel extends JPanel {
    private MessageHandlerControllerImpl controller;

    public LogsPanel(MessageHandlerControllerImpl controller){
        this.controller = controller;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(250, 150));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        g.setFont(new Font("Monospaced", Font.PLAIN, 14));
        g.drawString("LOGS:", 10, 20);
        g.drawString("No errors detected.", 10, 40);
    }
}
