var request 		= require('request');
/*
 * API route to get time series data
 * @author Kyle Duckworth (212326570)
 * @version 01.02.2017
 */
exports.getKpi = function(req, res){
	// Time series URL to query datapoints
    var timeseries_query_uri 	= 'https://time-series-store-predix.run.aws-usw02-pr.ice.predix.io/v1/datapoints';
	// Predix zone ID for Waukesha Engine data
    var timeseries_zone_id 		= 'e3fba85e-d334-409e-87ce-3a17e71b4946';
	// JSON Body for request
    var json_data =
    {
		// Data points needs start and end time in MS
		// Set the start time to 0 (Get all data)
        "start": 0000000000000,
		// Set the end time to the current tiem
        "end": (new Date).getTime(), 
		// Get the KPI name passed in the request
        "tags": [
            {
                "name": req.params['kpiName'],
                "order": "desc"
            }
        ]
	};

    // options for db call
    var options = {
		// Post request to get the data
        method: 'POST',
		// URL for time series query (generic, defined above)
        url: timeseries_query_uri,
		// Set the headers with the necessary parameters
        headers : {
            'Accept': 'application/json, application/x-www-form-urlencoded',
            'Authorization': 'Bearer ' + req.params['token'],
            'Predix-Zone-Id': timeseries_zone_id
        },
		// Body of request
        json : json_data
    };

    // call to db, if successful, return data as json for client
    request(options, function (error, response, body) {
		// If successfull request
        if (!error && response.statusCode == 200) {
            // Send the response back to the requester
            res.send(response.body);
		}
		// Not successful request
        else{
			// Send erro back to the requester
            res.send(error);
        }
    });

    //req.query contains the url queries, can be used to get specific time data down the line
    //by modifying the start / end key/value pairs in json_data below
    console.log(req.query);

};

