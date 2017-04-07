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
		"start": "1y-ago",
		"tags": [
			{
				"name": req.params['kpiName'],
				"order": "desc",
				"limit": 100
			}
		]
	};

    var token = 'eyJhbGciOiJSUzI1NiIsImtpZCI6ImxlZ2FjeS10b2tlbi1rZXkiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiI5MjBlMjY0NjcxMGI0OTNiOGM0Y2I2NDA4MjFlNWU3NiIsInN1YiI6ImFwcF9jbGllbnRfaWQiLCJzY29wZSI6WyJ0aW1lc2VyaWVzLnpvbmVzLjcyNmIxNWYwLTNjM2QtNGRjMi04MzdmLTg2ZjQ4MmMwMzRlMi5pbmdlc3QiLCJhY3MucG9saWNpZXMud3JpdGUiLCJ1YWEucmVzb3VyY2UiLCJvcGVuaWQiLCJ0aW1lc2VyaWVzLnpvbmVzLmUzZmJhODVlLWQzMzQtNDA5ZS04N2NlLTNhMTdlNzFiNDk0Ni5xdWVyeSIsInRpbWVzZXJpZXMuem9uZXMuZTNmYmE4NWUtZDMzNC00MDllLTg3Y2UtM2ExN2U3MWI0OTQ2LnVzZXIiLCJhY3MuYXR0cmlidXRlcy53cml0ZSIsInByZWRpeC1hc3NldC56b25lcy5jYmJlNWUxZS1kMmU3LTQ0YWEtOGU2NS03YmE5YTUwNDY4NTUudXNlciIsInRpbWVzZXJpZXMuem9uZXMuNDFjYTEwY2MtNWQzMi00YTJlLWJkMzUtMDM3MGQ3NjI0NGI4LmluZ2VzdCIsInByZWRpeC1hY3Muem9uZXMuMGY1ZDE5YjgtMTMwNS00NDk0LThiZjktYmM5NmY1NTc3ZjFmLnVzZXIiLCJ0aW1lc2VyaWVzLnpvbmVzLjQxY2ExMGNjLTVkMzItNGEyZS1iZDM1LTAzNzBkNzYyNDRiOC5xdWVyeSIsImFjcy5wb2xpY2llcy5yZWFkIiwidGltZXNlcmllcy56b25lcy5lM2ZiYTg1ZS1kMzM0LTQwOWUtODdjZS0zYTE3ZTcxYjQ5NDYuaW5nZXN0IiwidGltZXNlcmllcy56b25lcy43MjZiMTVmMC0zYzNkLTRkYzItODM3Zi04NmY0ODJjMDM0ZTIudXNlciIsInByZWRpeC1hc3NldC56b25lcy4xMjExMmFlNy1hYjM2LTQyNzgtODgxOC1jODA2MWEzZTFiNmIudXNlciIsImFjcy5hdHRyaWJ1dGVzLnJlYWQiLCJwcmVkaXgtYWNzLnpvbmVzLmNjMWYyOThkLWU4NWYtNGMwMi05YTY2LWZmOTJjNjBjNzliNC51c2VyIiwidWFhLm5vbmUiLCJ0aW1lc2VyaWVzLnpvbmVzLjcyNmIxNWYwLTNjM2QtNGRjMi04MzdmLTg2ZjQ4MmMwMzRlMi5xdWVyeSIsInByZWRpeC1hY3Muem9uZXMuNjhmNzI2NzgtYzllYy00ZjgxLTk2MmQtYjNiZmQ3YWYyN2QyLnVzZXIiLCJwcmVkaXgtYWNzLnpvbmVzLmJlNWM2MmZjLWRkZWItNDA4MC04ZjI0LTI1OGJjYmU5YjYyMy51c2VyIiwicHJlZGl4LWFzc2V0LnpvbmVzLmZlYmMyMTI3LWY5MDctNDhlMS1iYzI1LTNiMmY3ZjkwMjZhYS51c2VyIiwidGltZXNlcmllcy56b25lcy40MWNhMTBjYy01ZDMyLTRhMmUtYmQzNS0wMzcwZDc2MjQ0YjgudXNlciIsInByZWRpeC1hc3NldC56b25lcy5iODYwOWViNy1iZWU5LTQwYWYtYTkwNy03ZGQ0Yzk0ZTNmODgudXNlciJdLCJjbGllbnRfaWQiOiJhcHBfY2xpZW50X2lkIiwiY2lkIjoiYXBwX2NsaWVudF9pZCIsImF6cCI6ImFwcF9jbGllbnRfaWQiLCJncmFudF90eXBlIjoiY2xpZW50X2NyZWRlbnRpYWxzIiwicmV2X3NpZyI6IjEzNGI2ODlmIiwiaWF0IjoxNDkxNTg0MzAwLCJleHAiOjE0OTE2Mjc1MDAsImlzcyI6Imh0dHBzOi8vOThkNDE3NmQtYTI2OC00MDViLWE0NjEtM2Y5OTQ0NzkzYTMxLnByZWRpeC11YWEucnVuLmF3cy11c3cwMi1wci5pY2UucHJlZGl4LmlvL29hdXRoL3Rva2VuIiwiemlkIjoiOThkNDE3NmQtYTI2OC00MDViLWE0NjEtM2Y5OTQ0NzkzYTMxIiwiYXVkIjpbInByZWRpeC1hc3NldC56b25lcy5iODYwOWViNy1iZWU5LTQwYWYtYTkwNy03ZGQ0Yzk0ZTNmODgiLCJwcmVkaXgtYWNzLnpvbmVzLmJlNWM2MmZjLWRkZWItNDA4MC04ZjI0LTI1OGJjYmU5YjYyMyIsInRpbWVzZXJpZXMuem9uZXMuNzI2YjE1ZjAtM2MzZC00ZGMyLTgzN2YtODZmNDgyYzAzNGUyIiwidGltZXNlcmllcy56b25lcy40MWNhMTBjYy01ZDMyLTRhMmUtYmQzNS0wMzcwZDc2MjQ0YjgiLCJvcGVuaWQiLCJhY3MucG9saWNpZXMiLCJwcmVkaXgtYXNzZXQuem9uZXMuMTIxMTJhZTctYWIzNi00Mjc4LTg4MTgtYzgwNjFhM2UxYjZiIiwicHJlZGl4LWFjcy56b25lcy5jYzFmMjk4ZC1lODVmLTRjMDItOWE2Ni1mZjkyYzYwYzc5YjQiLCJ0aW1lc2VyaWVzLnpvbmVzLmUzZmJhODVlLWQzMzQtNDA5ZS04N2NlLTNhMTdlNzFiNDk0NiIsImFjcy5hdHRyaWJ1dGVzIiwidWFhIiwicHJlZGl4LWFzc2V0LnpvbmVzLmNiYmU1ZTFlLWQyZTctNDRhYS04ZTY1LTdiYTlhNTA0Njg1NSIsInByZWRpeC1hc3NldC56b25lcy5mZWJjMjEyNy1mOTA3LTQ4ZTEtYmMyNS0zYjJmN2Y5MDI2YWEiLCJwcmVkaXgtYWNzLnpvbmVzLjBmNWQxOWI4LTEzMDUtNDQ5NC04YmY5LWJjOTZmNTU3N2YxZiIsImFwcF9jbGllbnRfaWQiLCJwcmVkaXgtYWNzLnpvbmVzLjY4ZjcyNjc4LWM5ZWMtNGY4MS05NjJkLWIzYmZkN2FmMjdkMiJdfQ.oD6PnhsId9oW_PP5RUt3mKSFfYNtqtD5huRoA5cAZMI_tfjbM8F8o7l-zNkO8HWWnWwwm-CjWwr1zYlSzrPjn5PoUnOFxtdb6R4u8cJWGyZ2obW1HYGIcvy4eW6OaXv_49vHzsBrA0JFnZN6Zrlvy7wjrBQpzh5TnwbJI4Ml7A-w9AEXptx-0MHVknulJ5B9gJAU8ZGEZJlR2h0qI3lrrm3KBdtsFG_nIzxnBPD0wHNM4U97TIRMC26F1BlRolw5nZyKQkaSiNxk3xAxjAEISCPVR9Zt9J4HM2CQ58s3nf3luycZFD8zu2_fWlsSgbIVPxmM34c3MN9avZe-xRjE6A';

    // options for db call
    var options = {
		// Post request to get the data
        method: 'POST',
		// URL for time series query (generic, defined above)
        url: timeseries_query_uri,
		// Set the headers with the necessary parameters
        headers : {
            'Accept': 'application/json, application/x-www-form-urlencoded',
            'Authorization': 'Bearer ' + token,
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
			console.log("Success!");
            res.send(response.body);
			
		}
		// Not successful request
        else{
			// Send erro back to the requester
			console.log("Error!");
            res.send(error);
        }
    });

    //req.query contains the url queries, can be used to get specific time data down the line
    //by modifying the start / end key/value pairs in json_data below
    console.log(req.query);

};
