package src.main.java.swing;

import javax.swing.JFrame;

import src.main.java.controller.api.MainController;
import src.main.java.swing.panel.Console;

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
