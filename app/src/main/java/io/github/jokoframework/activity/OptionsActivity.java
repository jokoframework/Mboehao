package io.github.jokoframework.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

;

import org.apache.commons.lang3.StringUtils;

import io.github.jokoframework.R;
import io.github.jokoframework.constants.AppConstants;
import io.github.jokoframework.utilities.AppUtils;
import io.github.jokoframework.utilities.SecurityUtils;

/**
 * Created by joaquin on 01/09/17.
 */

public class OptionsActivity extends Activity {

    private static final String LOG_TAG = OptionsActivity.class.getSimpleName();

    private Activity self;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        self = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        final EditText currentPasswordEdit = (EditText) findViewById(R.id.EditTextCurrentPassword);
        final EditText password1 = (EditText) findViewById(R.id.EditText_Pwd1);
        final EditText password2 = (EditText) findViewById(R.id.EditText_Pwd2);
        final TextView error = (TextView) findViewById(R.id.TextView_PwdProblem);

        password2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                checkNewPasswords(password1, password2, error);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //not needed...
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //not needed...
            }
        });

        password1.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (password2.getText() != null && StringUtils.isNotBlank(password2.getText().toString())) {
                    checkNewPasswords(password1, password2, error);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //not needed...
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //not needed...
            }
        });

        View acceptButton = findViewById(R.id.save_change_password);
        View refusetButton = findViewById(R.id.cancel_change_password);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentStoredPassword = SecurityUtils.decrypt(AppUtils.getPrefs(self, AppConstants.USER_PREFS_PW));
                Log.d(LOG_TAG, "Guardando contraseña");
                if (currentPasswordEdit.getText() != null) {
                    String currentPassword = currentPasswordEdit.getText().toString();
                    if (StringUtils.isBlank(currentPassword) || !currentPassword.equals(currentStoredPassword)) { // No es doble comprobante de que esta vacia la currentPassword.?
                        showCurrentPasswordWarning(error, currentPasswordEdit);
                    } else {
                        String strPassword1 = password1.getText().toString();
                        final String strPassword2 = password2.getText().toString();
                        if (strPassword1.equals(strPassword2)) {
                            doChangePassword(strPassword2, error, currentPasswordEdit);
                        }
                    }
                } else {
                    showCurrentPasswordWarning(error, currentPasswordEdit);
                }
            }
        });

        refusetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "No se guarda la contraseña");
                Intent intent = new Intent(self, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void checkNewPasswords(EditText password1, EditText password2, TextView error) {
        String strPass1 = password1.getText().toString();
        String strPass2 = password2.getText().toString();
        if (strPass1.equals(strPass2)
                && strPass1.trim().length() >= AppConstants.MIN_PASSWORD_LENGTH) {
            error.setText(R.string.settings_pwd_equal);
        } else {
            if (strPass1.trim().length() >= AppConstants.MIN_PASSWORD_LENGTH) {
                error.setText(R.string.settings_pwd_not_equal);
            } else {
                error.setText(R.string.settings_pwd_too_short);
            }
        }
    }

    protected void doChangePassword(final String strPassword2, TextView error, EditText currentPasswodEdit) {
        error.setText(null);
        currentPasswodEdit.setBackgroundColor(Color.GREEN);
        currentPasswodEdit.requestFocus();
    }

    protected void showCurrentPasswordWarning(TextView error, EditText currentPasswodEdit) {
        error.setText(R.string.current_password_warning);
        currentPasswodEdit.setBackgroundColor(getResources().getColor(R.color.warningColor));
        currentPasswodEdit.requestFocus();
    }

}
