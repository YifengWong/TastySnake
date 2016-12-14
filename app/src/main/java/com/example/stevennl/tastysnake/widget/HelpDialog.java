package com.example.stevennl.tastysnake.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.example.stevennl.tastysnake.Config;

/**
 * A dialog showing game help information.
 * Author: Yifeng Wong
 */
public class HelpDialog extends AlertDialog.Builder {
    private static final String TAG = "HelpDialog";

    private static final String TITLE = "Help";
    private static final String INFO = "1.通过蓝牙连接朋友，一起游戏吧！\n\n" +
            "2.请记住自己永远是红色的蛇！\n\n" +
            "3.时刻留意上方倒计时！确认自己是攻方还是守方，决定策略。" +
                "每隔" + Config.DURATION_ATTACK + "秒将会转换一次攻守能力。\n\n" +
            "4.两方产生碰撞，都将会判定为攻击者胜利！\n\n"+
            "5.吃到战场上的果子，会导致身体变长，导致自身处于劣势，无论是攻击者还是防守者都应尽量避免变长！\n\n"+
            "6.作为攻击者，请毫不犹豫地追击对手吧！\n\n"+
            "7.撞到自己的身体或者战场边界将会被判定为失败。";

    private DialogInterface.OnClickListener listener;
    private Context context;

    /**
     * Initialize a dialog to show help information.
     *
     * @param context The context
     * @param listener The listener which would be call when the dialog disappear
     */
    public HelpDialog(Context context, final DialogInterface.OnClickListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
        this.setPositiveButton("确定",  listener);
        setListener();

        this.setTitle(TITLE);
        this.setMessage(INFO);

        this.create();
    }

    private void setListener() {
        // set response for click the blank area.
        this.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                listener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
            }
        });

        // set to capture the back button press.
        this.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0) {
                    // Do nothing. Because it will call the cancel listener automatically.
                }
                return false;
            }
        });
    }


}
