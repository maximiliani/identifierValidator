package edu.kit.datamanager.datacite.validate.website;

import edu.kit.datamanager.datacite.validate.GenericValidator;
import edu.kit.datamanager.datacite.validate.exceptions.ValidationError;
import edu.kit.datamanager.datacite.validate.exceptions.ValidationWarning;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ValidatorInputController {

    @GetMapping("")
    public String createProjectForm(Model model) {

        model.addAttribute("validatorInput", new ValidatorInput());
        model.addAttribute("types", GenericValidator.soleInstance().getListOfAvailableValidators());
        return "index";
    }

    @PostMapping("/validate")
    public String saveProjectSubmission(@ModelAttribute ValidatorInput validatorInput) {
        try {
            if (GenericValidator.soleInstance().isValid(validatorInput.input, validatorInput.type))
                validatorInput.valid = true;
        } catch (ValidationWarning e) {
            validatorInput.valid = false;
            validatorInput.message = "WARNING:   " + e.getMessage();
        } catch (ValidationError e) {
            validatorInput.valid = false;
            validatorInput.message = "ERROR:   " + e.getMessage();
        }
        return "result";
    }
}
