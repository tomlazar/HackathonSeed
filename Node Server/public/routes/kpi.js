var request 		= require('request');

exports.getKpi = function(req, res){
  
    var timeseries_query_uri 	= 'https://time-series-store-predix.run.aws-usw02-pr.ice.predix.io/v1/datapoints';
    var timeseries_zone_id 		= 'f4939df9-fcf2-46af-90e2-dfce1a2a26bd';

                var json_data =
                    {
                        "start": 0000000000000,
                        "end": (new Date).getTime(), 
                        "tags": [
                            {
                                "name": req.params['kpiName'],
                                "order": "desc"
                           }
                       ]
                    };

                // options for db call
                var options = {
                    method: 'POST',
                    url: timeseries_query_uri,
                    headers : {
                          'Accept': 'application/json, application/x-www-form-urlencoded',
                          'Authorization': 'Bearer ' + req.params['token'],
                          'Predix-Zone-Id': timeseries_zone_id
                      },
                    json : json_data
                };

                // call to db, if successful, return data as json for client
                request(options, function (error, response, body) {
                    if (!error && response.statusCode == 200) {
                        //console.log(response);
                        res.send(response.body);

                    }
                    else{
                        //console.log(response);
                        res.send(error);
                    }
                })

            
        
    	

    //req.query contains the url queries, can be used to get specific time data down the line
    //by modifying the start / end key/value pairs in json_data below
    console.log(req.query);

};

