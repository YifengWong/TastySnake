package com.example.stevennl.tastysnake.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.R;


/**
 * A dialog showing game help information.
 * Author: Yifeng Wong
 */
public class HelpDialog extends Dialog {
    private static final String TAG = "HelpDialog";
    private DialogInterface.OnCancelListener cancelListener;

    private TextView infoTextView;

    /**
     * Initialize a dialog to show help information.
     *
     * @param context The context
     * @param cancelListener_ Called when the dialog is closed
     */
    public HelpDialog(Context context, DialogInterface.OnCancelListener cancelListener_) {
        super(context);
        this.cancelListener = cancelListener_;

        setLayout();
        setWindow();
    }

    private void setLayout() {
        final View view = LayoutInflater.from(this.getContext()).inflate(R.layout.dialog_help, null);
        infoTextView = (TextView) view.findViewById(R.id.dialog_help_text);
        infoTextView.setText(getMessage(this.getContext()));
        infoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpDialog.this.dismiss();
                cancelListener.onCancel(HelpDialog.this);
            }
        });

        setContentView(view);
        setOnCancelListener(this.cancelListener);
    }

    private void setWindow() {
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
    }

    /**
     * Return the dialog message.
     *
     * @param context The context
     */
    private String  getMessage(Context context) {
        return String.format(context.getString(R.string.help_info), Config.DURATION_ATTACK);
    }
}
