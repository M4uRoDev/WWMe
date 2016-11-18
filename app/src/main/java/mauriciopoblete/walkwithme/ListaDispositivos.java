package mauriciopoblete.walkwithme;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.BeanManager;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.Callback;
import com.punchthrough.bean.sdk.message.DeviceInfo;
import com.punchthrough.bean.sdk.message.LedColor;
import com.punchthrough.bean.sdk.message.ScratchBank;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ListaDispositivos extends ListActivity implements Serializable {

    final String AreConnected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ArrayList<Bean> beans = new ArrayList<>();

        final ArrayAdapter<Bean> Dispositivos = new ArrayAdapter(this, android.R.layout.simple_list_item_1);

        BeanDiscoveryListener listener = new BeanDiscoveryListener() {
            @Override
            public void onBeanDiscovered(Bean bean, int rssi) {
                beans.add(bean);
                String addr = bean.getDevice().getAddress();
                for(Bean b: beans){

                    if(Dispositivos.getCount() > 0){
                        Bean ultimoRegistro = Dispositivos.getItem(Dispositivos.getCount()-1);
                        if(ultimoRegistro != b){
                            if(b.getDevice().getAddress().equals(addr)){
                                Dispositivos.add(b);
                            }
                        }
                    }else{
                        Dispositivos.add(b);
                    }


                }
                setListAdapter(Dispositivos);

            }


            @Override
            public void onDiscoveryComplete() {
                // This is called when the scan times out, defined by the .setScanTimeout(int seconds) method+
                /*for(Bean bean: beans){

                    //PREPARAMOS LISTA DE BEANS A MOSTRAR EN LA APLICACIÓN
                    if(Dispositivos.getCount() > 0){ //SE PREGUNTA SI EXISTEN REGISTROS YA
                        Bean lastReg = Dispositivos.getItem(Dispositivos.getCount() -1);
                        if(lastReg != bean){
                            Dispositivos.add(bean);
                        }
                    }else{
                        Dispositivos.add(bean);
                    }

                }*/


            }
        };
        BeanManager.getInstance().setScanTimeout(15);  // Timeout in seconds, optional, default is 30 seconds
        BeanManager.getInstance().startDiscovery(listener);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        final Bean beanConnect = (Bean) l.getAdapter().getItem(position);
        final Intent returnLectura = new Intent(this, lecturaAcc.class);
        returnLectura.putExtra("connect",(Bean) l.getAdapter().getItem(position));

        BeanListener beanListener = new BeanListener() {
            @Override
            public void onConnected() {
                Toast.makeText(getApplicationContext(), "Connect Success!", Toast.LENGTH_LONG).show();
                startActivity(returnLectura);
                finish();

            }

            @Override
            public void onConnectionFailed() {

            }

            @Override
            public void onDisconnected() {

            }

            @Override
            public void onSerialMessageReceived(byte[] data) {

            }

            @Override
            public void onScratchValueChanged(ScratchBank bank, byte[] value) {

            }

            @Override
            public void onError(BeanError error) {

            }

            @Override
            public void onReadRemoteRssi(int rssi) {

            }
        };
        beanConnect.connect(this,beanListener);
    }
}
