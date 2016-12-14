package com.example.stevennl.tastysnake.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.R;

/**
 * A dialog showing game help information.
 * Author: Yifeng Wong
 */
public class HelpDialog extends AlertDialog.Builder {
    private static final String TAG = "HelpDialog";
    private DialogInterface.OnCancelListener cancelListener;

    /**
     * Initialize a dialog to show help information.
     *
     * @param context The context
     * @param cancelListener_ Called when the dialog is closed
     */
    public HelpDialog(Context context, DialogInterface.OnCancelListener cancelListener_) {
        super(context);
        this.cancelListener = cancelListener_;
        setTitle(context.getString(R.string.help));
        setMessage(getMessage(context));
        setOnCancelListener(cancelListener);
        setPositiveButton(context.getString(R.string.confirm),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelListener.onCancel(dialog);
                    }
                });
        create();
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
