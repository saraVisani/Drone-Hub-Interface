package it.unibo.controller.impl;

import it.unibo.controller.api.MessageController;
import it.unibo.util.Enum.PanelType;

public class MessageHandlerControllerImpl implements MessageController{

    @Override
    public PanelType getPanelType() {
        return PanelType.LOGS;
    }

    void update(){

    }
}
