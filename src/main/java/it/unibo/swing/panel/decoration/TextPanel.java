package it.unibo.swing.panel.decoration;

import javax.swing.*;
import java.awt.*;
import it.unibo.swing.panel.util.OldScreenTextArea;

public class TextPanel extends JPanel {

    private OldScreenTextArea screenArea;

    /**
     * Costruisce un pannello con una OldScreenTextArea e uno scroll personalizzato.
     * @param initialText testo iniziale della textarea
     * @param preferredSize dimensione preferita dello scroll (opzionale)
     */
    public TextPanel(String initialText, Dimension preferredSize){
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        screenArea = new OldScreenTextArea();
        screenArea.setText(initialText);

        JScrollPane scroll = new JScrollPane(screenArea);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        if(preferredSize != null){
            scroll.setPreferredSize(preferredSize);
        }

        scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.BLACK;
                this.trackColor = new Color(0, 255, 0);
                this.thumbHighlightColor = new Color(0, 255, 0);
                this.thumbDarkShadowColor = new Color(0, 255, 0);
                this.trackHighlightColor = new Color(0, 255, 0);
                this.scrollBarWidth = 10;
            }

            @Override
            protected Dimension getMinimumThumbSize() {
                return new Dimension(8, 30);
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
