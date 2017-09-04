package io.github.jokoframework.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;

import com.example.simplerel.R;

public class SecurityDialog {

    private SecurityDialog() {
    }

    public static void show(final Activity activity) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setCancelable(false);
        final View infoView = View.inflate(activity, R.layout.activity_security_dialog, null);
        View acceptButton = infoView.findViewById(R.id.blockout_accept_btn);
        dialogBuilder.setView(infoView);
        final AlertDialog alert = dialogBuilder.create();
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });
        alert.show();
    }
}
