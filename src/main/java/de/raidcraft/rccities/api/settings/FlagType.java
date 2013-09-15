package de.raidcraft.rccities.api.settings;

/**
 * @author Philip Urban
 */
public enum FlagType {

    STRING("Es wurde Text erwartet!"),
    INTEGER("Es wurde eine Ganzzahl erwartet!"),
    DOUBLE("Es wurde eine Kommazahl erwartet!"),
    BOOLEAN("Es wurde ein Boolean (wahr/falsch) erwartet!");

    private String errorMsg;

    private FlagType(String errorMsg) {

        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {

        return errorMsg;
    }

    public boolean validate(String input) {

        if(this == INTEGER) {
            try {
                Integer.valueOf(input);
                return true;
            }
            catch(NumberFormatException e) {
                return false;
            }
        }
        else if(this == DOUBLE) {
            try {
                Double.valueOf(input);
                return true;
            }
            catch(NumberFormatException e) {
                return false;
            }
        }
        else if(this == BOOLEAN) {
            if(input.equalsIgnoreCase("true")
                    || input.equalsIgnoreCase("allow")
                    || input.equalsIgnoreCase("deny")
                    || input.equalsIgnoreCase("false")
                    || input.equalsIgnoreCase("wahr")
                    || input.equalsIgnoreCase("falsch")
                    || input.equalsIgnoreCase("ja")
                    || input.equalsIgnoreCase("nein")
                    || input.equalsIgnoreCase("1")
                    || input.equalsIgnoreCase("0")) {
                return true;
            }
            else {
                return false;
            }
        }

        return true;
    }

    public boolean convertToBoolean(String value) {

        if(value.equalsIgnoreCase("true")
                || value.equalsIgnoreCase("allow")
                || value.equalsIgnoreCase("wahr")
                || value.equalsIgnoreCase("ja")
                || value.equalsIgnoreCase("1")) {
            return true;
        }
        else {
            return false;
        }
    }

    public int convertToInteger(String value) {

        int result = 0;
        try {
            result = Integer.valueOf(value);
        }
        catch(NumberFormatException e) {}
        return result;
    }

    public double convertToDouble(String value) {

        double result = 0;
        try {
            result = Double.valueOf(value);
        }
        catch(NumberFormatException e) {}
        return result;
    }

    public Object convert(String input) {

        Object converted = input;

        if(this == INTEGER) {
            try {
                converted = Integer.valueOf(input);
            }
            catch(NumberFormatException e) {
                return null;
            }
        }
        else if(this == DOUBLE) {
            try {
                converted = Double.valueOf(input);
            }
            catch(NumberFormatException e) {
                return null;
            }
        }
        else if(this == BOOLEAN) {
            if(input.equalsIgnoreCase("true")
                    || input.equalsIgnoreCase("wahr")
                    || input.equalsIgnoreCase("ja")
                    || input.equalsIgnoreCase("1")) {
                return true;
            }
            else if(input.equalsIgnoreCase("false")
                    || input.equalsIgnoreCase("falsch")
                    || input.equalsIgnoreCase("nein")
                    || input.equalsIgnoreCase("0")) {
                return false;
            }
            else {
                return null;
            }
        }

        return converted;
    }
}
