
// Define a new module for our app
var app = angular.module('predixModule', []);

// The main controller for the sonar issue tracker
app.controller('mainController', function($scope, $http) {
	// Add the function generateIssueList to the scope
	// This will be called on page load, and the apply filters button
    $scope.getDataFromPredix = getDataFromPredix;

    function getDataFromPredix(){
    	$http.get('/getAuthToken')
    	
    	// If the request was successful, parse the response
    	.success(function(authToken) {			
    		console.log(authToken);
    		$http.post('/getData/')
        	
        	// If the request was successful, parse the response
        	.success(function(data) {			
        		console.log(data);
        	})
        	.error(function(data) {
        		console.log("Could not get the Predix data due to " + data);
        	}); 
    	})
    	.error(function(data) {
    		console.log("Could not get the Auth Token due to " + data);
    	}); 
    	
    	
    }

  	getDataFromPredix(); 
 
       
	//end controller
});