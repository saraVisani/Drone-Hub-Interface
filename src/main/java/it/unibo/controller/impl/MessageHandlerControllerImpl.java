package it.unibo.controller.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingUtilities;

import it.unibo.controller.api.MessageController;
import it.unibo.swing.panel.logic.ScreenPanel;
import it.unibo.util.Enum.OrderType;
import it.unibo.util.Enum.PanelType;

public class MessageHandlerControllerImpl implements MessageController {

    private final SerialChannel serialChannel;

    private ScreenPanel screenPanel;

    private OrderType currentOrder = null;

    private boolean alarmInProgress = false;
    private boolean orderInProgress = false;
    private boolean logInProgress = false;

    private int lineCounter = 0;
    private int logCounter = 0;
    private int globalCounter = -1;

    private final List<String> labels = List.of(
            "Stato Hangar",
            "Stato Drone",
            "Temperatura Hangar",
            "Take off",
            "Landing"
    );

    public MessageHandlerControllerImpl(SerialChannel serialChannel) {
        this.serialChannel = serialChannel;
    }

    @Override
    public PanelType getPanelType() {
        return PanelType.LOGS;
    }

    public void setScreenPanel(ScreenPanel screenPanel) {
        this.screenPanel = screenPanel;
    }

    public void update() {

        try {

            serialChannel.update();

        } catch (Exception e) {

            e.printStackTrace();
            return;
        }

        if (serialChannel.hasMsg()) {

            String msg = serialChannel.readMsg();

            if (msg != null) {
                parseMessage(msg);
            }
        }
    }

    /**
     * Parses messages received from Arduino.
     *
     * STATUS:
     *
     * OK-Idle-TEMP-N/A
     *
     * OK-Operating-TEMP-N/A
     *
     * OK-Take off-TEMP-AVG-SINGLE
     *
     * OK-Landing-DISTANCE-*-SINGLE-TEMP-N/A
     *
     * LOG:
     *
     * *-OK-Operating-TEMP-N/A-N/A
     *
     * *-OK-Take off-TEMP-AVG-SINGLE-N/A-N/A
     *
     * *-OK-Landing-TEMP-TAKEOFF_AVG-TAKEOFF_SINGLE-LANDING_AVG-LANDING_SINGLE
     */
    private void parseMessage(String msg) {

        if (msg == null || msg.isBlank()) {
            return;
        }

        msg = msg.trim();

        if(msg.charAt(1) == 'O') {
            alarmInProgress = false;
        }
        if(msg.charAt(1) == 'A') {
            alarmInProgress = true;
            return;
        }

        /*
         * ============================================================
         * ALARM / ERROR / COMMAND IGNORED MESSAGE
         * ============================================================
         * Handles direct system warning messages like:
         * "System state is Alarm. Command ignored."
         */
        if (msg.startsWith("System state is")) {
            safeAppendLine("System -> " + msg + "\n");

            // Se un ordine era in corso, lo resettiamo in quanto ignorato
            orderInProgress = false;
            currentOrder = null;
            return;
        }

        /*
         * ============================================================
         * LOG MESSAGE
         * ============================================================
         *
         * The '*' indicates that this is a LOG message.
         *
         * The information after '*' has the same meaning as the
         * normal status message.
         */
        if (msg.startsWith("*")) {

            String logPart = msg.substring(1);

            List<String> logFields = splitByDash(logPart);

            if (logFields.size() < 2) {
                return;
            }

            /*
             * PRE-ALARM
             *
             * PRE-ALARM is split into:
             *
             * [PRE, ALARM, ...]
             *
             * and must become:
             *
             * [Pre-Alarm, ...]
             */
            if (logFields.size() >= 2 &&
                    "PRE".equalsIgnoreCase(logFields.get(0)) &&
                    "ALARM".equalsIgnoreCase(logFields.get(1))) {

                logFields.set(0, "Pre-Alarm");
                logFields.remove(1);
            }

            String droneState = logFields.get(1);

            /*
             * The LOG message is converted to the same seven fields
             * used by updateLogs().
             */
            if ("LANDING".equalsIgnoreCase(droneState)) {

                handleLogLanding(logFields);

            } else if ("TAKEOFF".equalsIgnoreCase(droneState)
                    || "TAKE OFF".equalsIgnoreCase(droneState)) {

                handleLogTakeoff(logFields);

            } else {

                handleLogNormalStatus(logFields);
            }

            logInProgress = false;
            currentOrder = null;

            return;
        }

        /*
         * ============================================================
         * NORMAL STATUS MESSAGE
         * ============================================================
         */

        String statusPart = msg;
        String landingPart = null;

        int starIndex = msg.indexOf('*');

        /*
         * LANDING contains a '*' inside the status message.
         *
         * Example:
         *
         * OK-Landing-50-*-48-25-N/A
         */
        if (starIndex >= 0) {

            statusPart = msg.substring(0, starIndex);

            landingPart = msg.substring(starIndex + 1);
        }

        List<String> statusFields = splitByDash(statusPart);

        /*
         * PRE-ALARM
         *
         * PRE-ALARM-IDLE-25-N/A
         *
         * becomes:
         *
         * [Pre-Alarm, IDLE, 25, N/A]
         */
        if (statusFields.size() >= 2 &&
                "PRE".equalsIgnoreCase(statusFields.get(0)) &&
                "ALARM".equalsIgnoreCase(statusFields.get(1))) {

            statusFields.set(0, "Pre-Alarm");
            statusFields.remove(1);
        }

        if (statusFields.size() < 2) {
            return;
        }

        /*
         * Update the current status shown in the command/status panel.
         */
        if (screenPanel != null) {
            updateStatus(statusFields);
        }

        String droneState = statusFields.get(1);

        /*
         * LANDING
         */
        if ("LANDING".equalsIgnoreCase(droneState)) {

            handleLanding(
                    statusFields,
                    landingPart
            );

        /*
         * TAKE OFF
         */
        } else if ("TAKEOFF".equalsIgnoreCase(droneState)
                || "TAKE OFF".equalsIgnoreCase(droneState)) {

            handleTakeoff(statusFields);

        /*
         * IDLE / OPERATING / other states
         */
        } else {

            handleNormalStatus(statusFields);
        }

        /*
         * The order has completed.
         */
        if (currentOrder == OrderType.TAKE_OFF ||
                currentOrder == OrderType.LANDING) {

            orderInProgress = false;
            currentOrder = null;
        }
    }

    /**
     * Handles a LOG message when the drone is not taking off or landing.
     *
     * Example:
     *
     * *-OK-Operating-22.48-N/A-N/A
     *
     * becomes:
     *
     * [OK, Operating, 22.48, N/A, N/A, N/A, N/A]
     */
    private void handleLogNormalStatus(List<String> logFields) {

        if (screenPanel == null) {
            return;
        }

        List<String> preparedLog = new ArrayList<>();

        preparedLog.add(getField(logFields, 0));
        preparedLog.add(getField(logFields, 1));
        preparedLog.add(getField(logFields, 2));

        preparedLog.add("N/A");
        preparedLog.add("N/A");

        preparedLog.add("N/A");
        preparedLog.add("N/A");

        updateLogs(preparedLog);
    }

    /**
     * Handles a LOG message during TAKEOFF.
     *
     * Example:
     *
     * *-OK-Takeoff-22.48-0.00-0.00
     *
     * becomes:
     *
     * [OK, Takeoff, 22.48, 0.00, 0.00, N/A, N/A]
     */
    private void handleLogTakeoff(List<String> logFields) {

        if (screenPanel == null) {
            return;
        }

        List<String> preparedLog = new ArrayList<>();

        preparedLog.add(getField(logFields, 0));
        preparedLog.add(getField(logFields, 1));
        preparedLog.add(getField(logFields, 2));

        preparedLog.add(getField(logFields, 3));
        preparedLog.add(getField(logFields, 4));

        preparedLog.add("N/A");
        preparedLog.add("N/A");

        updateLogs(preparedLog);
    }

    /**
     * Handles a LOG message during LANDING.
     *
     * The LOG message already contains all landing information.
     *
     * Format:
     *
     * *-OK-Landing-TEMP-TAKEOFF_AVG-TAKEOFF_SINGLE-LANDING_AVG-LANDING_SINGLE
     */
    private void handleLogLanding(List<String> logFields) {

        if (screenPanel == null) {
            return;
        }

        List<String> preparedLog = new ArrayList<>();

        preparedLog.add(getField(logFields, 0));
        preparedLog.add(getField(logFields, 1));
        preparedLog.add(getField(logFields, 2));

        preparedLog.add(getField(logFields, 3));
        preparedLog.add(getField(logFields, 4));

        preparedLog.add(getField(logFields, 5));
        preparedLog.add(getField(logFields, 6));

        updateLogs(preparedLog);
    }

    /**
     * Handles a normal STATUS message.
     *
     * Only system, drone and temperature are available.
     */
    private void handleNormalStatus(List<String> statusFields) {

        if (screenPanel == null) {
            return;
        }

        List<String> preparedLog = new ArrayList<>();

        preparedLog.add(getField(statusFields, 0));
        preparedLog.add(getField(statusFields, 1));
        preparedLog.add(getField(statusFields, 2));

        preparedLog.add("N/A");
        preparedLog.add("N/A");

        preparedLog.add("N/A");
        preparedLog.add("N/A");

        updateLogs(preparedLog);
    }

    /**
     * Handles TAKEOFF status.
     *
     * Format:
     *
     * SYSTEM-TAKEOFF-TEMP-TAKEOFF_AVG-TAKEOFF_SINGLE
     */
    private void handleTakeoff(List<String> statusFields) {

        if (screenPanel == null) {
            return;
        }

        List<String> preparedLog = new ArrayList<>();

        preparedLog.add(getField(statusFields, 0));
        preparedLog.add(getField(statusFields, 1));
        preparedLog.add(getField(statusFields, 2));

        preparedLog.add(getField(statusFields, 3));
        preparedLog.add(getField(statusFields, 4));

        preparedLog.add("N/A");
        preparedLog.add("N/A");

        updateLogs(preparedLog);
    }

    /**
     * Handles LANDING status.
     *
     * Arduino format:
     *
     * SYSTEM-LANDING-DISTANCE-*-SINGLE-TEMP-N/A
     *
     * Example:
     *
     * OK-Landing-50-*-48-25-N/A
     *
     * statusFields:
     *
     * [OK, Landing, 50]
     *
     * landingFields:
     *
     * [48, 25, N/A]
     *
     * Final format:
     *
     * [OK, Landing, 25, N/A, N/A, 50, 48]
     */
    private void handleLanding(
            List<String> statusFields,
            String landingPart) {

        if (screenPanel == null) {
            return;
        }

        List<String> landingFields =
                splitByDash(landingPart);

        List<String> preparedLog = new ArrayList<>();

        preparedLog.add(
                getField(statusFields, 0)
        );

        preparedLog.add(
                getField(statusFields, 1)
        );

        /*
         * Temperature is after the '*':
         *
         * 48-25-N/A
         *
         * [0] = landing single
         * [1] = temperature
         */
        preparedLog.add(
                getField(landingFields, 1)
        );

        /*
         * TAKEOFF data is not available during this
         * status message.
         */
        preparedLog.add("N/A");
        preparedLog.add("N/A");

        /*
         * LANDING:
         *
         * statusFields[2] = distance / average
         * landingFields[0] = single distance
         */
        preparedLog.add(
                getField(statusFields, 2)
        );

        preparedLog.add(
                getField(landingFields, 0)
        );

        updateLogs(preparedLog);
    }

    /**
     * Updates the status panel.
     */
    private void updateStatus(List<String> statusFields) {

        String prefix =
                "System Order(" +
                this.lineCounter +
                ") -> ";

        safeAppendLine(
                prefix +
                labels.get(0) +
                " " +
                getField(statusFields, 0) +
                "\n"
        );

        safeAppendLine(
                prefix +
                labels.get(1) +
                " " +
                getField(statusFields, 1) +
                "\n"
        );

        if (statusFields.size() > 2 &&
                !"LANDING".equalsIgnoreCase(
                        getField(statusFields, 1))) {

            safeAppendLine(
                    prefix +
                    labels.get(2) +
                    " " +
                    getField(statusFields, 2) +
                    "\n"
            );
        }

        if ("LANDING".equalsIgnoreCase(
                getField(statusFields, 1)) &&
                statusFields.size() > 2) {

            safeAppendLine(
                    prefix +
                    labels.get(4) +
                    " distance " +
                    getField(statusFields, 2) +
                    "\n"
            );
        }
    }

    /**
     * Updates the LOG panel.
     *
     * Expected format:
     *
     * [0] System
     * [1] Drone
     * [2] Temperature
     * [3] TakeOff average
     * [4] TakeOff single
     * [5] Landing average
     * [6] Landing single
     */
    private void updateLogs(List<String> logFields) {

        if (logFields == null || logFields.isEmpty()) {
            return;
        }

        String prefix = "Log -> ";

        List<String> preparedLog = new ArrayList<>();

        /*
         * SYSTEM
         */
        preparedLog.add(
                prefix +
                labels.get(0) +
                " " +
                getField(logFields, 0) +
                "\n"
        );

        /*
         * DRONE
         */
        preparedLog.add(
                prefix +
                labels.get(1) +
                " " +
                getField(logFields, 1) +
                "\n"
        );

        /*
         * TEMPERATURE
         */
        preparedLog.add(
                prefix +
                labels.get(2) +
                " " +
                getField(logFields, 2) +
                "\n"
        );

        /*
         * TAKEOFF
         */
        String takeoffAverage =
                getField(logFields, 3);

        //String takeoffSingle =
        //        getField(logFields, 4);

        if ("N/A".equalsIgnoreCase(takeoffAverage)) {

            preparedLog.add(
                    prefix +
                    labels.get(3) +
                    " N/A\n"
            );

        } else {

            preparedLog.add(
                    prefix +
                    labels.get(3) +
                    " Distanza Media Decollo " +
                    takeoffAverage +
                    /*" Distanza Singola " +
                    takeoffSingle +*/
                    "\n"
            );
        }

        /*
         * LANDING
         */
        String landingAverage =
                getField(logFields, 5);

        //String landingSingle =
        //        getField(logFields, 6);

        if ("N/A".equalsIgnoreCase(landingAverage)) {

            preparedLog.add(
                    prefix +
                    labels.get(4) +
                    " N/A\n"
            );

        } else {

            preparedLog.add(
                    prefix +
                    labels.get(4) +
                    " Distanza Media Atterraggio " +
                    landingAverage +
                    /*" Distanza Singola " +
                    landingSingle +*/
                    "\n"
            );
        }

        SwingUtilities.invokeLater(() -> {

            if (screenPanel != null) {

                screenPanel
                        .getLogsPanel()
                        .getScreenArea()
                        .setLines(preparedLog);
            }
        });
    }

    /**
     * Splits a message using '-'.
     */
    @SuppressWarnings("null")
    private List<String> splitByDash(String part) {

        if (part == null || part.isBlank()) {
            return new ArrayList<>();
        }

        return Arrays.stream(part.split("-"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Safely retrieves a field.
     */
    private String getField(
            List<String> fields,
            int index) {

        if (fields == null) {
            return "N/A";
        }

        if (index < 0 || index >= fields.size()) {
            return "N/A";
        }

        String value = fields.get(index);

        if (value == null || value.isBlank()) {
            return "N/A";
        }

        return value;
    }

    /**
     * Appends a line to the command screen.
     */
    private void safeAppendLine(String text) {

        SwingUtilities.invokeLater(() -> {

            if (screenPanel != null) {

                screenPanel
                        .getCommandsPanel()
                        .getScreenArea()
                        .appendLine(text);
            }
        });
    }

    public void updateLaunchOrder(OrderType order) {

        switch (order) {

            case LOGS:

                if (!logInProgress) {

                    currentOrder = order;
                    logInProgress = true;

                    logCounter = ++globalCounter;

                    safeAppendLine(
                            "System -> Sending order LOGS line " +
                            logCounter +
                            "\n"
                    );

                } else {

                    safeAppendLine(
                            "System -> LOG order denied: " +
                            "still waiting previous LOG\n"
                    );
                }

                break;

            case TAKE_OFF:
            case LANDING:

                if (!orderInProgress) {

                    currentOrder = order;
                    orderInProgress = true;

                    lineCounter = ++globalCounter;

                    safeAppendLine(
                            "System -> Sending order " +
                            order +
                            " line " +
                            lineCounter +
                            "\n"
                    );

                } else {

                    safeAppendLine(
                            "System -> " +
                            order +
                            " denied: " +
                            "previous order still in progress\n"
                    );
                }

                break;
        }
    }
}