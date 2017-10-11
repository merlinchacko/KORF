var app = angular.module('imageApp', []);

app.controller('GetController', function($scope, $http, $location) {

	$scope.errMessage = "";

	
	$scope.download = function($files) {
		
		var url = $location.absUrl() + "download";
		  var fd = new FormData();
		    angular.forEach($scope.files,function(file)
					  {
		    	 console.log("i am here"+file);
				  fd.append('file', file);

					  });
	   
	    /*$http({
	        method: 'POST',
	        url: url,
	        headers: {'Content-Type': undefined},
	        data: fd,
	        transformRequest: angular.identity
	        })*/
		    $http.post(url, 
					  fd,
					  {
				  transformRequest: angular.identity,
				  headers: {'Content-Type': undefined}

					  })
	       .success(function(data, status) {
	             alert("success");
	        });
	}

})
.directive('fileInput', ['$parse', function($parse){
  		return {
  			restrict: 'A',
  			link: function(scope, elm, attrs){
  				elm.bind('change', 
  						function(){
  							$parse(attrs.fileInput).assign(scope, elm[0].files);
  							var input = document.getElementById ("fileUploader");
  			  	           $parse(attrs.filePath).assign(scope, input.value);
  							
  							scope.$apply();
  						}
  					);
  			}
  		}
  	}]);

