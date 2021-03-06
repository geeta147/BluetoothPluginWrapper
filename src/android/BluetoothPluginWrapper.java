package cordova.plugin.bluetoothpluginwrapper;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
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
import android.os.Handler;
import java.util.Set;


/**
 * This class echoes a string called from JavaScript.
 */
public class BluetoothPluginWrapper extends CordovaPlugin {
Handler mHandler;
private BluetoothAdapter mBluetoothAdapter;
	
private CallbackContext callbackContext;
	
private Set<BluetoothDevice> pairedDevices;

private static final int REQUEST_ENABLE_BT = 1;
private PaymentInitialization initialization;
	 
private String deviceMACAddress, deviceName, device_MAC_Add, selectedUSBDevice, ipandport, deviceType;
	public BluetoothPluginWrapper() {
		
	}
	
	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
        
	}
	
	@Override
	public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
     	mHandler=new Handler();

		this.callbackContext = callbackContext;
		// pairedDevices = mBluetoothAdapter.getBondedDevices();
		//  for (BluetoothDevice device : pairedDevices) {
	    //         deviceName = device.getName();
	    //         Log.e("Paired Devices", deviceName);
		//  }
		 if(action.equals("miniATM")){
			try {
				// Intent intent = new Intent();
				// // Intent intent = new Intent();
				// // https://sandbox.payswiff.com/merchantConfiguration/paymentTypes
				//   intent.setAction("com.pnsol.sdk.payment.PaymentInitialization.initiateTransaction");
				// intent.setAction(" com.pnsol.sdk.vo.response.PaymentTypes");
				//   intent.putExtra("Handler",mHandler);
                //   intent.putExtra("DEVICE_NAME", "C-ME30S-099184");

				//   intent.putExtra("MAC_ADDRESS", "38:3C:9C:EA:9F:73");
                //   intent.putExtra("DEVICE_NAME", "C-ME30S-099184");
                //   intent.putExtra("DEVICE_COMMUNICATION_MODE",1);
                //   intent.putExtra("PAYMENT_TYPE", "BalanceEnquiry");
                //   intent.putExtra("referanceno", "1616171475463");
                //   intent.putExtra("amount","10.00");
                // //   intent.putExtra("cashBackAmoumt", "");
				
				// // intent.putExtra(PAYMENT_TYPE, 8);
				// // intent.putExtra("referanceno", "123456");
				// // intent.putExtra(DEVICE_NAME, "C-ME30S-099184");
				// // intent.putExtra("amount","10");
				// // intent.putExtra(MAC_ADDRESS, "38:3C:9C:EA:9F:73");
				// // intent.putExtra(DEVICE_COMMUNICATION_MODE, "BLUETOOTHCOMMUNICATION");
				// // intent.putExtra("cashBackAmoumt", "");
				Context context = cordova.getActivity();
				 initialization = new PaymentInitialization(context);
                initialization.initiateTransaction(mHandler,
				        "C-ME30S-099184",
                        "38:3C:9C:EA:9F:73",
                        "10.00",
                        "BalanceEnquiry",
						// "MICROATM",
                        "POS",
						"7454957737",
						"",
						71.000001,
                        17.000001,
                        "1616171475463",
                         null,
						 1,
						 "orderRef",
						 "appName",
						 "appVersion"
                       );
				// cordova.startActivityForResult(this, intent, 1);
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}
			return true;
		 }
		return false;	
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case 1:
			if (resultCode == Activity.RESULT_OK) {
				try {
					if (data != null) {
						String result = data.getStringExtra("MINI_ATM");
						// String rdService = data.getStringExtra("RD_SERVICE_INFO");
						JSONObject object = new JSONObject();
						// object.put("rd_service_info", rdService);
						object.put("miniATM", result);
						onSuccessRes(object);
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
