package me.aurelion.x.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * X-Utils 加密相关
 *
 * @author Leon (wshk729@163.com)
 * @date 2018/11/9
 */
public class EncryptUtils {

    private EncryptUtils() {
        throw new UnsupportedOperationException("No instantiate " + getClass().getSimpleName());
    }

    /*
     *  ########## MD5相关 ##########
     */

    /**
     * MD5加密
     *
     * @param data 数据
     * @return 密文
     */
    public static String encryptMD52String(final String data) {
        if (data == null || data.length() == 0) {
            return "";
        }
        return encryptMD52String(data.getBytes());
    }

    /**
     * MD5加密
     *
     * @param data 数据
     * @param salt 盐
     * @return 密文
     */
    public static String encryptMD52String(final String data, final String salt) {
        if (data == null && salt == null) {
            return "";
        }
        if (salt == null) {
            return bytes2HexString(encryptMD5(data.getBytes()));
        }
        if (data == null) {
            return bytes2HexString(encryptMD5(salt.getBytes()));
        }
        return bytes2HexString(encryptMD5((data + salt).getBytes()));
    }

    /**
     * MD5加密
     *
     * @param data 数据
     * @return 密文
     */
    public static String encryptMD52String(final byte[] data) {
        return bytes2HexString(encryptMD5(data));
    }

    /**
     * MD5加密
     *
     * @param data 数据
     * @param salt 盐
     * @return 密文
     */
    public static String encryptMD52String(final byte[] data, final byte[] salt) {
        if (data == null && salt == null) {
            return "";
        }
        if (salt == null) {
            return bytes2HexString(encryptMD5(data));
        }
        if (data == null) {
            return bytes2HexString(encryptMD5(salt));
        }
        byte[] dataSalt = new byte[data.length + salt.length];
        System.arraycopy(data, 0, dataSalt, 0, data.length);
        System.arraycopy(salt, 0, dataSalt, data.length, salt.length);
        return bytes2HexString(encryptMD5(dataSalt));
    }

    /**
     * MD5加密
     *
     * @param data 数据
     * @return 密文
     */
    public static byte[] encryptMD5(final byte[] data) {
        return hashTemplate(data, "MD5");
    }

    private static byte[] hashTemplate(final byte[] data, final String algorithm) {
        if (data == null || data.length <= 0) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * MD5文件加密
     *
     * @param filePath 文件路径
     * @return 密文
     */
    public static String encryptMD5File2String(final String filePath) {
        File file = TextUtils.isEmpty(filePath) ? null : new File(filePath);
        return encryptMD5File2String(file);
    }

    /**
     * MD5文件加密
     *
     * @param filePath 文件路径
     * @return 密文
     */
    public static byte[] encryptMD5File(final String filePath) {
        File file = TextUtils.isEmpty(filePath) ? null : new File(filePath);
        return encryptMD5File(file);
    }

    /**
     * MD5文件加密
     *
     * @param file 文件
     * @return 密文
     */
    public static String encryptMD5File2String(final File file) {
        return bytes2HexString(encryptMD5File(file));
    }

    /**
     * MD5文件加密
     *
     * @param file 文件
     * @return 密文
     */
    public static byte[] encryptMD5File(final File file) {
        if (file == null) {
            return null;
        }
        FileInputStream fis = null;
        DigestInputStream digestInputStream;
        try {
            fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            digestInputStream = new DigestInputStream(fis, md);
            byte[] buffer = new byte[256 * 1024];
            while (true) {
                if (digestInputStream.read(buffer) <= 0) {
                    break;
                }
            }
            md = digestInputStream.getMessageDigest();
            return md.digest();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     *  ########## DES相关 ##########
     */

    /**
     * DES加密(Base64)
     *
     * @param data           明文
     * @param key            秘钥
     * @param transformation 转换: <i>DES/CBC/PKCS5Padding</i>.
     * @return 密文
     */
    public static byte[] encryptDES2Base64(final byte[] data,
                                           final byte[] key,
                                           final String transformation) {
        return base64Encode(encryptDES(data, key, transformation));
    }

    /**
     * DES加密(Hex)
     *
     * @param data           明文
     * @param key            秘钥
     * @param transformation 转换: <i>DES/CBC/PKCS5Padding</i>.
     * @return 密文
     */
    public static String encryptDES2HexString(final byte[] data,
                                              final byte[] key,
                                              final String transformation) {
        return bytes2HexString(encryptDES(data, key, transformation));
    }

    /**
     * DES加密
     *
     * @param data           明文
     * @param key            秘钥
     * @param transformation 转换: <i>DES/CBC/PKCS5Padding</i>.
     * @return 密文
     */
    public static byte[] encryptDES(final byte[] data,
                                    final byte[] key,
                                    final String transformation) {
        return symmetricTemplate(data, key, "DES", transformation, true);
    }

    /**
     * DES解密(Base64)
     *
     * @param data           密文
     * @param key            秘钥
     * @param transformation 转换: <i>DES/CBC/PKCS5Padding</i>.
     * @return 明文
     */
    public static byte[] decryptBase64DES(final byte[] data,
                                          final byte[] key,
                                          final String transformation) {
        return decryptDES(base64Decode(data), key, transformation);
    }

    /**
     * DES解密(Hex)
     *
     * @param data           密文
     * @param key            秘钥
     * @param transformation 转换: <i>DES/CBC/PKCS5Padding</i>.
     * @return 明文
     */
    public static byte[] decryptHexStringDES(final String data,
                                             final byte[] key,
                                             final String transformation) {
        return decryptDES(hexString2Bytes(data), key, transformation);
    }

    /**
     * DES解密(Hex)
     *
     * @param data           密文
     * @param key            秘钥
     * @param transformation 转换: <i>DES/CBC/PKCS5Padding</i>.
     * @return 明文
     */
    public static byte[] decryptDES(final byte[] data,
                                    final byte[] key,
                                    final String transformation) {
        return symmetricTemplate(data, key, "DES", transformation, false);
    }

    /*
     *  ########## AES相关 ##########
     */

    /**
     * AES加密(Base64)
     *
     * @param data           明文
     * @param key            秘钥
     * @param transformation 转换: <i>DES/CBC/PKCS5Padding</i>.
     * @return 密文
     */
    public static byte[] encryptAES2Base64(final byte[] data,
                                           final byte[] key,
                                           final String transformation) {
        return base64Encode(encryptAES(data, key, transformation));
    }

    /**
     * AES加密(Hex)
     *
     * @param data           明文
     * @param key            秘钥
     * @param transformation 转换: <i>DES/CBC/PKCS5Padding</i>.
     * @return 密文
     */
    public static String encryptAES2HexString(final byte[] data,
                                              final byte[] key,
                                              final String transformation) {
        return bytes2HexString(encryptAES(data, key, transformation));
    }

    /**
     * AES加密
     *
     * @param data           明文
     * @param key            秘钥
     * @param transformation 转换: <i>DES/CBC/PKCS5Padding</i>.
     * @return 密文
     */
    public static byte[] encryptAES(final byte[] data,
                                    final byte[] key,
                                    final String transformation) {
        return symmetricTemplate(data, key, "AES", transformation, true);
    }

    /**
     * AES解密(Base64)
     *
     * @param data           密文
     * @param key            秘钥
     * @param transformation 转换: <i>DES/CBC/PKCS5Padding</i>.
     * @return 明文
     */
    public static byte[] decryptBase64AES(final byte[] data,
                                          final byte[] key,
                                          final String transformation) {
        return decryptAES(base64Decode(data), key, transformation);
    }

    /**
     * AES解密(Hex)
     *
     * @param data           密文
     * @param key            秘钥
     * @param transformation 转换: <i>DES/CBC/PKCS5Padding</i>.
     * @return 明文
     */
    public static byte[] decryptHexStringAES(final String data,
                                             final byte[] key,
                                             final String transformation) {
        return decryptAES(hexString2Bytes(data), key, transformation);
    }

    /**
     * AES解密
     *
     * @param data           密文
     * @param key            秘钥
     * @param transformation 转换: <i>DES/CBC/PKCS5Padding</i>.
     * @return 明文
     */
    public static byte[] decryptAES(final byte[] data,
                                    final byte[] key,
                                    final String transformation) {
        return symmetricTemplate(data, key, "AES", transformation, false);
    }

    private static byte[] symmetricTemplate(final byte[] data,
                                            final byte[] key,
                                            final String algorithm,
                                            final String transformation,
                                            final boolean isEncrypt) {
        if (data == null || data.length == 0 || key == null || key.length == 0) {
            return null;
        }
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec);
            return cipher.doFinal(data);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     *  ########## RSA相关 ##########
     */

    /**
     * RSA加密(Base64)
     *
     * @param data           明文
     * @param key            秘钥
     * @param isPublicKey    是否为公钥
     * @param transformation 转换: <i>DES/CBC/PKCS5Padding</i>.
     * @return 密文
     */
    public static byte[] encryptRSA2Base64(final byte[] data,
                                           final byte[] key,
                                           final boolean isPublicKey,
                                           final String transformation) {
        return base64Encode(encryptRSA(data, key, isPublicKey, transformation));
    }

    /**
     * RSA加密(Hex)
     *
     * @param data           明文
     * @param key            秘钥
     * @param isPublicKey    是否为公钥
     * @param transformation 转换: <i>DES/CBC/PKCS5Padding</i>.
     * @return 密文
     */
    public static String encryptRSA2HexString(final byte[] data,
                                              final byte[] key,
                                              final boolean isPublicKey,
                                              final String transformation) {
        return bytes2HexString(encryptRSA(data, key, isPublicKey, transformation));
    }

    /**
     * RSA加密
     *
     * @param data           明文
     * @param key            秘钥
     * @param isPublicKey    是否为公钥
     * @param transformation 转换: <i>DES/CBC/PKCS5Padding</i>.
     * @return 密文
     */
    public static byte[] encryptRSA(final byte[] data,
                                    final byte[] key,
                                    final boolean isPublicKey,
                                    final String transformation) {
        return rsaTemplate(data, key, isPublicKey, transformation, true);
    }

    /**
     * RSA解密(Base64)
     *
     * @param data           密文
     * @param key            秘钥
     * @param isPublicKey    是否为公钥
     * @param transformation 转换: <i>DES/CBC/PKCS5Padding</i>.
     * @return 明文
     */
    public static byte[] decryptBase64RSA(final byte[] data,
                                          final byte[] key,
                                          final boolean isPublicKey,
                                          final String transformation) {
        return decryptRSA(base64Decode(data), key, isPublicKey, transformation);
    }

    /**
     * RSA解密(Hex)
     *
     * @param data           密文
     * @param key            秘钥
     * @param isPublicKey    是否为公钥
     * @param transformation 转换: <i>DES/CBC/PKCS5Padding</i>.
     * @return 明文
     */
    public static byte[] decryptHexStringRSA(final String data,
                                             final byte[] key,
                                             final boolean isPublicKey,
                                             final String transformation) {
        return decryptRSA(hexString2Bytes(data), key, isPublicKey, transformation);
    }

    /**
     * RSA解密
     *
     * @param data           密文
     * @param key            秘钥
     * @param isPublicKey    是否为公钥
     * @param transformation 转换: <i>DES/CBC/PKCS5Padding</i>.
     * @return 明文
     */
    public static byte[] decryptRSA(final byte[] data,
                                    final byte[] key,
                                    final boolean isPublicKey,
                                    final String transformation) {
        return rsaTemplate(data, key, isPublicKey, transformation, false);
    }

    private static byte[] rsaTemplate(final byte[] data,
                                      final byte[] key,
                                      final boolean isPublicKey,
                                      final String transformation,
                                      final boolean isEncrypt) {
        if (data == null || data.length == 0 || key == null || key.length == 0) {
            return null;
        }
        try {
            Key rsaKey;
            if (isPublicKey) {
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
                rsaKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);
            } else {
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
                rsaKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
            }
            if (rsaKey == null) {
                return null;
            }
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, rsaKey);
            int len = data.length;
            int maxLen = isEncrypt ? 117 : 128;
            int count = len / maxLen;
            if (count > 0) {
                byte[] ret = new byte[0];
                byte[] buff = new byte[maxLen];
                int index = 0;
                for (int i = 0; i < count; i++) {
                    System.arraycopy(data, index, buff, 0, maxLen);
                    ret = joins(ret, cipher.doFinal(buff));
                    index += maxLen;
                }
                if (index != len) {
                    int restLen = len - index;
                    buff = new byte[restLen];
                    System.arraycopy(data, index, buff, 0, restLen);
                    ret = joins(ret, cipher.doFinal(buff));
                }
                return ret;
            } else {
                return cipher.doFinal(data);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     *  ########## 相关方法 ##########
     */

    private static byte[] joins(final byte[] prefix, final byte[] suffix) {
        byte[] ret = new byte[prefix.length + suffix.length];
        System.arraycopy(prefix, 0, ret, 0, prefix.length);
        System.arraycopy(suffix, 0, ret, prefix.length, suffix.length);
        return ret;
    }

    private static final char HEX_DIGITS[] =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static String bytes2HexString(final byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        int len = bytes.length;
        if (len <= 0) {
            return "";
        }
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = HEX_DIGITS[bytes[i] >> 4 & 0x0f];
            ret[j++] = HEX_DIGITS[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    private static byte[] hexString2Bytes(String hexString) {
        if (TextUtils.isEmpty(hexString)) {
            return null;
        }
        int len = hexString.length();
        if (len % 2 != 0) {
            hexString = "0" + hexString;
            len = len + 1;
        }
        char[] hexBytes = hexString.toUpperCase().toCharArray();
        byte[] ret = new byte[len >> 1];
        for (int i = 0; i < len; i += 2) {
            ret[i >> 1] = (byte) (hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
        }
        return ret;
    }

    private static int hex2Dec(final char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - '0';
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static byte[] base64Encode(final byte[] input) {
        return Base64.encode(input, Base64.NO_WRAP);
    }

    private static byte[] base64Decode(final byte[] input) {
        return Base64.decode(input, Base64.NO_WRAP);
    }

}
