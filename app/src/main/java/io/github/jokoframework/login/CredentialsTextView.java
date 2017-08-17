package io.github.jokoframework.login;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;


public class CredentialsTextView implements Textwatcher {
    private String LOG_TAG =  CredentialsTextView.class.getSimpleName();

    private EditText userTextField, passTextField;


    public CredentialsTextView(EditText userTextField, EditText passTextField) {
        this.userTextField = userTextField;
        this.passTextField = passTextField;
    }

    @Override
    public void userTextListener() {
        userTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(LOG_TAG, "Not implemented");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(LOG_TAG, "Not implemented");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Si cambia el texto del user field, ceramos el password
                passTextField.setText(null);
            }
        });
    }

}
