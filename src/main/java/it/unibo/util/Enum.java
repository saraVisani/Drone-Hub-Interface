package it.unibo.util;

public class Enum {
    public enum PanelType {
        ORDERS,
        LOGS
    }

    public enum RequestType {

        TAKE_OFF(false),
        LANDING(false),
        LOGS(false);

        private boolean enabled;

        // costruttore dell'enum
        RequestType(boolean enabled) {
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
    }
}
