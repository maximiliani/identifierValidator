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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RESTAPIControllerTest {

    @Test
    void validDOIInput() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String url = "http://localhost:8080/api/validate?type=DOI&input=0.NA/10.1038";
        String output = "Valid Input!";

        ResponseEntity<String> productEntity
                = restTemplate.getForEntity(url, String.class);
        Assertions.assertEquals(productEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertNotNull(productEntity.getBody());
        Assertions.assertEquals(productEntity.getBody(), output);
    }

    @Test
    void validatorsAvailable() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String url = "http://localhost:8080/api";

        ResponseEntity<String> productEntity
                = restTemplate.getForEntity(url, String.class);
        Assertions.assertEquals(productEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertNotNull(productEntity.getBody());
    }

    @Test
    void partiallyValidDOIInput() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String url = "http://localhost:8080/api/validate?type=DOI&input=0.NA/10.10.38";
        String output = "Prefix valid, but suffix not";

        ResponseEntity<String> productEntity
                = restTemplate.getForEntity(url, String.class);
        Assertions.assertEquals(productEntity.getStatusCode(), HttpStatus.ACCEPTED);
        Assertions.assertNotNull(productEntity.getBody());
        Assertions.assertEquals(productEntity.getBody(), output);
    }

    @Test
    void invalidInput() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String url = "http://localhost:8080/api/validate?type=DOI&input=test";
        String output = "Invalid input";

        ResponseEntity<String> productEntity
                = restTemplate.getForEntity(url, String.class);
        Assertions.assertEquals(productEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertNotNull(productEntity.getBody());
        Assertions.assertEquals(productEntity.getBody(), output);
    }

    @Test
    void invalidType() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String url = "http://localhost:8080/api/validate?type=test&input=test";
        String output = "Invalid Type!";

        ResponseEntity<String> productEntity
                = restTemplate.getForEntity(url, String.class);
        Assertions.assertEquals(productEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertNotNull(productEntity.getBody());
        Assertions.assertEquals(productEntity.getBody(), output);
    }

    @Test
    void noParams() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String url = "http://localhost:8080/api/validate";

        ResponseEntity<String> productEntity
                = restTemplate.getForEntity(url, String.class);
        Assertions.assertEquals(productEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertNotNull(productEntity.getBody());
    }

    @Test
    void onlyInputParam() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String url = "http://localhost:8080/api/validate?input=http://google.com";

        ResponseEntity<String> productEntity
                = restTemplate.getForEntity(url, String.class);
        Assertions.assertEquals(productEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertNotNull(productEntity.getBody());
    }

    @Test
    void onlyTypeParam() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String url = "http://localhost:8080/api/validate?type=URL";

        ResponseEntity<String> productEntity
                = restTemplate.getForEntity(url, String.class);
        Assertions.assertEquals(productEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertNotNull(productEntity.getBody());
    }
}