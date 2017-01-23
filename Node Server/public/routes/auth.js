var request 		= require('request');

exports.authenticate = function(req, res){
    var uaa_issuer_id		= "https://df1e4f24-d699-4fef-a783-916e727ea2a5.predix-uaa.run.aws-usw02-pr.ice.predix.io/oauth/token"

    //should probably make a predix client in uaa alongside the ts client, or else maybe the tokens get screwed up?
    var client_password     = "password";
    var client_id           = "tsWebApp";

    // pretend this works
    //var base64_auth = Buffer.from(client_id+":"+client_password).toString('base64');

    var base64_auth = 'dHNXZWJBcHA6cGFzc3dvcmQ=';

    // parameters for call to get token
    var params_token = {
        grant_type: 'client_credentials'
    };

    // options for call to get token
    var options_token = {
        method: 'POST',
        url: uaa_issuer_id,
        headers : {
                'Accept': 'application/json, application/x-www-form-urlencoded',
                'Authorization': 'Basic ' + base64_auth,
                'Content-Type': 'application/x-www-form-urlencoded'
            },
        form : 'client_id='+client_id+'&grant_type=client_credentials'
    }

    //note to figure out why this line is needed, something to do with the uaa server config i think
    //http://stackoverflow.com/questions/19254029/angularjs-http-post-does-not-send-data
    request(options_token, function (error, response, body) {
            if (!error && response.statusCode == 200) {

                // if we successfully get the token, parse response body to json
                // access token from json assigned to var for db call
                var body = JSON.parse(response.body);
                var access_token = body.access_token;
				console.log('Auth in Auth worked');
				res.send(access_token);


            }
            else{
                console.log(error);
                console.log('error getting auth token')
            }
    	})

    //req.query contains the url queries, can be used to get specific time data down the line
    //by modifying the start / end key/value pairs in json_data below
    console.log(req.query);

};
