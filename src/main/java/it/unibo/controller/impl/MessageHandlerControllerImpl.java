package it.unibo.controller.impl;

import java.util.ArrayList;
import java.util.List;

import it.unibo.controller.api.InputControllers;
import it.unibo.controller.api.MessageController;
import it.unibo.swing.panel.logic.ScreenPanel;
import it.unibo.util.Enum.OrderType;
import it.unibo.util.Enum.PanelType;

public class MessageHandlerControllerImpl implements MessageController, InputControllers{

    private final SerialChannel serialChannel;
    private ScreenPanel screenPanel;
    private OrderType currentOrder;
    private boolean occupied = false;

    public MessageHandlerControllerImpl(SerialChannel serialChannel) {
        this.serialChannel = serialChannel;
    }

    @Override
    public PanelType getPanelType() {
        return PanelType.LOGS;
    }

    public void update(){
        try {
            serialChannel.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (serialChannel.hasMsg()) {
            String msg = serialChannel.readMsg();
            ParsMessage(msg);
        }
    }

    private void ParsMessage(String msg){
        if (msg == null || msg.isEmpty()) return;

        String statusPart = "";
        String logPart = "";

        int starIndex = msg.indexOf('*');

        if (starIndex >= 0) {
            // C’è '*', dividiamo in due parti
            statusPart = msg.substring(0, starIndex);
            logPart = msg.substring(starIndex + 1); // tutto dopo '*'
        } else {
            // Nessun '*', consideriamo tutto status
            statusPart = msg;
        }

        // Creiamo liste di campi separati da '-'
        java.util.List<String> statusFields = splitByDash(statusPart);
        java.util.List<String> logFields = splitByDash(logPart);

        // Debug
        System.out.println("Status fields: " + statusFields);
        System.out.println("Log fields: " + logFields);

        // Aggiorniamo il pannello se presente
        if (screenPanel != null) {
            if(!statusFields.isEmpty()) {
                updateStatus(statusFields);
            }
            if(!logFields.isEmpty()) {
                updateLogs(logFields);
            }
        }
    }

    private List<String> splitByDash(String part) {
        List<String> list = new ArrayList<>();
        if (part == null || part.isEmpty()) return list;

        for (String s : part.split("-")) {
            if (!s.isEmpty()) list.add(s.trim());
        }
        return list;
    }

    private void updateStatus(List<String> statusFields) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateStatus'");
    }

    private void updateLogs(List<String> logFields) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateLogs'");
    }

    public void setScreenPanel(ScreenPanel screenPanel) {
        this.screenPanel = screenPanel;
    }

    @Override
    public void updateLaunchOrder(OrderType order) {
        if(!this.occupied){
            this.currentOrder = order;
            this.occupied = true;
        }
    }
}
