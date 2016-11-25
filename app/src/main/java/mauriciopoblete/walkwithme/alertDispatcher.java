package mauriciopoblete.walkwithme;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.view.WindowManager;
import android.widget.Toast;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.LedColor;
import com.punchthrough.bean.sdk.message.ScratchBank;

/**
 * Created by M4uRo on 27-11-16.
 */
public class alertDispatcher extends Service{
    public static final int notification_id = 1;

    String texto = null;
    private StringBuilder sb = new StringBuilder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        Toast.makeText(getApplicationContext(), "Intentando Conexión...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "Construyendo tuneles para la conexión...", Toast.LENGTH_SHORT).show();
        final Bean beanConnect = (Bean) intent.getExtras().get("connect");
        final LedColor green = LedColor.create(0, 255, 0);
        final LedColor blue = LedColor.create(0,0,255);
        final LedColor red = LedColor.create(255,0,0);
        final LedColor off = LedColor.create(0, 0, 0);

        final BeanListener beanListener = new BeanListener() {

            @Override
            public void onConnected() {
                Toast.makeText(getApplicationContext(), "Conectando...", Toast.LENGTH_SHORT).show();
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
                beanConnect.setLed(blue);
                String s = new String(data);
                texto = s;

                sb.append(s);                                                // append string
                int endOfLineIndex = sb.indexOf("\r\n");                            // determine the end-of-line
                if (endOfLineIndex > 0) {                                            // if end-of-line,
                    String sbprint = sb.substring(0, endOfLineIndex);               // extract string
                    sb.delete(0, sb.length());                                      // and clear
                    //oast.makeText(getApplicationContext(),"En estos momentos: "+ sbprint,Toast.LENGTH_LONG).show();
                    if(sbprint.equals("CAIDA")) {
                        for(int i=1;i<10;i++) {
                            beanConnect.setLed(red);
                            beanConnect.setLed(blue);
                            beanConnect.setLed(red);
                            beanConnect.setLed(blue);
                            beanConnect.setLed(red);
                        }
                        //Construcción intent implicito para notificacion
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.icinfvm.cl"));
                        PendingIntent pendingIntent = PendingIntent.getActivity(alertDispatcher.this, 0, intent, 0);

                        //Construcción notificación
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(alertDispatcher.this);
                        builder.setSmallIcon(R.mipmap.ic_launcher);
                        builder.setContentIntent(pendingIntent);
                        builder.setAutoCancel(true);
                        builder.setVibrate(new long[] {1000, 1000, 1000, 1000, 1000});
                        builder.setTicker("Alerta de caída! - WalkWithMe");
                        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                        builder.setContentTitle("Alerta de caída!");
                        builder.setContentText("Se ha detectado una caída!");
                        builder.setSubText("Toque para ver más información WWM.");

                        //Enviando notificación
                        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.notify(notification_id,builder.build());

                    }

                    /*
                    Intent send = new Intent(getApplicationContext(), lecturaAcc.class);
                    send.putExtra("data", sbprint);
                    send.setAction("broadcastReceiver");
                    sendBroadcast(send);
                    //PendingIntent pendingIntent = PendingIntent.getActivity(alertDispatcher.this,0,send,0);*/
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
