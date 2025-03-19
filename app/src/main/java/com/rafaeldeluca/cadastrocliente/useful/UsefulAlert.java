package com.rafaeldeluca.cadastrocliente.useful;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.rafaeldeluca.cadastrocliente.R;

public final class UsefulAlert {

    private UsefulAlert () {
        // to avoid class to be instantiated
    }

    public static void showAlertDialog (Context context, String message,
                                    DialogInterface.OnClickListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.warning);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage(message);
        /*
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // action on click button
            }
        });
        */
        builder.setNeutralButton(R.string.ok, listener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showAlertDialog (Context context, int messageId,
                                  DialogInterface.OnClickListener listener) {
        showAlertDialog(context, context.getString(messageId), listener );
    }

    public static void showAlertDialog (Context context, int messageId) {
        showAlertDialog(context, context.getString(messageId), null );
    }
}
