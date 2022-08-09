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
        type = "";
        input = "";
        message = "";
        valid = false;
    }

    @Override
    public String toString() {
        return "ValidatorInput{" +
                "type='" + type + '\'' +
                ", input='" + input + '\'' +
                ", message='" + message + '\'' +
                ", valid=" + valid +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }
}
