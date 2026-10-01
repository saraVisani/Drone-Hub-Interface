package it.unibo.controller.impl;

import java.util.ArrayDeque;
import java.util.Queue;
import jssc.*;

public class SerialChannel {
    private SerialPort serialPort;
    private StringBuilder buffer = new StringBuilder();
    private Queue<String> messages = new ArrayDeque<>();

    public SerialChannel(String portName, int baudRate)
            throws SerialPortException {

        serialPort = new SerialPort(portName);
        serialPort.openPort();
        serialPort.setParams(
                baudRate,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE
        );
    }

    /** chiamata periodicamente dal Timer Swing */
    public void update() throws SerialPortException {

        int available = serialPort.getInputBufferBytesCount();
        if (available <= 0) return;

        String data = serialPort.readString(available).replace("\r", "");

        for (char c : data.toCharArray()) {
            if (c == '\n') {
                messages.add(buffer.toString());
                buffer.setLength(0);
            } else {
                buffer.append(c);
            }
        }
    }

    public boolean hasMsg() {
        return !messages.isEmpty();
    }

    public String readMsg() {
        return messages.poll();
    }

    public void send(String msg) throws SerialPortException {
        if(msg.isEmpty() && !serialPort.isOpened()) return ;
        serialPort.writeBytes((msg + "\n").getBytes());
    }

    public void close() throws SerialPortException {
        if (serialPort != null && serialPort.isOpened()) {
            serialPort.closePort();
        }
    }
}
