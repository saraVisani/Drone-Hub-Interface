package it.unibo.controller.impl;

import java.util.HashMap;
import java.util.Map;

import it.unibo.controller.api.InputControllers;
import it.unibo.controller.api.MainController;
import it.unibo.controller.api.MessageController;
import it.unibo.util.Enum.OrderType;
import it.unibo.util.Enum.PanelType;
import jssc.SerialPortException;
public class MainControllerImpl implements MainController{

    private final Map<PanelType, MessageController> registry = new HashMap<>();

    public MainControllerImpl() {
        SerialChannel serialChannel = null;
        try {
            serialChannel = new SerialChannel("COM3", 9600);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        register(new MessageHandlerControllerImpl(serialChannel));
        register(new OrdersControllerImpl(serialChannel, this));
    }

    private void register(MessageController controller) {
        registry.put(controller.getPanelType(), controller);
    }

    @Override
    public MessageController getControllerFor(PanelType type) {
        return registry.get(type);
    }

    @Override
    public void updateLaunchOrder(OrderType order) {
        for (MessageController controller : registry.values()) {
            if (controller instanceof InputControllers inputController) {
                inputController.updateLaunchOrder(order);
            }
        }
    }
}
