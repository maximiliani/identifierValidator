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

package edu.kit.datamanager.datacite.validate.website;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorInputTest {

    @Test
    public void emptyConstructor(){
        ValidatorInput validatorInput = new ValidatorInput();
        String expectedResult = "ValidatorInput{" +
                "type='" + "" + '\'' +
                ", input='" + "" + '\'' +
                ", message='" + "" + '\'' +
                ", valid=" + "false" +
                '}';
        assertEquals(validatorInput.toString(), expectedResult);
    }

    @Test
    public void twoParamConstructor(){
        ValidatorInput validatorInput = new ValidatorInput("URL", "https://test.example");
        String expectedResult = "ValidatorInput{" +
                "type='" + "URL" + '\'' +
                ", input='" + "https://test.example" + '\'' +
                ", message='" + "" + '\'' +
                ", valid=" + "false" +
                '}';
        assertEquals(validatorInput.toString(), expectedResult);
    }

    @Test
    public void fourParamConstructor(){
        ValidatorInput validatorInput = new ValidatorInput("URL", "https://test.example", "Hello World", true);
        String expectedResult = "ValidatorInput{" +
                "type='" + "URL" + '\'' +
                ", input='" + "https://test.example" + '\'' +
                ", message='" + "Hello World" + '\'' +
                ", valid=" + "true" +
                '}';
        assertEquals(validatorInput.toString(), expectedResult);
    }

    @Test
    public void testType(){
        ValidatorInput validatorInput = new ValidatorInput();
        validatorInput.setType("TYPE");
        assertEquals(validatorInput.getType(), "TYPE");
    }

    @Test
    public void testInput(){
        ValidatorInput validatorInput = new ValidatorInput();
        validatorInput.setInput("INPUT");
        assertEquals(validatorInput.getInput(), "INPUT");
    }

    @Test
    public void testMSG(){
        ValidatorInput validatorInput = new ValidatorInput();
        validatorInput.setMessage("MSG");
        assertEquals(validatorInput.getMessage(), "MSG");
    }
    @Test

    public void testValid(){
        ValidatorInput validatorInput = new ValidatorInput();
        validatorInput.setValid(true);
        assertTrue(validatorInput.getValid());
    }

//    public void getterTest(){
//        ValidatorInput validatorInput = new ValidatorInput();
//        assertEquals(validatorInput.getType(),"");
//        assertEquals(validatorInput.getInput(),"");
//        assertEquals(validatorInput.getMessage(),"");
//        assertEquals(validatorInput.getValid(),"false");
//    }
//
//    public void setterTest(){
//        ValidatorInput validatorInput = new ValidatorInput();
//        validatorInput.setType("1");
//        validatorInput.setInput("2");
//        validatorInput.setMessage("3");
//        validatorInput.setValid(true);
//        assertEquals(validatorInput.getType(),"1");
//        assertEquals(validatorInput.getInput(),"2");
//        assertEquals(validatorInput.getMessage(),"3");
//        assertEquals(validatorInput.getValid(),true);
//    }

}