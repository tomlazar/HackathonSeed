var app = angular.module('predixHackathon', ['ngRoute']);


app.config(function ($routeProvider) {
    $routeProvider

    // route for the home page
        .when('/', {
            templateUrl: '/templates/404.html',
            controller: 'mainController'
        })

        // route for the ics page
       
        .when('/Bar', {
            templateUrl: '/templates/BarChart.html',
            controller: 'mainController'
        })

        .when('/Scatter', {
            templateUrl: '/templates/ScatterGraph.html',
            controller: 'mainController'
        })

        .when('/Line', {
            templateUrl: '/templates/LineGraph.html',
            controller: 'mainController'
        })

        .when('/404', {
            templateUrl: '/templates/404.html',
            controller: 'mainController'
        });
});

app.controller('sampleController', function ($scope, $http) {

    //controller code goes here

//end controller
});

app.controller('mainController', function ($scope, $http) {
    var token;
    $scope.loadKpiData = loadKpiData;
    $scope.token = null;
    //console.log('hello, this is the main controller');
    // Removed auth token request from the time-series request
    $scope.choose = function choose(choice){
    console.log('Hi');
    engineName = choice;

}
    $http.get('/api/auth/')
        .success(function (data) {
            // Store the auth token to be used for time-series requests
            token = data;
            $scope.token = token;
            console.log(token);
            //loadKpiData('Engine Speed');
        })
        .error(function (data) {
            console.log('Error: ' + data);
        });

    function loadKpiData(kpiName) {
        // if auth is null
        // alert
        // Add the auth token as a parameter for a time-series request
        console.log('test');
        $http.get('/api/kpi/' + kpiName + '/' + $scope.token)
        
            .success(function (data) {
                data = data.map(new function(){
                    chartData =  {
                        'x' : data[0],
                        'y': data[1]
                    }
                    console.log(chartData);
                });
                console.log(data);
            })
            .error(function (data) {
                console.log('Error: ' + data);
            });

        //end function
    }

//end controller
});

