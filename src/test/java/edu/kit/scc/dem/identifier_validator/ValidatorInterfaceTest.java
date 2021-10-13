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

package edu.kit.scc.dem.identifier_validator;

import edu.kit.datamanager.datacite.validate.ValidatorInterface;
import edu.kit.datamanager.datacite.validate.exceptions.ValidationError;
import edu.kit.datamanager.datacite.validate.exceptions.ValidationWarning;
import edu.kit.datamanager.datacite.validate.impl.HandleNetValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorInterfaceTest {

    ValidatorInterface validatorInterface = new HandleNetValidator();

    @Test
    void valid_isValid() {
        try {
            validatorInterface.isValid("hdl://10.1038/nphys1170");
        } catch (ValidationError | ValidationWarning e) {
            fail(e);
        }
    }


    @Test
    void valid_isDownloadable() {
        try {
            assertTrue(validatorInterface.getURLStatus("http://hdl.handle.net/api/handles/10.1038/nphys1170") == 200);
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void invalidURL_isDownloadable() {
        try {
            assertFalse(validatorInterface.getURLStatus("hdl.handle/10.1038/nphys1170") == 200);
        } catch (ValidationWarning e) {
            ;
        }
    }

    @Test
    void serverNotReachable_isDownloadable() {
        try {
            assertFalse(validatorInterface.getURLStatus("https://hdl.test.example/10.1038/nphys1170") == 200);
        } catch (ValidationWarning e) {
            ;
        }
    }

    @Test
    void invalidPrefix_isDownloadable() {
        try {
            assertFalse(validatorInterface.getURLStatus("http://hdl.handle.net/api/handles/10.10385/nphys1170") == 200);
        } catch (ValidationWarning e) {
            ;
        }
    }

    @Test
    void invalidSuffix_isDownloadable() {
        try {
            assertFalse(validatorInterface.getURLStatus("http://hdl.handle.net/api/handles/10.1038/nphys1170.345678") == 200);
        } catch (ValidationWarning e) {
            ;
        }
    }

    @Test
    void invalidCharacters_isDownloadable() {
        try {
            assertFalse(validatorInterface.getURLStatus("http://google.com/®¡“¢∂‚/®¡“¢∂‚") == 200);
        } catch (ValidationWarning e) {
            ;
        }
    }
}