package io.github.jokoframework.eula;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;

import io.github.jokoframework.R;
import io.github.jokoframework.dialog.SecurityDialog;
import io.github.jokoframework.mboehaolib.util.ImageUtils;

/**
 * Created by joaquin on 04/09/17.
 */
public class Eula {
    private static final String PREFERENCE_EULA_ACCEPTED = "eula.accepted";
    private static final String PREFERENCES_EULA = "eula";

    private Eula() {
    }

    /**
     * Displays the EULA if necessary. This method should be called from the onCreate()
     * method of your main Activity.
     *
     * @param activity The Activity to finish if the user rejects the EULA.
     * @return Whether the user has agreed already.
     */

    public static boolean show(final Activity activity) {
        final SharedPreferences preferences = activity.getSharedPreferences(PREFERENCES_EULA,
                Activity.MODE_PRIVATE);
        if (!preferences.getBoolean(PREFERENCE_EULA_ACCEPTED, false)) {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
            dialogBuilder.setCancelable(false);
            final View eulaView = View.inflate(activity, R.layout.activity_eula, null);

            WebView mWebView = (WebView) eulaView.findViewById(R.id.webviewEula);
            String eulaBodyText = "<html><body>"
                    + "<p align=\"justify\">"
                    + "<font size=\"" +
                    ImageUtils.getHtmlScaledFontSize(activity) +
                    "\">"
                    + activity.getString(R.string.eula_body)
                    + "</p> "
                    + "</body></html>";
            mWebView.loadData(eulaBodyText, "text/html; charset=utf-8", "utf-8");

            View acceptButton = eulaView.findViewById(R.id.eula_accept_btn);
            View refusetButton = eulaView.findViewById(R.id.eula_refuse_btn);
            final CheckBox checkBox = (CheckBox) eulaView.findViewById(R.id.checkBoxAcceptEula);
            dialogBuilder.setView(eulaView);
            final AlertDialog alert = dialogBuilder.create();
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    acceptClicked(checkBox, preferences, alert, activity);

                }
            });
            refusetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.dismiss();
                    refuse(activity);
                }
            });
            alert.show();
            return false;
        }
        return true;
    }

    private static void acceptClicked(CheckBox checkBox, SharedPreferences preferences, AlertDialog alert, Activity activity) {
        if (checkBox.isChecked()) {
            accept(preferences);
            alert.dismiss();
            SecurityDialog.show(activity);
        } else {
            new AlertDialog.Builder(activity)
                    .setTitle(activity.getString(R.string.eula_warning_title))
                    .setMessage(activity.getString(R.string.eula_warning_body))
                    .setNeutralButton(activity.getString(R.string.button_back),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    /*
                                    No hace nada por el momento...//TODO: Close app..
                                     */
                                }
                            }).show();
        }
    }

    private static void accept(SharedPreferences preferences) {
        preferences.edit().putBoolean(PREFERENCE_EULA_ACCEPTED, true).commit();
    }

    private static void refuse(Activity activity) {
        activity.finish();
    }


}

