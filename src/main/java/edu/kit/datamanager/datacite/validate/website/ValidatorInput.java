package edu.kit.datamanager.datacite.validate.website;

import java.io.Serializable;

public class ValidatorInput implements Serializable {

    public String type;
    public String input;
    public String message;
    public Boolean valid;

    public ValidatorInput(String type, String input, String message, Boolean valid) {
        this.type = type;
        this.input = input;
        this.message = message;
        this.valid = valid;
    }

    public ValidatorInput(String type, String input) {
        this.type = type;
        this.input = input;
        message = "";
        valid = false;
    }

    public ValidatorInput() {

    }

    @Override
    public String toString() {
        return "ValidatorInput{" +
                "type='" + type + '\'' +
                ", input='" + input + '\'' +
                ", reason='" + message + '\'' +
                ", valid=" + valid +
                '}';
    }
}
