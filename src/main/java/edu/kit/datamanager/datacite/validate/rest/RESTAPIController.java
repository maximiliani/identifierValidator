/*
 * Copyright 2021 Karlsruhe Institute of Technology.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.kit.datamanager.datacite.validate.rest;

import edu.kit.datamanager.datacite.validate.GenericValidator;
import edu.kit.datamanager.datacite.validate.exceptions.ValidationError;
import edu.kit.datamanager.datacite.validate.exceptions.ValidationWarning;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RESTAPIController {

    @GetMapping("")
    public List<String> getAvailableValidators() {
        return GenericValidator.soleInstance().getListOfAvailableValidators();
    }

    @GetMapping("/validate")
    @ResponseBody
    public ResponseEntity<String> isValid(@RequestParam(name = "type") String type, @RequestParam String input) {
        try {
            if (GenericValidator.soleInstance().isValid(input, type))
                return new ResponseEntity<>("Valid Input!", HttpStatus.OK);
        } catch (ValidationWarning e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ValidationError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
