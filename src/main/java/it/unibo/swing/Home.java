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

        frame.setVisible(true);
    }
}
