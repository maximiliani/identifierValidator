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

import edu.kit.scc.dem.identifier_validator.ValidatorInterface;
import edu.kit.scc.dem.identifier_validator.exceptions.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class validates Handles with help of handle.net.
 */
public class HandleNetValidator implements ValidatorInterface {

    Logger log = LoggerFactory.getLogger(HandleNetValidator.class);

    /**
     * This method checks if a special schema is defined, removes it in some cases and validates it with another method.
     *
     * @return true if the input is valid.
     * @throws ValidationError   if the input is invalid.
     * @throws ValidationWarning if there was an internal error, which doesn't mean directly that the input is invalid.
     */
    @Override
    public boolean isValid(String input) throws ValidationWarning, ValidationError {
        String regex = "^(hdl|http|https)\\:\\/\\/(.+)";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            if (matcher.group(1).equals("https") || matcher.group(1).equals("http"))
                return isValidHTTPURL(matcher.group(0));
            else return isValidHandle(matcher.group(2));
        } else throw new ValidationError("Invalid input", new ValidationError());
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
    public boolean isDownloadable(String serverAddress, String prefix, String suffix) throws ValidationWarning {
        URL url = null;
        HttpURLConnection con = null;
        int status = 0;

        try {
            url = new URL(serverAddress + "/" + prefix + "/" + suffix);
            con = (HttpURLConnection) url.openConnection();
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

        if (status != 200) {
            log.warn("Either the suffix or the prefix might be invalid. Proving if prefix is valid...");
            try {
                url = new URL("https://hdl.handle.net/0.NA" + prefix);
                con = (HttpURLConnection) url.openConnection();
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
            if (status != 200) {
                log.error("The entered prefix is invalid!");
                throw new ValidationWarning("Prefix not provable on handle.net", new ValidationError());
            }
        }
        log.info("The prefix and suffix are valid.");
        return true;
    }

    /**
     * Uses the other isDownloadable method with the handle.net server as serverAddress
     *
     * @param prefix the handle prefix
     * @param suffix the handle suffix
     * @return true if the record is downloadable
     * @throws ValidationWarning if an error occurs (e.g. no internet connection, invalid prefix or suffix). For further information read the error message or the log.
     */
    public boolean isDownloadable(String prefix, String suffix) throws ValidationWarning {
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
    public boolean isValidHTTPURL(String url) throws ValidationWarning, ValidationError {
        String regex = "(http|https)\\:\\/\\/(.+)\\/([A-Za-z0-9.]+)\\/([A-Za-z0-9.]+)";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return isDownloadable((matcher.group(1) + "://" + matcher.group(2)), matcher.group(3), matcher.group(4));
        } else throw new ValidationError("Invalid input", new ValidationError());
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
    public boolean isValidHandle(String handle) throws ValidationWarning, ValidationError {
        String regex = "([A-Za-z0-9.]+)\\/([A-Za-z0-9.]+)";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(handle);
        if (matcher.find()) {
            return isDownloadable(matcher.group(1), matcher.group(2));
        } else throw new ValidationError("Invalid input", new ValidationError());
    }
}
