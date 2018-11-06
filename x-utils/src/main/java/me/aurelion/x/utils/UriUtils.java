package me.aurelion.x.utils;

import android.content.ContentResolver;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import java.io.File;


/**
 * X-Utils Uri 相关
 *
 * @author Leon (wshk729@163.com)
 * @date 2018/11/6
 */
public class UriUtils {

    private UriUtils() {
        throw new UnsupportedOperationException("No instantiate " + getClass().getSimpleName());
    }

    public static Uri file2Uri(@NonNull final File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = Utils.getApp().getPackageName() + ".x_utils.provider";
            return FileProvider.getUriForFile(Utils.getApp(), authority, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    public static File uri2File(@NonNull final Uri uri, final String columnName) {
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            return new File(uri.getPath());
        }
        CursorLoader cl = new CursorLoader(Utils.getApp());
        cl.setUri(uri);
        cl.setProjection(new String[]{columnName});
        Cursor cursor = null;
        try {
            cursor = cl.loadInBackground();
            int columnIndex = cursor.getColumnIndexOrThrow(columnName);
            cursor.moveToFirst();
            return new File(cursor.getString(columnIndex));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
