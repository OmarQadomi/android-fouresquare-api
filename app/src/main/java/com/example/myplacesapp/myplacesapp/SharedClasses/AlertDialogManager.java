package com.example.myplacesapp.myplacesapp.SharedClasses;

/**
 * Created by 1234485 on 3/25/2017.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.myplacesapp.myplacesapp.R;

public abstract class AlertDialogManager {

    public abstract void onPositiveButtonClickListener();

    public abstract void onNegativeButtonClickListener();

    /**
     * Function to display simple Alert Dialog
     *
     * @param context - application context
     * @param title   - alert dialog title
     * @param message - alert message
     * @param status  - success/failure (used to set icon)
     *                - pass null if you don't want icon
     */
    public static void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        if (status != null)
            // Setting alert dialog icon
            alertDialog.setIcon((status) ? R.drawable.success : R.drawable.error);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void showConfirmationDialog(Context context, String title, String message, String positiveButtonTitle, String negativButtonTitle) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        alertDialog.setIcon(R.drawable.confirmation);
        // On pressing Settings button
        alertDialog.setPositiveButton(positiveButtonTitle,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onPositiveButtonClickListener();
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton(negativButtonTitle,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        // Showing Alert Message
        alertDialog.show();

    }
}