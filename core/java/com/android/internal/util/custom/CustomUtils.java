/*
 * Copyright (C) 2017 The Android Open Source Project
 * Copyright (C) 2022 FlamingoOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.util.custom;

import android.app.AlertDialog;
import android.app.IActivityManager;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;

import com.android.internal.R;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.util.crdroid.Utils;

/**
 * Some custom utilities
 * @hide
 */
public class CustomUtils {
    public static boolean isPackageInstalled(Context context, String pkg, boolean ignoreState) {
        return Utils.isPackageInstalled(context, pkg, ignoreState);
    }

    public static boolean isPackageInstalled(Context context, String pkg) {
        return Utils.isPackageInstalled(context, pkg, true);
    }

    public static boolean isPackageEnabled(Context context, String packageName) {
       return Utils.isPackageEnabled(context, packageName);
    }

    public static void switchScreenOff(Context ctx) {
       Utils.switchScreenOff(ctx);
    }

    public static boolean deviceHasFlashlight(Context ctx) {
        return Utils.deviceHasFlashlight(ctx);
    }

    public static boolean hasNavbarByDefault(Context context) {
         return Utils.hasNavbarByDefault(context);
    }

    public static void showSettingsRestartDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.settings_restart_title)
                .setMessage(R.string.settings_restart_message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        restartSettings(context);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    public static void restartSettings(Context context) {
        new restartSettingsTask(context).execute();
    }

    private static class restartSettingsTask extends AsyncTask<Void, Void, Void> {
        private Context mContext;

        public restartSettingsTask(Context context) {
            super();
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                ActivityManager am =
                        (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
                IActivityManager ams = ActivityManager.getService();
                for (ActivityManager.RunningAppProcessInfo app: am.getRunningAppProcesses()) {
                    if ("com.android.settings".equals(app.processName)) {
                    	ams.killApplicationProcess(app.processName, app.uid);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
