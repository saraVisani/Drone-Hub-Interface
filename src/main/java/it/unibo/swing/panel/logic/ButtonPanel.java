package it.unibo.swing.panel.logic;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import it.unibo.controller.impl.OrdersControllerImpl;
import it.unibo.swing.panel.decoration.ButtonPlusLabel;
import it.unibo.util.Enum.OrderType;

public class ButtonPanel extends JPanel {

    private List<String> labels = List.of("TAKE OFF", "LANDING", "LOGS");

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
            addListener(bpl, i, controller);
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
    
    private void addListener(ButtonPlusLabel listening, final int i, OrdersControllerImpl controller) {
        listening.getButton().addActionListener(evt -> controller.sendOrder(OrderType.stringMatch(labels.get(i))));
    }
}

