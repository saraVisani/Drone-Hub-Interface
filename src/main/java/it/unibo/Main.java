package it.unibo;

import it.unibo.controller.api.MainController;
import it.unibo.controller.impl.MainControllerImpl;
import it.unibo.swing.Home;

public class Main {

    public static void main(String[] args) {
        MainController controller = new MainControllerImpl();
        new Home(controller);
    }
}
