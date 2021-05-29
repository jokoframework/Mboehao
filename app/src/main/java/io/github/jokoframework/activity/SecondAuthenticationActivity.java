package io.github.jokoframework.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import io.github.jokoframework.R;
import io.github.jokoframework.mboehaolib.util.Utils;

public class SecondAuthenticationActivity extends BaseActivity {
    private EditText otpTextField;
    private String secret;
    private Activity thisActivity;
    public Activity thisActivity() {
        return thisActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeUI();
        Bundle b = getIntent().getExtras();
        assert b != null;
        secret = (String) b.get("SECRET");
    }

    private void initializeUI(){
        setContentView(R.layout.activity_second_authentication);
        HomeActivity.cancelAlarmServices(this);

        try {
            Intent mServiceIntent = new Intent(getBaseContext(), io.github.jokoframework.service.CountryHelper.class);
            getBaseContext().startService(mServiceIntent);
        } catch (RuntimeException e) {
            Utils.showToast(getBaseContext(), "Fallo");
        }

        otpTextField = findViewById(R.id.otp);
        findViewById(R.id.cancel).setOnClickListener(v -> {
            Toast.makeText(getBaseContext(), "Back to Login", Toast.LENGTH_LONG).show();
            finish();
        });

        findViewById(R.id.enter).setOnClickListener(this::otpValid);
    }

    private void otpValid(View view){
        RequestQueue queue = Volley.newRequestQueue(this);

        String host_name = getString(R.string.host_name);
        String end_point = getString(R.string.user_acces_URL);
        String url = host_name + end_point;

        final Context ctx = view.getContext();
        Intent intent = new Intent(ctx, HomeActivity.class);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
            // Mostrar el response (DEBUG)
            //Utils.showToast(getBaseContext(), String.format("Response is: "+ response.toString()));

            // Verificar login exitoso
            String loginSuccess;
            try {
                loginSuccess = response.getString("success");

                if (loginSuccess.equals("true")){
                    Toast.makeText(getBaseContext(), "Access Granted", Toast.LENGTH_LONG).show();
                    ctx.startActivity(intent);
                    ((Activity) ctx).finish();
                } else {
                    Utils.showToast(getBaseContext(), "Login failed.");
                    showProgress(false);
                    invalidOtp();
                }

            } catch (Exception e){
                Log.e("Log_Tag", "Error at JWT Login " + e.getMessage(), e);
            }
        }, error -> {
            showProgress(false);
            invalidOtp();
            Log.d("Error", "error => " + error.toString());
        }){
            //Prepara el refresh_token como header para ser enviado como request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("SEED_OTP_TOKEN", otpTextField.getText().toString());
                params.put("X-JOKO-AUTH", secret);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        postRequest.setShouldCache(false);
        queue.add(postRequest);

        //thisActivity().finish();
        showProgress(false);
    }
    /*
    private void otpValid(View view){
        final Context ctx = view.getContext();
        Intent intent = new Intent(ctx, HomeActivity.class);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.user_acces_URL);

        Map<String, String> params = new HashMap<>();
        params.put("OTP", otpTextField.getText().toString());

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                // Mostrar el response (DEBUG)
                //Utils.showToast(getBaseContext(), String.format("Response is: "+ response.toString()));

                // Verificar login exitoso
                String loginSuccess;
                try {
                    loginSuccess = response.getString("success");
                    if (loginSuccess.equals("true")){
                        Toast.makeText(getBaseContext(), otpTextField.getText().toString(), Toast.LENGTH_LONG).show();
                        ctx.startActivity(intent);
                        ((Activity) ctx).finish();
                    } else {
                        Utils.showToast(getBaseContext(), String.format("Login failed."));
                        showProgress(false);
                    }
                } catch (Exception e){
                    Log.e("Log_Tag", "Error at JWT Login " + e.getMessage(), e);
                };
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getBaseContext(), String.format("ERROR: "+ error.toString()));
                showProgress(false);
                invalidOtp();
            }
        }
        );


        // Add the request to the RequestQueue.
        postRequest.setShouldCache(false);
        queue.add(postRequest);

        //thisActivity().finish();
        showProgress(false);
    }
*/
    private void invalidOtp(){
        otpTextField.setError(String.format("%s", R.string.error_incorrect_credentials));
        otpTextField.requestFocus();
    }
}
