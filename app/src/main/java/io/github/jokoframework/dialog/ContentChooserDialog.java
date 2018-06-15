package io.github.jokoframework.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;

import io.github.jokoframework.R;

/**
 * Created by Alejandro on 08/06/18.
 */

public class ContentChooserDialog extends Dialog {
    private List<String> mList;
    private Context mContext;
    private Spinner mSpinner;
    private DialogListener mReadyListener;

    public interface DialogListener {
        void ready(int n);

        void cancelled();
    }

    public ContentChooserDialog(Context context, List<String> list, DialogListener readyListener) {
        super(context);
        mReadyListener = readyListener;
        mContext = context;
        mList = list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_content_chooser);
        mSpinner = (Spinner) findViewById(R.id.dialog_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, mList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        Button buttonOK = (Button) findViewById(R.id.dialogOK);
        Button buttonCancel = (Button) findViewById(R.id.dialogCancel);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int n = mSpinner.getSelectedItemPosition();
                mReadyListener.ready(n);
                ContentChooserDialog.this.dismiss();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mReadyListener.cancelled();
                ContentChooserDialog.this.dismiss();
            }
        });
    }
}
