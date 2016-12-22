package mauriciopoblete.walkwithme;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by M4uRo on 17-12-16.
 */
public class ElderlySelector extends ListActivity {

    String idRedApoyo = "";

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);
        final ArrayAdapter<JSONObject> AdultosMayores = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        final String funcion = "getDataAdultosMayores";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.FUNCIONES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String error = jsonObject.getString("error");
                            JSONArray jsonArray = jsonObject.getJSONArray("adultos");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject adulto = jsonArray.getJSONObject(i);
                                AdultosMayores.add(adulto);
                                setListAdapter(AdultosMayores);
                            }
                            if(error.equalsIgnoreCase("true")){
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                return;
                            }else if(error.equalsIgnoreCase("false")){
                            }

                        }catch(JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Intent intent = getIntent();
                idRedApoyo = intent.getStringExtra("idRedApoyo");
                Map<String, String> params = new HashMap<>();
                params.put("funcion",funcion);
                params.put("idRedApoyo", idRedApoyo);
                //params.put("idRedApoyo", getIntent().getStringExtra("idRedApoyo"));
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String adulto = l.getAdapter().getItem(position).toString();
        try {
            JSONObject adultoSelected = new JSONObject(adulto);

            String idAdultoMayor = adultoSelected.getString("idAdultoMayor");

            Intent GoToRegisterDevice = new Intent(this, RouterDevicesWWM.class);
            GoToRegisterDevice.putExtra("idAdultoMayor",idAdultoMayor);
            startActivity(GoToRegisterDevice);

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


}
