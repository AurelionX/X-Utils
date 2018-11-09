package me.aurelion.x.utils;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * X-Utils Intent 相关
 *
 * @author Leon (wshk729@163.com)
 * @date 2018/11/2
 */
public final class IntentUtils {

    private IntentUtils() {
        throw new UnsupportedOperationException("No instantiate " + getClass().getSimpleName());
    }

    /**
     * 判断意图是否可用
     */
    public static boolean isAvailable(final Intent intent) {
        return intent != null && Utils.getApp().getPackageManager().queryIntentActivities(intent, 0).size() > 0;
    }

    /**
     * 获取桌面的意图
     */
    public static Intent getHomeIntent() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        return intent;
    }

    /**
     * 获取分享文本的意图
     *
     * @param content 文本内容
     */
    public static Intent getShareTextIntent(@NonNull final String content) {
        return getShareTextIntent(content, false);
    }

    /**
     * 获取分享文本的意图
     *
     * @param content 文本内容
     */
    public static Intent getShareTextIntent(@NonNull final String content, final boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        return getIntent(intent, isNewTask);
    }

    /**
     * 获取分享图片的意图
     *
     * @param content   文本内容
     * @param imagePath 图片路径
     */
    public static Intent getShareImageIntent(final String content, final String imagePath) {
        return getShareImageIntent(content, imagePath, false);
    }

    /**
     * 获取分享图片的意图
     *
     * @param content   文本内容
     * @param imagePath 图片路径
     */
    public static Intent getShareImageIntent(final String content, final String imagePath, final boolean isNewTask) {
        if (imagePath == null || imagePath.length() == 0) {
            return null;
        }
        return getShareImageIntent(content, new File(imagePath), isNewTask);
    }

    /**
     * 获取分享图片的意图
     *
     * @param content 文本内容
     * @param image   图片文件
     */
    public static Intent getShareImageIntent(final String content, final File image) {
        return getShareImageIntent(content, image, false);
    }

    /**
     * 获取分享图片的意图
     *
     * @param content 文本内容
     * @param image   图片文件
     */
    public static Intent getShareImageIntent(final String content, final File image, final boolean isNewTask) {
        if (image == null || !image.isFile()) {
            return null;
        }
        return getShareImageIntent(content, ConvertUtils.file2Uri(image), isNewTask);
    }

    /**
     * 获取分享图片的意图
     *
     * @param content 文本内容
     * @param uri     图片Uri
     */
    public static Intent getShareImageIntent(final String content, final Uri uri) {
        return getShareImageIntent(content, uri, false);
    }

    /**
     * 获取分享图片的意图
     *
     * @param content 文本内容
     * @param uri     图片Uri
     */
    public static Intent getShareImageIntent(final String content, final Uri uri, final boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/*");
        return getIntent(intent, isNewTask);
    }

    /**
     * 获取分享图片的意图
     *
     * @param content    文本内容
     * @param imagePaths 图片路径集合
     */
    public static Intent getShareImageIntent(final String content, final LinkedList<String> imagePaths) {
        return getShareImageIntent(content, imagePaths, false);
    }

    /**
     * 获取分享图片的意图
     *
     * @param content    文本内容
     * @param imagePaths 图片路径集合
     */
    public static Intent getShareImageIntent(final String content, final LinkedList<String> imagePaths, final boolean isNewTask) {
        if (imagePaths == null || imagePaths.isEmpty()) {
            return null;
        }
        List<File> files = new ArrayList<>();
        for (String imagePath : imagePaths) {
            files.add(new File(imagePath));
        }
        return getShareImageIntent(content, files, isNewTask);
    }

    /**
     * 获取分享图片的意图
     *
     * @param content 文本内容
     * @param images  图片文件集合
     */
    public static Intent getShareImageIntent(final String content, final List<File> images) {
        return getShareImageIntent(content, images, false);
    }

    /**
     * 获取分享图片的意图
     *
     * @param content 文本内容
     * @param images  图片文件集合
     */
    public static Intent getShareImageIntent(final String content, final List<File> images, final boolean isNewTask) {
        if (images == null || images.isEmpty()) {
            return null;
        }
        ArrayList<Uri> uris = new ArrayList<>();
        for (File image : images) {
            if (!image.isFile()) {
                continue;
            }
            uris.add(ConvertUtils.file2Uri(image));
        }
        return getShareImageIntent(content, uris, isNewTask);
    }

    /**
     * 获取分享图片的意图
     *
     * @param content 文本内容
     * @param uris    图片Uri集合
     */
    public static Intent getShareImageIntent(final String content, final ArrayList<Uri> uris) {
        return getShareImageIntent(content, uris, false);
    }

    /**
     * 获取分享图片的意图
     *
     * @param content 文本内容
     * @param uris    图片Uri集合
     */
    public static Intent getShareImageIntent(final String content, final ArrayList<Uri> uris, final boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.setType("image/*");
        return getIntent(intent, isNewTask);
    }

    private static Intent getIntent(final Intent intent, final boolean isNewTask) {
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }

}
