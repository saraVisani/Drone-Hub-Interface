package src.main.java;

import src.main.java.controller.api.MainController;
import src.main.java.controller.impl.MainControllerImpl;
import src.main.java.swing.Home;
public class Main {

    public static void main(String[] args) {
        Home home = new Home();
        MainController controller = new MainControllerImpl();
        home.setController(controller);
    }
}
