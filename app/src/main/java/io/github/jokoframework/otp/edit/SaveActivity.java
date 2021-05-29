package io.github.jokoframework.otp.edit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.github.jokoframework.R;
import io.github.jokoframework.mboehaolib.util.Utils;
import io.github.jokoframework.otp.token.Token;
import io.github.jokoframework.otp.token.TokenPersistence;

public class SaveActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save);

        final Token token = new TokenPersistence(this).get(getPosition());
        ((TextView) findViewById(R.id.issuer)).setText(token.getIssuer());
        ((TextView) findViewById(R.id.label)).setText(token.getLabel());

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveSeed(token);
                finish();
            }
        });
    }

    private void saveSeed(Token token){
        RequestQueue queue = Volley.newRequestQueue(this);

        // saveseed_URL url to request, change values/strings.xml
        String host_name = getString(R.string.host_name);
        String end_point = getString(R.string.user_acces_URL);
        String url = host_name + end_point;

        //Creates parameters
        Map<String, String> params = new HashMap();
        params.put("user", token.getLabel()); //<--the username on the token
        params.put("seed_otp", token.toUri().getQueryParameter("secret")); //<-- the secret

        //Creates the JSON Object with the parameters
        JSONObject parameters = new JSONObject(params);

        //Sends the request "parameters" to the "saveseed_URL" URL
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    //If the package was sent correctly
                    //Response for request
                    String saveSuccess;
                    try {
                        saveSuccess = response.getString("success"); //<-- Only the "success" parameter of the response
                        if (saveSuccess.equals("true")){
                            Utils.showToast(getBaseContext(), String.format("Save successful."));
                        } else {
                            Utils.showToast(getBaseContext(), String.format("Save failed."));
                        }
                    } catch (Exception e){
                        Log.e("Tag", "Error while Saving Seed: " + e.getMessage(), e);
                    };
                    Utils.showToast(getBaseContext(), String.format("Seed Saved"));
                }

            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    //If there was an Error
                    Utils.showToast(getBaseContext(), String.format("ERROR: "+ error.toString()));
                }
            }
        );
        postRequest.setShouldCache(false);
        queue.add(postRequest);
        finish();
    }
}
