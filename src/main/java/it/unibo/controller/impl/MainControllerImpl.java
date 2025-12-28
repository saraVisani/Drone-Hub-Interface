package it.unibo.controller.impl;

import java.util.HashMap;
import java.util.Map;

import it.unibo.controller.api.MainController;
import it.unibo.controller.api.MessageController;
import it.unibo.util.Enum.PanelType;
public class MainControllerImpl implements MainController{

    private final Map<PanelType, MessageController> registry = new HashMap<>();

    public MainControllerImpl() {
        register(new MessageHandlerControllerImpl());
        register(new OrdersControllerImpl());
    }

    private void register(MessageController controller) {
        registry.put(controller.getPanelType(), controller);
    }

    @Override
    public MessageController getControllerFor(PanelType type) {
        return registry.get(type);
    }
}
