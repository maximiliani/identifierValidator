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

package edu.kit.datamanager.datacite.validate.exceptions;

/**
 * This class defines a custom exception for warnings during the validation process.
 * This exception is thrown when a result affects a subsequent method, but an error cannot be ruled out, for example, due to external circumstances that also apply for the calling method.
 * This case occurs, for example, when an input is to be checked using the Internet and no connection can be established, but based on a local possibility check (e.g. regex) the input seems possible.
 * A Validation Warning has always less importance than a ValidationError.
 *
 * @author maximilianiKIT
 */
public class ValidationWarning extends Exception {

    /**
     * This constructor expects an errorMessage and a Throwable for more information.
     *
     * @param errorMessage is a description about what happened.
     * @param err          is a Throwable from former errors like IllegalArgumentExceptions, which are not handled by the caller class.
     */
    public ValidationWarning(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    /**
     * This constructor expects an errorMessage for more information.
     *
     * @param errorMessage is a description about what happened.
     */
    public ValidationWarning(String errorMessage) {
        super(errorMessage);
    }

}
