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

package edu.kit.datamanager.datacite.validate.impl;

import edu.kit.datamanager.datacite.validate.ValidatorInterface;
import edu.kit.datamanager.datacite.validate.exceptions.ValidationError;
import edu.kit.datamanager.datacite.validate.exceptions.ValidationWarning;
import org.datacite.schema.kernel_4.RelatedIdentifierType;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class URLValidator implements ValidatorInterface {
    @Override
    public RelatedIdentifierType supportedType() {
        return RelatedIdentifierType.URL;
    }

    /**
     * This method must be implemented by any implementation.
     * It validates an input and either returns true or throws an exception.
     *
     * @param input to validate
     * @param type  of the input
     * @return true if input is valid for the special type of implementation.
     * @throws ValidationError   if the input is invalid and definitively unusable.
     * @throws ValidationWarning if there is a chance that the input could be valid. (e.g. Validation server not reachable. Additional information should be provided with logs and the exception message.
     */
    @Override
    public boolean isValid(String input, RelatedIdentifierType type) throws ValidationError, ValidationWarning {
        if (type != supportedType()) {
            LOG.warn("Illegal type of validator");
            throw new ValidationWarning("Illegal type of Validator.");
        }

        URL urlHandler = null;
        HttpURLConnection con = null;
        LOG.debug("URL: {}", input);
        int status;
        try {
            urlHandler = new URL(input);
            con = (HttpURLConnection) urlHandler.openConnection();
            con.setRequestMethod("GET");
            status = con.getResponseCode();
            LOG.debug("HTTP status: {}", status);
            if (status != 200) {
                LOG.error("Invalid URL");
                throw new ValidationError("Invalid URL!");
            }
            return true;
        } catch (ProtocolException e) {
            LOG.warn("Error while setting request method");
            throw new ValidationWarning("Error setting request method", e);
        } catch (MalformedURLException e) {
            LOG.warn("Invalid URL");
            throw new ValidationError("Invalid URL", e);
        } catch (IOException e) {
            LOG.warn("IOException: Please check if you have internet access.");
            throw new ValidationWarning("IOException: Do you have an internet connection?", e);
        }
    }
}
