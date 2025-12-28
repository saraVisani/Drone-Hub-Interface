package it.unibo.controller.api;

import it.unibo.util.Enum.PanelType;

public interface MainController {
    MessageController getControllerFor(PanelType type);
}
