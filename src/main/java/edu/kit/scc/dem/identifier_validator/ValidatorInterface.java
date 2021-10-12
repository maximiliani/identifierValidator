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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * This interface provides a method which is necessary to validate things.
 */
public interface ValidatorInterface {

    Logger log = LoggerFactory.getLogger(ValidatorInterface.class);

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

    /**
     * This method checks if a URL is available.
     *
     * @param url to check
     * @return HTTP status code (e.g. 200 for OK)
     * @throws ValidationWarning if there is an error (e.g. server not reachable).
     */
    default int getURLStatus(String url) throws ValidationWarning {
        URL urlHandler = null;
        HttpURLConnection con = null;
        int status = 0;
        try {
            urlHandler = new URL(url);
            con = (HttpURLConnection) urlHandler.openConnection();
            con.setRequestMethod("GET");
            status = con.getResponseCode();
        } catch (ProtocolException e) {
            log.warn("Error while setting request method");
            throw new ValidationWarning("Error setting request method", e);
        } catch (MalformedURLException e) {
            log.warn("Invalid URL");
            throw new ValidationWarning("Invalid URL", e);
        } catch (IOException e) {
            log.warn("IOException: Please check if you have internet access.");
            throw new ValidationWarning("IOException: Do you have an internet connection?", e);
        }
        return status;
    }
}
