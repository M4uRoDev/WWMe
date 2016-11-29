package mauriciopoblete.walkwithme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.BroadcastReceiver;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.punchthrough.bean.sdk.Bean;

public class lecturaAcc extends Activity {


    TextView editText;
    Button disconnect;
    private StringBuilder sb = new StringBuilder();
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura_acc);
        disconnect = (Button)(findViewById(R.id.button));
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        editText = (TextView)(findViewById(R.id.editText));


        final Bean beanConnect = (Bean) getIntent().getExtras().get("connect");
        final Intent returnLectura = new Intent(this, alertDispatcher.class);
        returnLectura.putExtra("connect", beanConnect);
        this.startService(returnLectura);

        BroadcastReceiver broad = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                disconnect.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                String data = intent.getExtras().getString("data");
                editText.setText(data);
            }
        };
        broad.goAsync();

        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopService(new Intent(lecturaAcc.this, alertDispatcher.class));
                finish();
            }
        });


    }

}
