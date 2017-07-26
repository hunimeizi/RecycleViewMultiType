package com.recycleview.multitype.utils;

/**
 * Android M 新增了几个方法 对权限进行动态管理
 * 其中 
 * checkSelfPermission(String permission) 是Context 方法
 * shouldShowRequestPermissionRationale(String permission) 是Activity 方法
 * requestPermissions(String permission) 是Activity 方法
 *
 * 动态声请权限成功后 会回调Activity 方法
 * onRequestPermissionsResult(int requestCode,
 *                             @NonNull String permissions[],
 *                             @NonNull int[] grantResults)
 * 这里对  requestCode 进行统一管理  ACCESS_XXXX_PERMISSIONS_REQUEST = XXX
 *
 * 下面 onRequestPermissionsResult()方法的使用示例 可供参考
 * @Override
 * public void onRequestPermissionsResult(int requestCode,
 @NonNull String permissions[],
 @NonNull int[] grantResults) {
 if (requestCode == ACCESS_XXXX_PERMISSIONS_REQUEST) {
 if (grantResults.length == 1 &&
 grantResults[0] == PackageManager.PERMISSION_GRANTED) {
 //声请权限成功
 } else {
 //声请权限失败
 }
 } else {
 super.onRequestPermissionsResult(requestCode, permissions, grantResults);
 }
 }
  *
  * */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.recycleview.multitype.R;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("InlinedApi")
public class MPermissionsManager
{
    private static final String TAG = "MPermissionsManager";

    public static final int ACCESS_CAMERA_PERMISSIONS_REQUEST = 1;

    public static final int ACCESS_MEI_PERMISSIONS_REQUEST = 2;

    public static final int ACCESS_PHONE_STATE_PERMISSIONS_REQUEST = 3;

    public static final int ACCESS_PHONE_STATE_SETTING_PERMISSIONS_REQUEST = 4;

    public static final int ACCESS_UPDATE_PERMISSIONS_REQUEST = 5;

    public static final int ACCESS_LOCATION_N_PERMISSIONS_REQUEST = 6;

    public static final int ACCESS_CONTACTS_CHARGE_A_PERMISSIONS_REQUEST = 7;

    public static final int ACCESS_CONTACTS_CHARGE_M_PERMISSIONS_REQUEST = 8;

    public static final int ACCESS_CONTACTS_FLOW_A_PERMISSIONS_REQUEST = 9;

    public static final int ACCESS_CONTACTS_FLOW_M_PERMISSIONS_REQUEST = 10;

    public static final int ACCESS_READWRITE_EXTERNAL_STORAGE_PERMISSIONS_REQUEST = 11;

    public static final int ACCESS_RELOCATE_PERMISSIONS_REQUEST = 12;

    public static final int RATIONALE_NONE = 0;

    public static final int RATIONALE_DIALOG = 1;

    public static final int RATIONALE_EXT1 = 2;

    private volatile static MPermissionsManager instance;

    public static MPermissionsManager getInstance()
    {
        if (null == instance)
        {
            synchronized (MPermissionsManager.class)
            {
                if (null == instance)
                {
                    instance = new MPermissionsManager();
                }
            }
        }
        return instance;
    }

    /**
     * @Manifest.permission.CAMERA
     * */
    public boolean requestCameraPermission(Activity activity)
    {
        return requestSelfPermission(activity, new String[]
                { Manifest.permission.CAMERA }, R.string.isw_permission_rationale_camera, ACCESS_CAMERA_PERMISSIONS_REQUEST);
    }

    public boolean checkExternalWritePermission(Context context)
    {
        return checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
    public boolean checkExternalReadPermission(Context context)
    {
        return checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     * @Manifest.permission.READ_EXTERNAL_STORAGE
     * @Manifest.permission.WRITE_EXTERNAL_STORAGE
     * */
    public boolean requestExternalStoragePermission(Activity activity, int requestCode)
    {
        String []  permission = new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE };
        return requestSelfPermission(activity, permission, R.string.isw_permission_rationale_storage, requestCode);
    }

    /**
     * @Manifest.permission.ACCESS_FINE_LOCATION
     * @Manifest.permission.ACCESS_COARSE_LOCATION
     * */
    public boolean requestLocationPermission(Activity activity, int requestCode)
    {

        boolean granted = requestSelfPermission(activity, new String[]
                        { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION },
                R.string.isw_permission_location, requestCode);
        return granted;
    }

    /**
     * @Manifest.permission.READ_CONTACTS
     * */
    public boolean requestContectsPermission(Activity activity, int requestCode)
    {
        return requestSelfPermission(activity, new String[]
                { Manifest.permission.READ_CONTACTS }, R.string.isw_permission_rationale_contects, requestCode);
    }

    /**
     * @Manifest.permission.PHONE_STATE
     * */
    public boolean requestPhoneStatePermission(Activity activity, int requestCode)
    {
        return requestSelfPermission(activity, new String[]
                { Manifest.permission.READ_PHONE_STATE }, R.string.isw_permission_rationale_phonestate, requestCode);
    }

    public boolean checkPhoneStatePermission(Context context)
    {
        return checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
    }

    public boolean checkSelfPermission(Context context, String permission)
    {

        if (null == context)
        {
            return false;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            return true;
        }

        return PackageManager.PERMISSION_GRANTED == context.checkSelfPermission(permission);

    }

    @SuppressLint("NewApi")
    private boolean requestSelfPermission(Activity activity, String[] permissions, int messageId, int requestCode)
    {
        if (null == activity)
        {
            return false;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            return true;
        }

        List<String> permissionsList = new ArrayList<String>();
        for (int i = 0; i < permissions.length; i++)
        {
            if (!checkSelfPermission(activity, permissions[i]))
            {
                permissionsList.add(permissions[i]);
            }
        }

        if (0 < permissionsList.size())
        {

            for (int i = 0; i < permissionsList.size(); i++)
            {
                if (activity.shouldShowRequestPermissionRationale(permissionsList.get(i)))
                {
                    break;
                }
            }

            String[] reqPermissons = permissionsList.toArray(new String[permissionsList.size()]);
            activity.requestPermissions(reqPermissons, requestCode);

        }
        else
        {
            return true;
        }

        return false;
    }
}
