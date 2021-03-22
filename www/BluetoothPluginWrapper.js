module.exports = {
	miniATM : function(params, successCallback, errorCallback) {

		cordova.exec(successCallback, errorCallback, "BluetoothPluginWrapper","miniATM", [params]);
	},
	
};