package com.ge.mvc.web.modules;

import java.io.IOException;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ge.mvc.web.modules.SpringService;

/*
 * Controller class for the Spring MVC Predix template
 * @author Kyle Duckworth (212326570)
 * @version 20.1.2017
 */
@Controller
//@RequestMapping(value = "/springmvc-helloworld")
class SpringController
{
	// Autowire the service object to execute logic
	@Autowired
    @Qualifier("SpringService")
    private SpringService springService;

	// Logger can be used for debugging
    private static final Logger logger = LoggerFactory.getLogger(SpringController.class);

    /*
     * Get the authentication token from the UAA service
     * @return String The authentication token
     */
    @RequestMapping(value = "/getAuthToken", method = {RequestMethod.GET},produces = "text/html")
    public @ResponseBody String getAuthToken() throws IOException, JSONException
    {
    	// Invoke the service to return the token
    	return springService.getAuthToken("https://98d4176d-a268-405b-a461-3f9944793a31.predix-uaa.run.aws-usw02-pr.ice.predix.io/oauth/token");
    	
    }
    
    /*
     * Get the JSON object from the time series service
     * @return Data stored in timeseries table
     */
    @RequestMapping(value = "/getData", method = {RequestMethod.POST},produces = "text/html")
    public @ResponseBody String getData() throws IOException, JSONException
    {
    	// Invoke the service to return the time series data
    	return springService.getData("https://time-series-store-predix.run.aws-usw02-pr.ice.predix.io/v1/datapoints");
    }


}
