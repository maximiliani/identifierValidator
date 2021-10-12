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

package edu.kit.scc.dem.identifier_validator.impl;

import edu.kit.scc.dem.identifier_validator.exceptions.ValidationError;
import edu.kit.scc.dem.identifier_validator.exceptions.ValidationWarning;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class HandleNetValidatorTest {

    HandleNetValidator validator = new HandleNetValidator();

    @Test
    void valid_isValid() {
        try {
            assertTrue(validator.isValid("hdl://10.1038/nphys1170"));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void invalid_isValid() {
        try {
            assertFalse(validator.isValid("10.1038/auifz8zhunjkad"));
        } catch (ValidationWarning e) {
            fail(e);
        } catch (ValidationError e) {
            ;
        }
    }

    @Test
    void invalidPrefix_isValid() {
        try {
            assertFalse(validator.isValid("testdgsdfg/auifz8zhunjkad"));
        } catch (ValidationWarning e) {
            fail(e);
        } catch (ValidationError e) {
            ;
        }
    }

    @Test
    void valid_isValidHTTP() {
        try {
            assertTrue(validator.isValid("http://hdl.handle.net/api/handles/10.1038/nphys1170"));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void valid_isValidHTTPS() {
        try {
            assertTrue(validator.isValid("https://hdl.handle.net/api/handles/10.1038/nphys1170"));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void invalid_isValidHTTPURL() {
        try {
            assertFalse(validator.isValid("https://google.com"));
        } catch (ValidationWarning e) {
            fail(e);
        } catch (ValidationError e) {
            ;
        }
    }

    @Test
    void valid_isValidHandle() {
        try {
            assertTrue(validator.isValidHandle("10.1038/nphys1170"));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void invalid_isValidHandleScheme() {
        try {
            assertFalse(validator.isValidHandle("test"));
        } catch (ValidationWarning e) {
            fail(e);
        } catch (ValidationError e) {
            ;
        }
    }

    @Test
    void valid_isDownloadable() {
        try {
            assertTrue(validator.isDownloadable("http://hdl.handle.net/api/handles", "10.1038", "nphys1170"));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void invalidURL_isDownloadable() {
        try {
            assertFalse(validator.isDownloadable("hdl.handle", "10.1038", "nphys1170"));
        } catch (ValidationWarning e) {
            ;
        }
    }

    @Test
    void serverNotReachable_isDownloadable() {
        try {
            assertFalse(validator.isDownloadable("https://hdl.test.example", "10.1038", "nphys1170"));
        } catch (ValidationWarning e) {
            ;
        }
    }

    @Test
    void invalidPrefix_isDownloadable() {
        try {
            assertFalse(validator.isDownloadable("http://hdl.handle.net/api/handles", "10.10385", "nphys1170"));
        } catch (ValidationWarning e) {
            ;
        }
    }

    @Test
    void invalidSuffix_isDownloadable() {
        try {
            assertFalse(validator.isDownloadable("10.1038", "nphys1170.345678"));
        } catch (ValidationWarning e) {
            ;
        }
    }

    @Test
    void invalidCharacters_isDownloadable() {
        try {
            assertFalse(validator.isDownloadable("http://google.com", "®¡“¢∂‚", "®¡“¢∂‚"));
        } catch (ValidationWarning e) {
            ;
        }
    }
}