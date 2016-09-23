package engineksoftware.arudinoproject;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends FragmentActivity {

    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    Bluetooth bt;
    String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt = new Bluetooth(this, handler);

        if(btAdapter.isEnabled() == false) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 0);
        }else{
            openDeviceFrag();
        }


    }

    /*
     * Starts the fragment once Bluetooth has been turned on.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                openDeviceFrag();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        bt.stop();
    }

    /*
     * Gets around an error that caused the App to crashed due to an
     * IllegalStateException: Can not perform this action after onSaveInstanceState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    public final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Bluetooth.MESSAGE_STATE_CHANGE:
                    Log.d(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    if(msg.arg1 == 3) {
                        replaceDeviceFrag();
                        Toast.makeText(MainActivity.this, "Connected!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Bluetooth.MESSAGE_WRITE:
                    Log.d(TAG, "MESSAGE_WRITE ");
                    break;
                case Bluetooth.MESSAGE_READ:
                    Log.d(TAG, "MESSAGE_READ ");
                    break;
                case Bluetooth.MESSAGE_DEVICE_NAME:
                    Log.d(TAG, "MESSAGE_DEVICE_NAME "+msg);
                    break;
                case Bluetooth.MESSAGE_TOAST:
                    Log.d(TAG, "MESSAGE_TOAST "+msg);
                    break;
            }
        }
    };

    /*********************************************************************************/
    /*Device Fragment Methods*/
    /*********************************************************************************/

    /*
     * Starts the first fragments that allows you to connect to the HC-06 module.
     */
    public void openDeviceFrag(){
        DeviceFragment dFrag = new DeviceFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout, dFrag).commit();
    }

    /*
     * Connects to the bluetooth module.
     */
    public void connectService(){
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter.isEnabled()) {
                bt.start();
                bt.connectDevice("HC-06");
                Log.d(TAG, "Btservice started - listening");
                Toast.makeText(this, "Trying to connect...", Toast.LENGTH_SHORT).show();
            } else {
                Log.w(TAG, "Btservice started - bluetooth is not enabled");
            }
        } catch(Exception e){
            Log.e(TAG, "Unable to start bt ",e);
        }
    }

    /*********************************************************************************/
    /*Device Fragment Methods*/
    /*********************************************************************************/

    /*********************************************************************************/
    /*Main Fragment Methods*/
    /*********************************************************************************/

    /*
     * Replaces the DeviceFragment with the MainFragment.
     */
    public void replaceDeviceFrag(){
        MainFragment mf = new MainFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, mf);
        transaction.commit();
    }

    /*
     * Sends the number entered in the EditText to the bluetooth module. '1' tells the Arduino to convert
     * the number to binary, and light the LEDs.
     */
    public void sendBase10(String num){
        bt.sendMessage("1" + num + "*");
    }

    /*********************************************************************************/
    /*Main Fragment Methods*/
    /*********************************************************************************/
}
