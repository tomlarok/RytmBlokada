package com.blokada.rytmblokada;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by tomek on 26.01.18.
 */

public class MyAdmin extends DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent){
        Toast.makeText(context, "Device Admin : enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        Toast.makeText(context, "Device Admin : disabled", Toast.LENGTH_SHORT).show();
    }
}
