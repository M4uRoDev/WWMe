package mauriciopoblete.walkwithme;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by M4uRo on 19-12-16.
 */
public class alertReceiver extends Service {
    public static final int notification_id = 1;
    String id = "";
    // Handler that receives messages from the thread
    public void onCreate() {
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Servicio monitor iniciado...", Toast.LENGTH_SHORT).show();

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);

                id = intent.getExtras().getString("idRedApoyo");
                final String funcion = "viewAlertas";

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Constants.FUNCIONES_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    String error = jsonObject.getString("error");

                                    if(error.equalsIgnoreCase("true")){
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                        return;
                                    }else if(error.equalsIgnoreCase("false")){
                                        String nombre = jsonObject.getString("nombreAdultoMayor");
                                        String apellido = jsonObject.getString("apellidoAdultoMayor");

                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http//ip.bancademia.net/wwm/"));
                                        PendingIntent pendingIntent = PendingIntent.getActivity(alertReceiver.this,0,intent,0);

                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(alertReceiver.this);
                                        builder.setSmallIcon(R.mipmap.ic_launcher);
                                        builder.setContentIntent(pendingIntent);
                                        builder.setAutoCancel(true);
                                        builder.setVibrate(new long[] {1000,1000,1000,1000,1000,1000});
                                        builder.setTicker("Alerta de Caída! - Walk With Me");
                                        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
                                        builder.setContentTitle("Alerta de Caída!");
                                        builder.setContentTitle("El adulto "+nombre+" "+apellido+" ha caido");
                                        builder.setSubText("Toque para atender alerta");

                                        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                                        notificationManager.notify(notification_id,builder.build());

                                    }

                                }catch(JSONException e){
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
                        params.put("idRedApoyo",id);
                        return params;
                    }
                };
                RequestHandler.getInstance(alertReceiver.this).addToRequestQueue(stringRequest);

            }

        };


        new Thread(new Runnable(){
            public void run() {
                // TODO Auto-generated method stub
                while(true)
                {
                    try {
                        Thread.sleep(1000);
                        handler.sendEmptyMessage(0);

                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        }).start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Servicio Monitoreo Detenido!", Toast.LENGTH_SHORT).show();
    }
}
