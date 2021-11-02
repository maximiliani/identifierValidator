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
import org.datacite.schema.kernel_4.RelatedIdentifierType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PluginLoaderTest {

    PluginLoader loader =  new PluginLoader();

    @Test
    void valid() {
        Map<RelatedIdentifierType, ValidatorInterface> validators = null;
        try {
            validators = PluginLoader.loadPlugins(new File("./plugins"));
        } catch (IOException | ValidationWarning e) {
            fail(e);
        }
        for (var entry: validators.entrySet()){
            System.out.println(entry.getValue().supportedType().toString());
        }
        try {
            System.out.println(validators.get(RelatedIdentifierType.ISBN).isValid("9783104996790"));
        } catch (ValidationError | ValidationWarning e) {
            fail(e);
        }
    }

    @Test
    void invalidPath(){
        Map<RelatedIdentifierType, ValidatorInterface> validators = null;
        try {
            PluginLoader.loadPlugins(new File("./invalid/test"));
        } catch (IOException e) {
            fail(e);
        } catch (ValidationWarning validationWarning) {
        }
    }

    @Test
    void invalidPlugin() {
        Map<RelatedIdentifierType, ValidatorInterface> validators = null;
        try {
            validators = PluginLoader.loadPlugins(new File("./invalid_plugins"));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void nullInput() {
        Map<RelatedIdentifierType, ValidatorInterface> validators = null;
        try {
            validators = PluginLoader.loadPlugins(null);
        } catch (IOException e) {
            fail(e);
        } catch (ValidationWarning validationWarning) {
        }
    }
    @Test
    void emptyinput() {
        Map<RelatedIdentifierType, ValidatorInterface> validators = null;
        try {
            validators = PluginLoader.loadPlugins(new File(""));
        } catch (IOException e) {
            fail(e);
        } catch (ValidationWarning validationWarning) {
        }
    }
}