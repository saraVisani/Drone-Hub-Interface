package it.unibo.util;

public class Enum {
    public enum PanelType {
        ORDERS,
        LOGS
    }

    public enum OrderType {

        TAKE_OFF(false),
        LANDING(false),
        LOGS(false);

        private boolean enabled;

        // costruttore dell'enum
        OrderType(boolean enabled) {
            this.enabled = enabled;
        }

        // getter
        public boolean isEnabled() {
            return enabled;
        }

        // utility
        public void enable() {
            this.enabled = true;
        }

        public void disable() {
            this.enabled = false;
        }

        public void toggle() {
            this.enabled = !this.enabled;
        }

        public static OrderType stringMatch(String input){
            if(input.equalsIgnoreCase("TAKE OFF")){
                return TAKE_OFF;
            } else if (input.equalsIgnoreCase("LANDING")){
                return LANDING;
            } else if (input.equalsIgnoreCase("LOGS")){
                return LOGS;
            }
            return null;
        }

        public static String orderMatch(OrderType input){
            if(input.equals(TAKE_OFF)){
                return "TAKE OFF";
            } else if (input.equals(LANDING)){
                return "LANDING";
            } else if (input.equals(LOGS)){
                return "LOGS";
            }
            return "";
        }
    }
}
