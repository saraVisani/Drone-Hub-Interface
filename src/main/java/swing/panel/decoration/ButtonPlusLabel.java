package src.main.java.swing.panel.decoration;

import javax.swing.*;
import java.awt.*;

import src.main.java.swing.panel.util.OldIndustrialButton;

public class ButtonPlusLabel extends JPanel {

    private LabelEncase label;
    private OldIndustrialButton button;

    public ButtonPlusLabel(String labelText) {
        setOpaque(false);
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(4, 4, 4, 4); // margini interni
        c.fill = GridBagConstraints.HORIZONTAL; // solo orizzontale per label
        c.weightx = 1.0;
        c.weighty = 0;

        // LABEL centrata
        label = new LabelEncase(labelText);
        label.getLable().setHorizontalAlignment(SwingConstants.CENTER); // importante per testo
        c.gridy = 0;
        add(label, c);

        // BUTTON sotto label
        button = new OldIndustrialButton();
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;  // scala sia in larghezza che altezza
        c.weightx = 1.0;
        c.weighty = 1.0;  // scala verticalmente con il panel
        add(button, c);

        // Permette al panel di scalare verticalmente in modo proporzionale
        c.gridy = 2;
        c.weighty = 1.0; // spazio flessibile sotto
        add(Box.createVerticalGlue(), c);
    }

    public LabelEncase getLabel() {
        return label;
    }

    public OldIndustrialButton getButton() {
        return button;
    }
}
