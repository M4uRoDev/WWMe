package mauriciopoblete.walkwithme;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.LedColor;
import com.punchthrough.bean.sdk.message.ScratchBank;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by M4uRo on 27-11-16.
 */
public class alertDispatcher extends Service{

    String texto = null;
    private StringBuilder sb = new StringBuilder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        Toast.makeText(getApplicationContext(), "Preparando Conexión...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String funcion = "addAlerta";
        final String idAdultoMayor = intent.getExtras().getString("idAdultoMayor");
        Toast.makeText(getApplicationContext(), "Intentado conexión...", Toast.LENGTH_SHORT).show();
        final Bean beanConnect = (Bean) intent.getExtras().get("connect");
        final LedColor green = LedColor.create(0, 255, 0);
        final LedColor blue = LedColor.create(0,0,255);
        final LedColor red = LedColor.create(255,0,0);
        final LedColor off = LedColor.create(0, 0, 0);

        final BeanListener beanListener = new BeanListener() {

            @Override
            public void onConnected() {
                Toast.makeText(getApplicationContext(), "Enlanzado con dispositivo...", Toast.LENGTH_SHORT).show();
                beanConnect.setLed(green);
            }

            @Override
            public void onConnectionFailed() {
                beanConnect.setLed(red);
                Toast.makeText(getApplicationContext(), "Error de Conexion", Toast.LENGTH_SHORT).show();
                beanConnect.setLed(off);
            }

            @Override
            public void onDisconnected() {

            }
            @Override
            public void onSerialMessageReceived(byte[] data) {
                beanConnect.setLed(off);
                String s = new String(data);
                texto = s;

                sb.append(s);                                                // append string
                int endOfLineIndex = sb.indexOf("\r\n");                            // determine the end-of-line
                if (endOfLineIndex > 0) {                                            // if end-of-line,
                    String sbprint = sb.substring(0, endOfLineIndex);               // extract string
                    sb.delete(0, sb.length());                                      // and clear
                    if(sbprint.equals("CAIDA")) {
                        for(int i=1;i<10;i++) {
                            beanConnect.setLed(red);
                            beanConnect.setLed(blue);
                            beanConnect.setLed(red);
                            beanConnect.setLed(blue);
                            beanConnect.setLed(red);
                        }
                        beanConnect.setLed(off);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                Constants.FUNCIONES_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try{
                                            JSONObject jsonObject = new JSONObject(response);
                                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                        }catch (JSONException e){
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("funcion",funcion);
                                params.put("idAM",idAdultoMayor);
                                return params;
                            }
                        };
                        RequestHandler.getInstance(alertDispatcher.this).addToRequestQueue(stringRequest);
                    }
                }

            }

            @Override
            public void onScratchValueChanged(ScratchBank bank, byte[] value) {

            }

            @Override
            public void onError(BeanError error) {
                Toast.makeText(getApplicationContext(), "Error Bean: " + error, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onReadRemoteRssi(int rssi) {

            }
        };
        beanConnect.connect(this, beanListener);
        return START_NOT_STICKY; //indica que el servicio no debe recrearse al ser destruido sin importar que haya quedado un trabajo pendiente.
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), "Servicio destruido...", Toast.LENGTH_SHORT).show();
    }
}
