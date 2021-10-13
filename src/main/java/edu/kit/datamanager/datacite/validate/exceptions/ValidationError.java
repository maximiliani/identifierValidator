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
 * This class defines a custom exception for errors during the validation process.
 * An error in this case can be defined as a malfunction that significantly affects the subsequent work with the return value of a function (e.g. an invalid input).
 * If a ValidationError is thrown, this ValidationError has a higher importance than a ValidationWarning.
 *
 * @author maximilianiKIT
 */
public class ValidationError extends Exception {

    /**
     * This constructor expects an errorMessage and a Throwable for more information.
     *
     * @param errorMessage is a description about what happened.
     * @param err          is a Throwable from former errors like IllegalArgumentExceptions, which are not handled by the caller class.
     */
    public ValidationError(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    /**
     * This is an empty constructor with no additional information.
     */
    public ValidationError() {
        super();
    }

}
