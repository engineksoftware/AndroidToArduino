package engineksoftware.arudinoproject;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();

        if(bt.isEnabled() == false) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 0);
        }else{
            startFrag();
        }


    }

    /*
     * Starts the fragment once Bluetooth has been turned on.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                startFrag();
            }
        }
    }

    /*
     * Gets around an error that caused the App to crashed due to an
     * IllegalStateException: Can not perform this action after onSaveInstanceState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    /*
     * Starts the first fragments that allows you to connect to the HC-06 module.
     */
    public void startFrag(){
        DeviceFragment dFrag = new DeviceFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout, dFrag).commit();
    }
}
