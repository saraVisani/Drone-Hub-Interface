package src.main.java.swing.panel.logic;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import src.main.java.controller.impl.OrdersControllerImpl;
import src.main.java.swing.panel.decoration.ButtonPlusLabel;

public class ButtonPanel extends JPanel {

    List<String> labels = List.of("TAKE OFF", "LANDING", "LOGS");

    public ButtonPanel(OrdersControllerImpl controller) {
        setLayout(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.weighty = 1.0; // altezza uniforme
        int col = 0;

        for (int i = 0; i < labels.size(); i++) {
            // aggiungi panel
            ButtonPlusLabel bpl = new ButtonPlusLabel(labels.get(i));
            c.gridx = col++;
            c.weightx = 1.0; // tutti i panel uguale larghezza
            add(bpl, c);

            // aggiungi spacer tranne dopo l'ultimo
            if (i < labels.size() - 1) {
                JPanel spacer = new JPanel();
                spacer.setOpaque(false);
                c.gridx = col++;
                c.weightx = 0.1; // spazio piccolo tra i panel
                add(spacer, c);
            }
        }
    }
}
