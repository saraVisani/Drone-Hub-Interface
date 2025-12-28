package it.unibo.controller.impl;

import it.unibo.controller.api.MessageController;
import it.unibo.util.Enum.PanelType;

public class OrdersControllerImpl implements MessageController {

    private final SerialChannel serialChannel;
    private final MainControllerImpl mainController;

    public OrdersControllerImpl(SerialChannel serialChannel, MainControllerImpl mainController) {
        this.serialChannel = serialChannel;
        this.mainController = mainController;
    }

    @Override
    public PanelType getPanelType() {
        return PanelType.ORDERS;
    }
}
