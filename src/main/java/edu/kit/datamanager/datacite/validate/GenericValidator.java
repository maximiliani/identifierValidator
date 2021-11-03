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

package edu.kit.datamanager.datacite.validate;

import edu.kit.datamanager.datacite.validate.exceptions.ValidationError;
import edu.kit.datamanager.datacite.validate.exceptions.ValidationWarning;
import edu.kit.datamanager.datacite.validate.impl.HandleNetValidator;
import edu.kit.datamanager.datacite.validate.impl.URLValidator;
import org.datacite.schema.kernel_4.RelatedIdentifierType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GenericValidator {

    static Logger LOG = LoggerFactory.getLogger(ValidatorInterface.class);
    private static final GenericValidator soleInstance = new GenericValidator();

    private static final Map<RelatedIdentifierType, ValidatorInterface> validators;

    static {
        Map<RelatedIdentifierType, ValidatorInterface> validators1;
        try {
            validators1 = PluginLoader.loadPlugins(new File("./plugins"));
        } catch (Exception e) {
            LOG.info("No plugins loaded.", e);
            validators1 = new HashMap<>();
        }
        validators1.put(RelatedIdentifierType.HANDLE, new HandleNetValidator());
        validators1.put(RelatedIdentifierType.DOI, new HandleNetValidator());
        validators1.put(RelatedIdentifierType.URL, new URLValidator());
        validators = validators1;
    }

    private GenericValidator() {
        // enforces singularity
    }

    public static GenericValidator soleInstance() {
        return soleInstance;
    }

    public boolean isValid(String input, RelatedIdentifierType type) throws ValidationWarning, ValidationError {
        if (validators.containsKey(type)){
            if (validators.get(type).isValid(input, type)) LOG.info("Valid input and valid input type!");
            return true;
        }
        else {
            LOG.warn("No matching validator found. Please check your input and plugins.");
            throw new ValidationWarning("No matching validator found. Please check your input and plugins.");
        }
    }

    public Map getValidators(){
        return validators;
    }

    public static void main(String[] args) {
        for (var entry: validators.entrySet()){
            System.out.println(entry.getValue().supportedType().toString());
        }
    }
}
