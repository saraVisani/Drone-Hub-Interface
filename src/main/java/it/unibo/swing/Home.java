package it.unibo.swing;

import javax.swing.JFrame;

import it.unibo.controller.api.MainController;
import it.unibo.swing.panel.Console;

public class Home {

    public Home(final MainController controller){
        JFrame frame = new JFrame("Drone Hangar Control Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        Console mainPanel = new Console(controller);
        frame.add(mainPanel);

        // Listener per chiusura finestra
        /*frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Funzioni di pulizia
                mainPanel.stopUpdateTimer();   // stop del timer Swing
                controller.close();            // eventuale metodo per chiudere risorse
            }
        });*/

        frame.setVisible(true);
    }
}
