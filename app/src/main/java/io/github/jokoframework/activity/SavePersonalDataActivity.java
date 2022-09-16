package io.github.jokoframework.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.github.jokoframework.R;
import io.github.jokoframework.mboehaolib.util.Utils;

public class SavePersonalDataActivity extends AppCompatActivity {
    public static final String KEY_PHONE_NUMBER = "phone_number";
    public static final String KEY_DATE = "date";
    public static final String KEY_ADDRESS = "address";
    private Button saveButton;
    private Button cancelButton;
    private EditText phoneNumberText;
    private EditText dateText;
    private EditText addressText;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_personal_data);

        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(phoneNumberText != null && phoneNumberText.getText().length() == 0){
            String savedPhoneNumber = Utils.getPrefs(this, KEY_PHONE_NUMBER);
            phoneNumberText.setText(savedPhoneNumber);
        }

        if(dateText != null && dateText.getText().length() == 0){
            String savedDate = Utils.getPrefs(this, KEY_DATE);
            dateText.setText(savedDate);
        }

        if(addressText != null && addressText.getText().length() == 0){
            String savedAddress = Utils.getPrefs(this, KEY_ADDRESS);
            addressText.setText(savedAddress);
        }

        initHandlers();
    }

    private void initUI() {
        phoneNumberText = findViewById(R.id.etPhoneNumber);
        dateText = findViewById(R.id.etDate);
        addressText = findViewById(R.id.etAddress);
        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);
        backButton = findViewById(R.id.back_button);

        initHandlers();
    }

    private void initHandlers() {
        saveButton.setOnClickListener(v -> saveTask());
        cancelButton.setOnClickListener(v -> backToHomeTask());
        backButton.setOnClickListener(v -> backToHomeTask());
    }


    public void saveTask() {
        Toast.makeText(SavePersonalDataActivity.this, "Saving personal data...", Toast.LENGTH_SHORT).show();

        if(phoneNumberText != null){
            String numberValue = phoneNumberText.getText().toString();
            Utils.addPrefs(SavePersonalDataActivity.this, KEY_PHONE_NUMBER, numberValue);
        }

        if(dateText != null){
            String dateValue = dateText.getText().toString();
            Utils.addPrefs(SavePersonalDataActivity.this, KEY_DATE, dateValue);
        }

        if(addressText != null){
            String addressValue = addressText.getText().toString();
            Utils.addPrefs(SavePersonalDataActivity.this, KEY_ADDRESS, addressValue);
        }

        backToHomeTask();
    }

    private void backToHomeTask() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}