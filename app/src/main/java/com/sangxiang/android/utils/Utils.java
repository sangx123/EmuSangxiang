package com.sangxiang.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageView;

import com.sangxiang.android.App;
import com.sangxiang.android.R;
import com.sangxiang.android.network.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.sangxiang.android.utils.picasso.CircleTransform;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 09/08/2017  6:19 PM
 * Created by Zhang.
 */

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//    private static SimpleDateFormat formatter = new SimpleDateFormat();
    //new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static int seed = 0;
    public static int getRandomSeed(){
        if(seed++ == (Integer.MAX_VALUE-1)){
            seed = 0;
        }
        return seed;
    }

    public static boolean isMobileNO(String mobiles) {
        /*
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        }
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        */
        return true;
    }


    public static boolean notEmpty(String... s) {

        if (s == null || s.length == 0) {
            return false;
        }
        for (int i = 0; i < s.length; i++) {
            if (TextUtils.isEmpty(s[i])) {
                return false;

            }
        }
        return true;
    }

    public static String getMd5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String md5 = number.toString(16);

            while (md5.length() < 32)
                md5 = "0" + md5;

            return md5;
        } catch (NoSuchAlgorithmException e) {
            Log.e("MD5", e.getLocalizedMessage());
            return null;
        }
    }

    public static void deleteUserInApp() {
//        App.getInstance().getDaoSession().getUserDao().deleteAll();
        Constants.setLoginUser(null);
    }

    public static void writeUsefulLog(String s) {
        FileWriter fw = null;
        try {
            File f = new File(Environment.getExternalStorageDirectory(), "json.txt");
//            LogUtil.e(TAG, "onSuccess: -->"+f.getAbsolutePath() );
            fw = new FileWriter(f);
            fw.write(s);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getDateFromPattern(long date, String pattern) {

//        SimpleDateFormat format = new SimpleDateFormat("HH:mm",Locale.getDefault());
        formatter.applyLocalizedPattern(pattern);
//        formatter.applyPattern(pattern);
        return formatter.format(new Date(date));


//        String format = SimpleDateFormat.getInstance().format(new Date(date),null,new FieldPosition());
//        return formatter.format(new Date(date));
    }

    public static String getHHmmFromDate(long date) {
        formatter.applyLocalizedPattern("HH:mm");
        return formatter.format(new Date(date));
    }

    public static String getDefaultDateFormat(long date) {
        formatter.applyLocalizedPattern("yyyy年MM月dd日");
        return formatter.format(new Date(date));
    }

    public static String getDateFromPattern(Date date, String pattern) {
        formatter.applyLocalizedPattern(pattern);
        return formatter.format(date);
    }

    public static long getDateLong(String date, String pattern) {
        formatter.applyLocalizedPattern(pattern);
        try {
            return formatter.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Map<String, String> getMerchantMap(String merchantMap) {

        if (!TextUtils.isEmpty(merchantMap)) {
            try {
                JSONObject jsonObject = new JSONObject(merchantMap);
                Gson gson = new Gson();
                return gson.fromJson(merchantMap, new TypeToken<Map<String, String>>() {
                }.getType());

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        return null;
    }


    //loadImageOrUserNameOnIV
    @Deprecated
    public static void loadHeadPicToVh( String headPicUrl, ImageView imageView) {
        if (!TextUtils.isEmpty(headPicUrl)) {
            Picasso.with().load(headPicUrl).transform(new CircleTransform()).placeholder(R.drawable.icon_head_not_load).error(R.drawable.icon_head_not_load).into(imageView);
        } else {
           Picasso.with().load(R.drawable.icon_head_not_load).placeholder(R.drawable.icon_head_not_load).error(R.drawable.icon_head_not_load).into(imageView);
        }
    }

    public static void loadNormalPicToIv( String url, ImageView imageView) {
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(url.trim())) {
            Picasso.with().load(url).noFade().fit().placeholder(R.mipmap.default_image).error(R.mipmap.default_image).into(imageView);
        } else {
            Picasso.with().load(R.mipmap.default_image).placeholder(R.mipmap.default_image).error(R.mipmap.default_image).into(imageView);
        }
    }

    public static String getApkDownloadPath(String version, boolean isTemp) {
        File dir = new File(App.mInstance.getExternalCacheDir(), "download");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File f = null;
        if (isTemp) {
            f = new File(dir, "emucoo_" + version + ".apk.tmp");
        } else {

            f = new File(dir, "emucoo_" + version + ".apk");
        }

        return f.getAbsolutePath();
    }

    public static void expandViewTouchDelegate(final View view, final int left, final int top,
                                               final int right, final int bottom) {

        ((View) view.getParent()).post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                view.setEnabled(true);
                view.getHitRect(bounds);

                bounds.top -= top;
                bounds.bottom += bottom;
                bounds.left -= left;
                bounds.right += right;

                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }

    public static String getVersionName() throws PackageManager.NameNotFoundException {
        return App.mInstance.getPackageManager().getPackageInfo(App.mInstance.getPackageName(), 0).versionName;
    }

    public static void install(Activity activity, String apkPath) {
        File f = new File(apkPath);
        if (f.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data;
            // 判断版本大于等于7.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities
                data = FileProvider.getUriForFile(App.mInstance, "com.emucoo.FileProvider", f);
                // 给目标应用一个临时授权
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                data = Uri.fromFile(f);
            }
            intent.setDataAndType(data, "application/vnd.android.package-archive");
            activity.startActivity(intent);


        }
    }

    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static String getDateString(@NonNull CalendarDay date) {
        int month = date.getMonth() + 1;
        return date.getYear() + "-" + (month < 10 ? "0" + month : month) + "-" + (date.getDay() < 10 ? "0" + date.getDay() : date.getDay());
    }

    public static String getLastName(String s) {
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        if(s.length() <=2){
            return s;
        }else{
            return s.substring(s.length()-2,s.length());
        }
    }

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);


    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    /**
     * 保存图片
     *
     * @param path
     * @param name
     * @param bitmap
     */
    public static void saveBitmap(String path, String name, Bitmap bitmap) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        File _file = new File(path + name);
        if (_file.exists()) {
            _file.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(_file);
            if (name != null && !"".equals(name)) {
                int index = name.lastIndexOf(".");
                if (index != -1 && (index + 1) < name.length()) {
                    String extension = name.substring(index + 1).toLowerCase();
                    if ("png".equals(extension)) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    } else if ("jpg".equals(extension)
                            || "jpeg".equals(extension)) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}

