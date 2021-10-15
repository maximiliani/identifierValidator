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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class validates Handles with help of handle.net.
 */
public class HandleNetValidator implements ValidatorInterface {

    Logger log = LoggerFactory.getLogger(HandleNetValidator.class);

    /**
     * This method returns the type of the validator implementation.
     *
     * @return element of the enum defined in the Datacite schema.
     */
    @Override
    public RelatedIdentifierType supportedType() {
        return RelatedIdentifierType.HANDLE;
    }

    /**
     * This method must be implemented by any implementation.
     * It validates an input and either returns true or throws an exception.
     *
     * @param input to validate
     * @param type  of the input
     * @return true if input is valid for the special type of implementation.
     * @throws ValidationError   if the input is invalid and definitively unusable.
     * @throws ValidationWarning if there is a chance that the input could be valid. (e.g. Validation server not reachable. Additional information should be provided with logs and the exception message).
     */
    @Override
    public boolean isValid(String input, RelatedIdentifierType type) throws ValidationError, ValidationWarning {
        if (type != supportedType()) {
            LOG.warn("Illegal type of validator");
            throw new ValidationWarning("Illegal type of Validator.");
        }

        String regex = "^(hdl://|http://|https://|doi:)(.+)";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            if (matcher.group(1).equals("https://") || matcher.group(1).equals("http://"))
                return isValidHTTPURL(matcher.group(0));
            else return isValidHandle(matcher.group(2));
        } else return isValidHandle(input);
    }

    /**
     * This method checks if the given prefix and suffix is downloadable.
     * If there is some invalid input, it will validate the prefix with help of the RESTful API by handle.net.
     *
     * @param serverAddress from the server which should be uses for the validation.
     * @param prefix        the handle prefix
     * @param suffix        the handle suffix
     * @return true if the record is downloadable
     * @throws ValidationWarning if an error occurs (e.g. no internet connection, invalid prefix or suffix). For further information read the error message or the log.
     */
    private boolean isDownloadable(String serverAddress, String prefix, String suffix) throws ValidationWarning, ValidationError {
        ValidatorInterface urlValidator = new URLValidator();
        AtomicBoolean fullValid = new AtomicBoolean(false);
        log.debug("Server address: {}", serverAddress);
        log.debug("Prefix: {}", prefix);
        log.debug("Suffix: {}", suffix);
        ignoringExc(() -> {
            try {
                fullValid.set(urlValidator.isValid(serverAddress + "/" + prefix + "/" + suffix));
            } catch (ValidationError | ValidationWarning ignored) {
            }
        });
        if (fullValid.get()) {
            LOG.info("The handle is valid!");
            return true;
        }
        log.warn("Either the suffix or the prefix might be invalid. Proving if prefix is valid...");
        if (urlValidator.isValid("https://hdl.handle.net/0.NA/" + prefix)) {
            log.info("The prefix {} is valid!", prefix);
            throw new ValidationWarning("Prefix valid, but suffix not");
        } else {
            log.error("The entered prefix is invalid!");
            throw new ValidationError("Prefix not provable on handle.net");
        }
    }

    /**
     * Uses the other isDownloadable method with the handle.net server as serverAddress
     *
     * @param prefix the handle prefix
     * @param suffix the handle suffix
     * @return true if the record is downloadable
     * @throws ValidationWarning if an error occurs (e.g. no internet connection, invalid prefix or suffix). For further information read the error message or the log.
     */
    private boolean isDownloadable(String prefix, String suffix) throws ValidationWarning, ValidationError {
        return isDownloadable("http://hdl.handle.net/api/handles", prefix, suffix);
    }

    /**
     * This method uses regex to get the prefix, suffix and the server address out of a http or https URL.
     * After this it uses the isDownloadable method to check whether the input is downloadable.
     *
     * @param url to validate
     * @return true if the input is valid and downloadable.
     * @throws ValidationWarning from isDownloadable. Contains further information in the message or in the log.
     * @throws ValidationError   if the input is invalid.
     */
    private boolean isValidHTTPURL(String url) throws ValidationWarning, ValidationError {
        String regex = "(http|https)://(.+)/([A-Za-z0-9.]+)/([A-Za-z0-9.]+)";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return isDownloadable((matcher.group(1) + "://" + matcher.group(2)), matcher.group(3), matcher.group(4));
        } else throw new ValidationError("Invalid input");
    }

    /**
     * This method uses regex to check whether unallowed characters are used and to extract the prefix and suffix.
     * After this it uses the isDownloadable method to check whether the input is downloadable.
     *
     * @param handle to validate
     * @return true if the input is valid and downloadable.
     * @throws ValidationWarning from isDownloadable. Contains further information in the message or in the log.
     * @throws ValidationError   if the input is invalid.
     */
    private boolean isValidHandle(String handle) throws ValidationWarning, ValidationError {
        String regex = "([A-Za-z0-9.]+)/([A-Za-z0-9.]+)";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(handle);
        if (matcher.find()) {
            return isDownloadable(matcher.group(1), matcher.group(2));
        } else throw new ValidationError("Invalid input");
    }

    private static void ignoringExc(Runnable r) {
        try {
            r.run();
        }
        catch (Exception ignored) {
        }
    }
}
