package com.quote.cwotit.Common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by shabb on 7/29/2018.
 */

public class Utils {

    // Check network connectivity
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    //Validate email id
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    //Display Snackbar with action
    private static Snackbar snackbar;

    public static void snackBarWithAction(Activity activity, String value) {
        snackbar = Snackbar.make(activity.findViewById(android.R.id.content), value, Snackbar.LENGTH_INDEFINITE).setAction("OK ", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        // Changing message text color
        snackbar.setActionTextColor(Color.GREEN);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    //Display simple snackbar
    public static void simpleSnackBar(Activity activity, String value) {

        snackbar = Snackbar.make(activity.findViewById(android.R.id.content), value, Snackbar.LENGTH_SHORT);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }
}
