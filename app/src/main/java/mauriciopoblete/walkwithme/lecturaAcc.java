package mauriciopoblete.walkwithme;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.BeanManager;
import com.punchthrough.bean.sdk.message.Acceleration;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.Callback;
import com.punchthrough.bean.sdk.message.DeviceInfo;
import com.punchthrough.bean.sdk.message.LedColor;
import com.punchthrough.bean.sdk.message.ScratchBank;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class lecturaAcc extends AppCompatActivity {

    TextView editText;
    String texto = null;
    Button disconnect;
    private StringBuilder sb = new StringBuilder();
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura_acc);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        final Bean beanConnect = (Bean) getIntent().getExtras().get("connect");
        editText = (TextView)(findViewById(R.id.editText));
        disconnect = (Button)(findViewById(R.id.button));
        final LedColor green = LedColor.create(0, 255, 0);
        final LedColor off = LedColor.create(0, 0, 0);

        final BeanListener beanListener = new BeanListener() {

            @Override
            public void onConnected() {
                beanConnect.setLed(green);
            }

            @Override
            public void onConnectionFailed() {

            }

            @Override
            public void onDisconnected() {
            }
            @Override
            public void onSerialMessageReceived(byte[] data) {
                beanConnect.setLed(off);
                spinner.setVisibility(View.GONE);
                String s = new String(data);
                texto = s;

                sb.append(s);                                                // append string
                int endOfLineIndex = sb.indexOf("\r\n");                            // determine the end-of-line
                if (endOfLineIndex > 0) {                                            // if end-of-line,
                    String sbprint = sb.substring(0, endOfLineIndex);               // extract string
                    sb.delete(0, sb.length());                                      // and clear
                    editText.setText(sbprint);
                }
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
        beanConnect.connect(this, beanListener);

        disconnect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                beanConnect.disconnect();
                finish();
            }
        });

    }
}
