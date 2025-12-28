package it.unibo.swing.panel;

import java.awt.*;
import javax.swing.*;

import it.unibo.controller.api.MainController;
import it.unibo.controller.impl.MessageHandlerControllerImpl;
import it.unibo.controller.impl.OrdersControllerImpl;
import it.unibo.swing.panel.decoration.RadarPanel;
import it.unibo.swing.panel.decoration.ScreenPanel;
import it.unibo.swing.panel.decoration.VentPanel;
import it.unibo.swing.panel.logic.ButtonPanel;
import it.unibo.swing.panel.util.ScrewPanel;
import it.unibo.util.Enum.PanelType;

public class Console extends ScrewPanel {

    public Console(MainController controller) {
        setLayout(new GridBagLayout());
        GridBagConstraints cMain = new GridBagConstraints();
        cMain.fill = GridBagConstraints.BOTH;
        cMain.gridx = 0;
        cMain.gridy = 0;
        cMain.weightx = 1.0;
        cMain.weighty = 1.0;

        // ======================
        // RIGA 1 PANEL
        // ======================
        JPanel row1 = new JPanel(new GridBagLayout());
        row1.setOpaque(false);
        GridBagConstraints c1 = new GridBagConstraints();
        c1.fill = GridBagConstraints.BOTH;
        c1.gridy = 0;
        c1.insets = new Insets(0,0,0,0);

        // Vent SX
        VentPanel ventLeft = new VentPanel(VentPanel.Alignment.LEFT);
        c1.gridx = 0;
        c1.weightx = 2.0;
        c1.weighty = 1.0;
        row1.add(ventLeft, c1);

        // Spacer
        JPanel spacer1 = new JPanel();
        spacer1.setOpaque(false);
        c1.gridx = 1;
        c1.weightx = 0.25;
        row1.add(spacer1, c1);

        // Screen
        ScreenPanel screen = new ScreenPanel(
                (MessageHandlerControllerImpl) controller.getControllerFor(PanelType.LOGS)
        );
        c1.gridx = 2;
        c1.weightx = 6.0;
        row1.add(screen, c1);

        // Spacer
        JPanel spacer2 = new JPanel();
        spacer2.setOpaque(false);
        c1.gridx = 3;
        c1.weightx = 0.25;
        row1.add(spacer2, c1);

        // Vent DX
        VentPanel ventRight = new VentPanel(VentPanel.Alignment.RIGHT);
        c1.gridx = 4;
        c1.weightx = 2.0;
        row1.add(ventRight, c1);

        // Aggiungi riga 1 alla Console
        cMain.gridy = 0;
        cMain.weighty = 8.0; // peso riga 1
        cMain.insets = new Insets(8, 0, 4, 0); // margini sopra e sotto
        add(row1, cMain);

        // ======================
        // RIGA 2 PANEL
        // ======================
        JPanel row2 = new JPanel(new GridBagLayout());
        row2.setOpaque(false); // sfondo trasparente
        GridBagConstraints c2 = new GridBagConstraints();
        c2.fill = GridBagConstraints.BOTH;
        c2.gridy = 0;

        // Margin sinistro del row2
        c2.insets = new Insets(0, 6, 0, 0); // 6 px come margine laterale

        // ButtonPanel (sinistra)
        ButtonPanel buttons = new ButtonPanel((OrdersControllerImpl) controller.getControllerFor(PanelType.ORDERS));
        c2.gridx = 0;
        c2.weightx = 7.0;
        c2.weighty = 1.0;
        row2.add(buttons, c2);

        // Spacer tra ButtonPanel e Radar
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        c2.gridx = 1;
        c2.weightx = 0.25; // piccolo spazio
        row2.add(spacer, c2);

        // Radar (destra)
        RadarPanel radar = new RadarPanel();
        c2.gridx = 2;
        c2.weightx = 2.0;
        row2.add(radar, c2);

        // Margin destro del row2
        c2.gridx = 3;
        JPanel rightMargin = new JPanel();
        rightMargin.setOpaque(false);
        c2.weightx = 0.25; // come margine laterale destro
        row2.add(rightMargin, c2);

        // Aggiungi row2 alla Console
        cMain.gridy = 1;
        cMain.weightx = 1.0;
        cMain.weighty = 6.0;
        cMain.insets = new Insets(4, 0, 8, 0); // margini sopra e sotto della riga
        add(row2, cMain);
    }
}
