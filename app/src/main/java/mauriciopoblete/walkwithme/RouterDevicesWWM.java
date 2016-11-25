package mauriciopoblete.walkwithme;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanManager;

import java.io.Serializable;
import java.util.ArrayList;

public class RouterDevicesWWM extends ListActivity implements Serializable {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ArrayList<Bean> beans = new ArrayList<>();

        final ArrayAdapter<Bean> Dispositivos = new ArrayAdapter(this, android.R.layout.simple_list_item_1);

        BeanDiscoveryListener listener = new BeanDiscoveryListener() {
            @Override
            public void onBeanDiscovered(Bean bean, int rssi) {
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
                beans.add(bean);
                setListAdapter(Dispositivos);

            }


            @Override
            public void onDiscoveryComplete() {

            }
        };
        BeanManager.getInstance().setScanTimeout(10);  // Timeout in seconds, optional, default is 30 seconds
        BeanManager.getInstance().startDiscovery(listener);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        final Intent returnLectura = new Intent(this, lecturaAcc.class);
        returnLectura.putExtra("connect", (Bean) l.getAdapter().getItem(position));
        startActivity(returnLectura);
        finish();

    }
}
