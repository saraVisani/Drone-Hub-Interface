package src.main.java.controller.impl;

import java.util.HashMap;
import java.util.Map;

import src.main.java.controller.api.MainController;
import src.main.java.controller.api.MessageController;
import src.main.java.util.Enum.PanelType;
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
