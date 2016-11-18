package mauriciopoblete.walkwithme;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button connect;

    private static final int BluetoothON = 1;
    private static final int quiereConectar = 2;

    BluetoothAdapter wwmBluetoothAdapter = null;
    BluetoothDevice wwmDevice = null;
    BluetoothSocket wwmSocket = null;
    boolean conectado = false;

    private static String MAC = null;

    UUID WWM_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

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

                    Intent abrirLista = new Intent(MainActivity.this, ListaDispositivos.class);
                    startActivityForResult(abrirLista, quiereConectar);
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
            /* case quiereConectar:
                if(resultCode == Activity.RESULT_OK){
                    //MAC = data.getExtras().getString(ListaDispositivos.Dir_MAC);
                    //Toast.makeText(getApplicationContext(), "MAC To connect: " + MAC, Toast.LENGTH_LONG).show();
                    wwmDevice = wwmBluetoothAdapter.getRemoteDevice(MAC);
                    try {
                        wwmSocket = wwmDevice.createRfcommSocketToServiceRecord(WWM_UUID);
                        wwmSocket.connect();
                        conectado = true;
                        connect.setText("Disconnect");
                        Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this,lecturaAcc.class);
                        startActivity(intent);
                    } catch (IOException error){
                        conectado = false;
                        Toast.makeText(getApplicationContext(), "An error has occurred: " + error, Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "No device selected", Toast.LENGTH_LONG).show();
                }*/
        }
    }
}
