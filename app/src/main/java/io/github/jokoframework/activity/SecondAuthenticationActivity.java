package io.github.jokoframework.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.github.jokoframework.R;
import io.github.jokoframework.mboehaolib.util.Utils;

public class SecondAuthenticationActivity extends BaseActivity {
    private EditText otpTextField;
    private Button enterButton, cancelButton;
    private Activity thisActivity;

    public Activity thisActivity() {
        return thisActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initializeUI();
    }

    private void initializeUI(){
        setContentView(R.layout.activity_second_authentication);
        HomeActivity.cancelAlarmServices(this);
        try {
            Intent mServiceIntent = new Intent(getBaseContext(), io.github.jokoframework.service.CountryHelper.class);
            getBaseContext().startService(mServiceIntent);
        } catch (RuntimeException e) {
            Utils.showToast(getBaseContext(), String.format("Fallo"));
        }

        otpTextField = (EditText) findViewById(R.id.otp);
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Back to Login", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpValid(v);
            }
        });
    }

    private void otpValid(View view){
        final Context ctx = view.getContext();
        Intent intent = new Intent(ctx, HomeActivity.class);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.jwt_URL);

        Map<String, String> params = new HashMap();
        params.put("otp", otpTextField.getText().toString());

        JSONObject parameters = new JSONObject(params);

        //Aqui va el envio JSON
        if (stubOtp()){
            Toast.makeText(getBaseContext(), otpTextField.getText().toString(), Toast.LENGTH_LONG).show();
            ctx.startActivity(intent);
            ((Activity) ctx).finish();
        }else {
            Toast.makeText(getBaseContext(), "OTP Invalido", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private boolean stubOtp(){
        return true;
    }
}
