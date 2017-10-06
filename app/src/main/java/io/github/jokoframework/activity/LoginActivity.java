package io.github.jokoframework.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.simplerel.R;

import io.github.jokoframework.aplicationconstants.Constants;
import io.github.jokoframework.login.Authenticable;
import io.github.jokoframework.login.CredentialsTextView;
import io.github.jokoframework.login.ParseLogin;
import io.github.jokoframework.utilitys.SecurityUtils;
import io.github.jokoframework.utilitys.Utils;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import io.github.jokoframework.eula.Eula;

public class LoginActivity extends Activity {

    private Button enterButton;
    private EditText userTextField, passTextField;
    private CheckBox saveCredentials;
    private Activity mySelf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        HomeActivity.cancelAlarmServices(this);
        Fabric.with(this, new Crashlytics());
        mySelf = this;
        Eula.show(mySelf);
        final String decryptedUser = SecurityUtils.decrypt(Utils.getPrefs(this, Constants.USER_PREFS_USER));
        final String decryptedPassword = SecurityUtils.decrypt(Utils.getPrefs(this, Constants.USER_PREFS_PW));

        enterButton = (Button) findViewById(R.id.buttonEnter);
        userTextField = (EditText) findViewById(R.id.user);
        passTextField = (EditText) findViewById(R.id.pass);
        saveCredentials = (CheckBox) findViewById(R.id.checkBox);
        userTextField.setText(decryptedUser);
        passTextField.setText(decryptedPassword);

        //Si el usuario es diferente se tiene que vaciar el password...

        CredentialsTextView credentialsTextView = new CredentialsTextView(userTextField,passTextField);
        credentialsTextView.userTextListener();

        enterButton.setOnClickListener(new clickEnter());
    }

    private class clickEnter implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            final String username = userTextField.getText().toString(); // Enlace a la cadena de texto del valor del Usuario...
            final String password = passTextField.getText().toString(); // PasswordText enlace...
            final View progress = findViewById(R.id.progressMainWindow); // progress bar...
            Authenticable parseLogin = new ParseLogin(enterButton,progress, mySelf,saveCredentials);
            parseLogin.setPassword(password);
            parseLogin.setUser(username);
            parseLogin.saveCredentials();
            // HomeActivity progress
            progress.setVisibility(View.VISIBLE); // Muestra el progress bar mientras se obtine el acceso...
            parseLogin.authenticate();
        }
    }

}

