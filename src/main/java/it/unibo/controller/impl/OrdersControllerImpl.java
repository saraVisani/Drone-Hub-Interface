package it.unibo.controller.impl;

import it.unibo.controller.api.MainController;
import it.unibo.controller.api.MessageController;
import it.unibo.util.Enum.OrderType;
import it.unibo.util.Enum.PanelType;
import jssc.SerialPortException;

public class OrdersControllerImpl implements MessageController {

    private final SerialChannel serialChannel;
    private final MainController mainController;

    public OrdersControllerImpl(SerialChannel serialChannel, MainController mainController) {
        this.serialChannel = serialChannel;
        this.mainController = mainController;
    }

    @Override
    public PanelType getPanelType() {
        return PanelType.ORDERS;
    }
    public void sendOrder(OrderType order) {
        this.mainController.updateLaunchOrder(order);
        try {
            this.serialChannel.send(OrderType.orderMatch(order));
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }
}