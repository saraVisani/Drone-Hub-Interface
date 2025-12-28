package it.unibo.swing.panel.logic;

import javax.swing.*;
import java.awt.*;

import it.unibo.controller.impl.MessageHandlerControllerImpl;
import it.unibo.swing.panel.util.OldScreenTextArea;

public class LogsPanel extends JPanel {

    private OldScreenTextArea screenArea;

    public LogsPanel(MessageHandlerControllerImpl controller){
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        screenArea = new OldScreenTextArea();
        screenArea.setText("LOGS:\nNo errors detected.");
        JScrollPane scroll = new JScrollPane(screenArea);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setPreferredSize(new Dimension(250, 150));
        scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {

            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.BLACK; // colore dello slider
                this.trackColor = new Color(0, 255, 0);          // colore dello sfondo della barra
                this.thumbHighlightColor = new Color(0, 255, 0);
                this.thumbDarkShadowColor = new Color(0, 255, 0);
                this.trackHighlightColor = new Color(0, 255, 0);
                this.scrollBarWidth = 10;
            }

            @Override
            protected Dimension getMinimumThumbSize() {
                return new Dimension(8, 30); // dimensioni minime dello slider
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0,0));
                jbutton.setMinimumSize(new Dimension(0,0));
                jbutton.setMaximumSize(new Dimension(0,0));
                return jbutton;
            }
        });
        add(scroll, BorderLayout.CENTER);
    }

    public OldScreenTextArea getScreenArea() {
        return screenArea;
    }
}
