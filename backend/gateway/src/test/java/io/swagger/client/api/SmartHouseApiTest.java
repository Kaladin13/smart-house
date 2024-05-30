/*
 * Smart house service
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.api;

import java.math.BigDecimal;
import io.swagger.client.model.CreateHouseRequest;
import io.swagger.client.model.CreateHouseResponse;
import io.swagger.client.model.CreateUserRequest;
import io.swagger.client.model.Devices;
import io.swagger.client.model.UserCreateResponse;
import org.junit.Test;
import org.junit.Ignore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * API tests for SmartHouseApi
 */
@Ignore
public class SmartHouseApiTest {

    private final SmartHouseApi api = new SmartHouseApi();

    /**
     * 
     *
     * 
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void createHouseTest() throws Exception {
        CreateHouseRequest body = null;
        CreateHouseResponse response = api.createHouse(body);

        // TODO: test validations
    }
    /**
     * 
     *
     * 
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void createUserTest() throws Exception {
        CreateUserRequest body = null;
        UserCreateResponse response = api.createUser(body);

        // TODO: test validations
    }
    /**
     * 
     *
     * 
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void deleteHouseTest() throws Exception {
        BigDecimal houseId = null;
        api.deleteHouse(houseId);

        // TODO: test validations
    }
    /**
     * 
     *
     * 
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void getAllowedDevicesTest() throws Exception {
        Devices response = api.getAllowedDevices();

        // TODO: test validations
    }
}