package mauriciopoblete.walkwithme;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button connect;

    private static final int BluetoothON = 1;

    BluetoothAdapter wwmBluetoothAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connect = (Button)findViewById(R.id.botonConnect);

        wwmBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(wwmBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Your phone does not support bluetooth", Toast.LENGTH_LONG).show();
        } else if(!wwmBluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, BluetoothON);
        }

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent abrirListabb = new Intent(MainActivity.this, RouterDevicesWWM.class);
                    startActivity(abrirListabb);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case BluetoothON:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(getApplicationContext(), "Bluetooth enabled", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth could not be activated, application will be closed.", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }
}
