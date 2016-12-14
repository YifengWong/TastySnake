package com.example.stevennl.tastysnake.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
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
    private Context context;
    private DialogInterface.OnCancelListener cancelListener;

    /**
     * Initialize a dialog to show help information.
     *
     * @param context The context
     * @param cancelListener_ Called when the dialog is closed
     */
    public HelpDialog(Context context, @Nullable DialogInterface.OnCancelListener cancelListener_) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = context;
        this.cancelListener = cancelListener_;
        setOnCancelListener(cancelListener);
        init();
    }

    private void init() {
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_help, null);
        initInfoTxt(v);
        Window window = getWindow();
        if (window != null) {
            window.setContentView(v);
            window.setWindowAnimations(R.style.DialogAnim);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
    }

    private void initInfoTxt(View v) {
        TextView infoTxt = (TextView) v.findViewById(R.id.dialog_help_infoTxt);
        infoTxt.setText(String.format(context.getString(R.string.help_info),
                Config.DURATION_ATTACK));
        infoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpDialog.this.dismiss();
                if (cancelListener != null) {
                    cancelListener.onCancel(HelpDialog.this);
                }
            }
        });
    }
}
