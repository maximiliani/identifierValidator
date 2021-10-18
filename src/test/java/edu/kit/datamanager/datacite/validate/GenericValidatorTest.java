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

import static org.junit.jupiter.api.Assertions.*;

class GenericValidatorTest {

    @Test
    void valid() {
        try {
            assertTrue(GenericValidator.soleInstance().isValid("https://kit.edu", RelatedIdentifierType.URL));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void invalidInputString() {
        try {
            assertFalse(GenericValidator.soleInstance().isValid("https://kit.example", RelatedIdentifierType.URL));
        } catch (ValidationError e) {
            fail(e);
        } catch (ValidationWarning e) {
        }
    }


    @Test
    void unimplementedType() {
        try {
            assertFalse(GenericValidator.soleInstance().isValid("https://kit.edu", RelatedIdentifierType.ARK));
        } catch (ValidationError e) {
            fail(e);
        } catch (ValidationWarning e) {
        }
    }

    @Test
    void main() {
        GenericValidator.soleInstance().main(new String[] {});
    }
}