package me.aurelion.x.utils;

import android.os.Build;
import android.text.Html;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * X-Utils 编译相关
 *
 * @author Leon (wshk729@163.com)
 * @date 2018/11/9
 */
public class EncodeUtils {

    private EncodeUtils() {
        throw new UnsupportedOperationException("No instantiate " + getClass().getSimpleName());
    }

    /*
     *  ########## Url相关 ##########
     */

    /**
     * Url编译 ("UTF-8")
     *
     * @param content 内容
     * @return 编译内容
     */
    public static String urlEncode(final String content) {
        return urlEncode(content, "UTF-8");
    }

    /**
     * Url编译
     *
     * @param content     内容
     * @param charsetName 编码集
     * @return 编译内容
     */
    public static String urlEncode(final String content, final String charsetName) {
        if (content == null || content.length() == 0) {
            return "";
        }
        try {
            return URLEncoder.encode(content, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Url解译 ("UTF-8")
     *
     * @param content 内容
     * @return 解译内容
     */
    public static String urlDecode(final String content) {
        return urlDecode(content, "UTF-8");
    }

    /**
     * Url解译
     *
     * @param content     内容
     * @param charsetName 编码集
     * @return 解译内容
     */
    public static String urlDecode(final String content, final String charsetName) {
        if (content == null || content.length() == 0) {
            return "";
        }
        try {
            return URLDecoder.decode(content, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    /*
     *  ########## Base64相关 ##########
     */

    /**
     * Base64编译
     *
     * @param content 内容
     * @return 编译内容
     */
    public static byte[] base64Encode(final String content) {
        return base64Encode(content.getBytes());
    }

    /**
     * Base64编译
     *
     * @param content 内容
     * @return 编译内容
     */
    public static byte[] base64Encode(final byte[] content) {
        if (content == null || content.length == 0) {
            return new byte[0];
        }
        return Base64.encode(content, Base64.NO_WRAP);
    }

    /**
     * Base64编译
     *
     * @param content 内容
     * @return 编译内容
     */
    public static String base64Encode2String(final byte[] content) {
        if (content == null || content.length == 0) {
            return "";
        }
        return Base64.encodeToString(content, Base64.NO_WRAP);
    }

    /**
     * Base64解译
     *
     * @param content 内容
     * @return 解译内容
     */
    public static byte[] base64Decode(final String content) {
        if (content == null || content.length() == 0) {
            return new byte[0];
        }
        return Base64.decode(content, Base64.NO_WRAP);
    }

    /**
     * Base64解译
     *
     * @param content 内容
     * @return 解译内容
     */
    public static byte[] base64Decode(final byte[] content) {
        if (content == null || content.length == 0) {
            return new byte[0];
        }
        return Base64.decode(content, Base64.NO_WRAP);
    }

    /*
     *  ########## Html相关 ##########
     */

    /**
     * Html编译
     *
     * @param content 内容
     * @return 编译内容
     */
    public static String htmlEncode(final CharSequence content) {
        if (content == null || content.length() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        char c;
        for (int i = 0, len = content.length(); i < len; i++) {
            c = content.charAt(i);
            switch (c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '\'':
                    sb.append("&#39;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Html解译
     *
     * @param content 内容
     * @return 解译内容
     */
    @SuppressWarnings("deprecation")
    public static CharSequence htmlDecode(final String content) {
        if (content == null || content.length() == 0) {
            return "";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(content);
        }
    }

}
