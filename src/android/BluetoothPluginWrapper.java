package cordova.plugin.bluetoothpluginwrapper;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;
import com.pnsol.sdk.auth.AccountValidator;
import com.pnsol.sdk.interfaces.DeviceCommunicationMode;
import com.pnsol.sdk.interfaces.DeviceType;
import com.pnsol.sdk.interfaces.PaymentTransactionConstants;
import com.pnsol.sdk.payment.PaymentInitialization;
import com.pnsol.sdk.payment.PaymentModeThread;
import com.pnsol.sdk.vo.response.PaymentTypes;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;

/**
 * This class echoes a string called from JavaScript.
 */
public class BluetoothPluginWrapper extends CordovaPlugin {

  private BluetoothAdapter mBluetoothAdapter;
	
private CallbackContext callbackContext;
	
private Set<BluetoothDevice> pairedDevices;

private static final int REQUEST_ENABLE_BT = 1;
	 
private String deviceMACAddress, deviceName, device_MAC_Add, selectedUSBDevice, ipandport, deviceType;
	
	public BluetoothPluginWrapper() {
		
	}
	
	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);

	}
	
	@Override
	public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
		this.callbackContext = callbackContext;
		 if(action.equals("miniATM")) {
			 mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
				if (mBluetoothAdapter == null) {
					PluginResult result = new PluginResult(PluginResult.Status.ERROR, "Please Enable Bluetooth");
					result.setKeepCallback(true);
					callbackContext.sendPluginResult(result);
		        }
				else {
					if (!mBluetoothAdapter.isEnabled()) {
		                Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		                startActivityForResult(discoveryIntent, REQUEST_ENABLE_BT);
		            }
				}
				pairedDevices = mBluetoothAdapter.getBondedDevices();
				 for (BluetoothDevice device : pairedDevices) {
			            deviceName = device.getName();
			            Log.e("Paired Devices", deviceName);
				 }
			try {
				Intent intent = new Intent();
				intent.setAction("com.pnsol.sdk.payment.PaymentInitialization");
				intent.putExtra("PAYMENT_TYPE", "miniATM");
				intent.putExtra("referanceno", "test");
				intent.putExtra("DEVICE_NAME", "me30s");
				intent.putExtra("amount","100");
				intent.putExtra("MAC_ADDRESS", "");
				intent.putExtra("DEVICE_COMMUNICATION_MODE", "BLUETOOTHCOMMUNICATION");
				intent.putExtra("cashBackAmoumt", "0");
				cordova.startActivityForResult(this, intent, 1);
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}
			return true;
		 }
		 else {
			 return false;
		 }
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == Activity.RESULT_OK) {
				try {
					if (data != null) {
						data = new JSONObject();
//						String result = data.getStringExtra("DEVICE_INFO");
//						String rdService = data.getStringExtra("RD_SERVICE_INFO");
//						JSONObject object = new JSONObject();
//						object.put("rd_service_info", rdService);
//						object.put("device_info", result);
						onSuccessRes(data);
					}
				} catch (Exception e) {
					Log.e("Error", "Error", e);
					onFailedRes(e.toString());
				}
			}
			return;
		case 2:
			if (resultCode == Activity.RESULT_OK) {
				try {
					JSONObject object = new JSONObject();
					if (data != null) {
						String result = data.getStringExtra("PID_DATA");
						object.put("pid_data", result);
					}
					onSuccessRes(object);
				} catch (Exception e) {
					Log.e("Error", "Error", e);
					onFailedRes(e.toString());
				}
			}
			return;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	

	private void onSuccessRes(JSONObject response) {
		if (callbackContext != null) {
			// tolog(response.toString());
			PluginResult result = new PluginResult(PluginResult.Status.OK, response);
			result.setKeepCallback(true);
			callbackContext.sendPluginResult(result);
		}
	}
	
	private void onFailedRes(String error) {
		if (callbackContext != null) {
			PluginResult result = new PluginResult(PluginResult.Status.ERROR, error);
			result.setKeepCallback(true);
			callbackContext.sendPluginResult(result);
		}
	}
	
	public void tolog(String toLog) {
		Context context = cordova.getActivity();
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, toLog, duration);
		toast.show();
	}
}
