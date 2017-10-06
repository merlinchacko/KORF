var app = angular.module('imageApp', []);

app.controller('GetController', function($scope, $http, $location) {
	
	$scope.errMessage = "";
	console.log("url:"+$location.absUrl());
	$scope.download = function(){
		var url = $location.absUrl() + "download";
		console.log("url:"+url);
		var config = {
				headers : {
					'Content-Type': 'application/json;charset=utf-8;'
				}
		}
		console.log("i am here");
		$http.get(url, config).then(function (response) {
			console.log("i am here:2:"+response);
		}, function (response) {
			$scope.errMessage = "Invalid input or Internal server error!";
		});
	}
	
});

