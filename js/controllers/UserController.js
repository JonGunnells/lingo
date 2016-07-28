module.exports = function(app) {
    app.controller('UserController', ['UserService', '$scope', '$location', function(UserService, $scope, $location) {
        $scope.userInput = '';
        $scope.userPassword = '';

        $scope.UserPrefences = UserService.getPreferences();

        $scope.signIn = function() {
            console.log("clicked log in", $scope.UserPrefences);
            UserService.postExistingUser($scope.userInput, $scope.userPassword)
        }

        $scope.signUp = function() {
            // console.log("clicked sign up");
            UserService.postUserInfo($scope.userInput, $scope.userPassword)

        }

        //when controller loads, we get our user Preference object from the server;
        //this saves our object to the server with our current values that changed on our model
        $scope.savePref = function() {
            console.log('saving saving saving Technology is: ', $scope.UserPrefences.technology);
            UserService.updatePreferences($scope.UserPrefences).then(function(result) {
                console.log("result > updatePreferences", result);
                //$scope.UserPrefences = UserService.getPreferences();
                console.log('saved saved saved Technology is: ', $scope.UserPrefences.technology);
            });
            //console.log('saving preferencesss:', $scope.UserPrefences);
            $location.path('/news');
        };

        //these toggle and change our values of our user preference model/object

        // TOGGLE spanish
        $scope.setSpanish = function() {
            console.log("Your lengua es Espanol");
            $scope.UserPrefences.language = "spanish";
        };
        // TOGGLE FRENCH
        $scope.setFrench = function() {
            console.log("Wee wee!!!!");
            $scope.UserPrefences.language = "french";
        };
        // TOGGLE ON AND OFF TECHNOLOGY PREFERENCE
        $scope.toggleTechnology = function(value) {
            console.log("Tech is: ", value);
            $scope.UserPrefences.technology = value;
        };
        // TOGGLE ON AND OFF BUSINESS PREFERENCE
        $scope.toggleBusiness = function(value) {
            console.log("Busy-ness is: ", value);
            $scope.UserPrefences.business = value;
        };
        // TOGGLE ON AND OFF POLITICS PREFERENCE
        $scope.togglePolitics = function(value) {
            console.log("Politics is: ", value);
            $scope.UserPrefences.politics = value;
        };
        // TOGGLE ON AND OFF ARTS PREFERENCE
        $scope.toggleArts = function(value) {
            console.log("Arts is: ", value);
            $scope.UserPrefences.arts = value;
        };
        // TOGGLE ON AND OFF SPORTS PREFERENCE
        $scope.toggleSports = function(value) {
            console.log("Sports is: ", value);
            $scope.UserPrefences.sports = value;
        };
    }]);
}
