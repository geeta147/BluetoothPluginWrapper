module.exports = {
	miniATM : function(name, successCallback, errorCallback) {
		cordova.exec(successCallback, errorCallback, "BluetoothPluginWrapper",
				"miniATM", [ name ]);
	},
	
};
