
// Define a new module for our app
var app = angular.module('predixModule', []);

// The main controller for the Predix Hackathon
app.controller('mainController', function($scope, $http) {
	// Add the function getDataFromPredix to the scope
	// This will be called on page load
    $scope.getDataFromPredix = getDataFromPredix;
    
    /*
     * Get data from Predix - send the http request that will be mapped in our controller
     */
    function getDataFromPredix(){
    	// test
    	// Get the UAA authentication token
    	$http.get('/springmvc-helloworld/getAuthToken/')
    	
    	// If the request was successful
    	.success(function(authToken) {	
    		// Log auth token in console
    		console.log(authToken);
    		// Get data from predix using the token
    		$http.post('/springmvc-helloworld/getData/')
        	
        	// If the request was successful
        	.success(function(data) {			
        		// Log the data in console
        		console.log(data);
        	})
        	// Error from getData request
        	.error(function(data) {
        		// Log the error in console
        		console.log("Could not get the Predix data due to " + data);
        	}); 
    	})
    	// Error from getAuthToken request
    	.error(function(data) {
    		// Log error in console
    		console.log("Could not get the Auth Token due to " + data);
    	}); 
    	
    	
    }
    // Call method on page load
  	getDataFromPredix(); 
 
       
	//end controller
});
