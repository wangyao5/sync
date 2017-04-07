package com.leecco.sync.utils;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class RSAUtils {
    /**
     * 生成非对称密钥对,密钥长度;<br/>
     * 通过 KeyPair.getPublic()获得公钥PublicKey;<br/>
     * 通过KeyPair.getPrivate()获得私钥PrivateKey;<br/>
     * 通过PublicKey(PrivateKey).getEncoded()获得Key的编码后的byte内容;<br/>
     * 其中 公钥是过X.509格式编码的， 私钥是过PKCS#8编码的， 恢复的时候也要过这两种编码格式恢复<br/>
     */
    public static KeyPair genKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        return kpg.genKeyPair();
    }

    /**
     * 通过byte数组恢复私钥<br/>
     * @param bytes 私钥编码后的数组<br/>
     * @return
     * @throws Exception
     */
    public static PrivateKey restorePrivateKey(byte[] bytes) throws Exception {
        PKCS8EncodedKeySpec pkcs = new PKCS8EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(pkcs);
    }

    /**
     * 通过byte数组恢复公钥
     * @param bytes 公钥编码后的数组
     * @return
     * @throws Exception
     */
    public static PublicKey restorePublicKey(byte[] bytes) throws Exception {
        X509EncodedKeySpec pkcs = new X509EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(pkcs);
    }

    /**
     * 利用非对称密钥加公钥加密的密文只能私钥解私钥加密的密文只能公钥解
     * @param src 要加密的明文
     * @param key 公钥或私钥
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] src, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(src);
    }

    /**
     * 利用非对称密钥解公钥加密的密文只能私钥解私钥加密的密文只能公钥解
     * @param dest 要解密的密文
     * @param key 公钥或私钥
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] dest, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(dest);
    }

    /**
     * 利用私钥签名
     * @param src 要签名的内容
     * @param prvKey 私钥
     * @return
     * @throws Exception
     */
    public static byte[] sign(byte[] src, PrivateKey prvKey) throws Exception {
        Signature sig = Signature.getInstance("MD5withRSA");
        sig.initSign(prvKey);
        sig.update(src);
        return sig.sign();
    }

    /**
     * 利用公钥验证签名
     * @param src 被签名的原内
     * @param dest 签名后的内容
     * @param pubKey 公钥
     * @return
     * @throws Exception
     */
    public static boolean verifySign(byte[] src, byte[] dest, PublicKey pubKey) throws Exception {
        Signature sig = Signature.getInstance("MD5withRSA");
        sig.initVerify(pubKey);
        sig.update(src);
        return sig.verify(dest);
    }

    /**
     * 大数据加RSA+AES加密
     * @param input 明文
     * @param key 非对称密公钥或私钥,公钥加密私钥解密,私钥加密公钥解密
     * @return
     * @throws Exception
     */
    public static byte[] encryptLarger(byte[] input, Key key) throws Exception {
        SecureRandom random = new SecureRandom();
        //取密钥长度为1286比特
        byte[] secretKey = new byte[16];
        random.nextBytes(secretKey);
        //生成密文长度为非对称密钥长度,102428比特
        byte[] encryptedSecretKey = encrypt(secretKey, key);
        //AES加密
        Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec sks = new SecretKeySpec(secretKey, "AES");
        aes.init(Cipher.ENCRYPT_MODE, sks);
        byte[] encryptedData = aes.doFinal(input);
        byte[] result = new byte[encryptedSecretKey.length + encryptedData.length];
        System.arraycopy(encryptedSecretKey, 0, result, 0, encryptedSecretKey.length);
        System.arraycopy(encryptedData, 0, result, encryptedSecretKey.length, encryptedData.length);
        return result;
    }

    /**
     * 大数据解RSA+AES解密
     * @param data 密文
     * @param key 非对称密公钥或私钥,公钥加密私钥解密,私钥加密公钥解密
     * @return
     * @throws Exception
     */
    public static byte[] decryptLarger(byte[] data, Key key) throws Exception {
        byte[] encryptedSecretKey = new byte[128];
        System.arraycopy(data, 0, encryptedSecretKey, 0, encryptedSecretKey.length);
        byte[] encryptedData = new byte[data.length - encryptedSecretKey.length];
        System.arraycopy(data, encryptedSecretKey.length, encryptedData, 0, encryptedData.length);
        // 恢复secretKey
        byte[] secretKey = decrypt(encryptedSecretKey, key);
        Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec sks = new SecretKeySpec(secretKey, "AES");
        aes.init(Cipher.DECRYPT_MODE, sks);
        return aes.doFinal(encryptedData);
    }

    public static byte[] encodeHmacSHA1(byte[] input, byte[] key) throws Exception{
        SecretKey secretKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(input);
    }

    public static byte[] initHmacSHA1Key() throws Exception{
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA1");
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }
}
