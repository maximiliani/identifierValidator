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

import edu.kit.scc.dem.identifier_validator.exceptions.*;

/**
 * This interface provides a method which is necessary to validate things.
 */
public interface ValidatorInterface {

    /**
     * This method must be implemented by any implementation.
     * It validates an input and either returns true or throws an exception.
     *
     * @param input to validate
     * @return true if input is valid for the special type of implementation
     * @throws ValidationError   if the input is invalid and definitively unusable.
     * @throws ValidationWarning if there is a chance that the input could be valid. (e.g. Validation server not reachable. Additional information should be provided with logs and the exception message.
     */
    boolean isValid(String input) throws ValidationError, ValidationWarning;
}
