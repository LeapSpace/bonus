package com.huilianjk.bonus.util.httpclient;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 * 示例内容包括：生成公私钥对、将公私钥写入文件、从文件读取公私钥、签名、对签名串做BASE64编码和解码、验签
 * 可参考https://docs.oracle.com/javase/tutorial/security/apisign/index.html
 */
public class MyRSA {
    public static final int PRIVATE = 0;
    public static final int PUBLIC = 1;

    // 从文件中读取密钥，私钥应为PKCS#8 DER格式，公钥应为X.509 DER格式
    private static Key getKeyFromFile(String filename, int type) throws FileNotFoundException {
        FileInputStream fis = null;
        fis = new FileInputStream(filename);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b;
        try {
            while ((b = fis.read()) != -1) {
                baos.write(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] keydata = baos.toByteArray();

        Key key = null;
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            switch (type) {
                case PRIVATE:
                    PKCS8EncodedKeySpec encodedPrivateKey = new PKCS8EncodedKeySpec(keydata);
                    key = kf.generatePrivate(encodedPrivateKey);
                    return key;
                case PUBLIC:
                    X509EncodedKeySpec encodedPublicKey = new X509EncodedKeySpec(keydata);
                    key = kf.generatePublic(encodedPublicKey);
                    return key;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return key;
    }

    public static void main(String[] args) throws IOException {
        try {

            //初始化
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");

            //生成RSA公私钥对
            System.out.println("生成公私钥对...");
            int KEY_LENGTH = 1024;//密钥长度，默认1024
            gen.initialize(KEY_LENGTH);
            System.out.println("\tRSA密钥长度：" + KEY_LENGTH);
            KeyPair pair = gen.generateKeyPair();
            PublicKey publicKey = pair.getPublic();
            PrivateKey privateKey = pair.getPrivate();
            System.out.println("\t公钥格式: " + publicKey.getFormat() + ", 私钥格式: " + privateKey.getFormat());
//            System.out.println("publicKey: " + new String(publicKey.getEncoded()));
//            System.out.println("privateKey: " + new String(privateKey.getEncoded()));

            //将私钥按默认格式编码写入到普通文件中
            System.out.println("\t公钥写入PublicKeyPKCS8.der文件，私钥写入PrivateKeyX509.der文件");
            FileOutputStream privateKeyFile = new FileOutputStream("PrivateKeyPKCS8.der");
            privateKeyFile.write(privateKey.getEncoded()); //对私钥调用getEncoded()获得的是PKCS#8 DER格式
            privateKeyFile.close();
            //将公钥按默认格式编码写入到普通文件中
            FileOutputStream publicKeyFile = new FileOutputStream("PublicKeyX509.der");
            publicKeyFile.write(publicKey.getEncoded()); //对公钥调用getEncoded()获得的是X.509 DER格式
            publicKeyFile.close();
            System.out.println("密钥生成完毕！\r\n");


            //签名示例
            System.out.println("签名示例...");
            //从文件中读取私钥
            PrivateKey privateKeyLoadedFromFile = (PrivateKey) getKeyFromFile("PrivateKeyPKCS8.der", PRIVATE);
//            PrivateKey privateKeyLoadedFromFile =(PrivateKey)getKeyFromFile("rsa_private_key_pkcs8.der",PRIVATE);
//            System.out.println("\t从文件中读取私钥...\r\n\t验证：从文件中读取的私钥是否与此前生成的私钥一致："+privateKeyLoadedFromFile.equals(privateKey));
            System.out.println("\t从文件中读取私钥...");

            //初始化签名算法
            Signature sign = Signature.getInstance("SHA256withRSA");//建议SHA256withRSA
            System.out.println("\t签名算法: " + sign.getAlgorithm());
//            sign.initSign(privateKey);//指定签名所用私钥
            sign.initSign(privateKeyLoadedFromFile);//指定使用从文件中读取的私钥

            byte[] data = "Hello World!".getBytes();//待签名明文数据
            System.out.println("\t待签名的明文串: " + new String(data));
            //更新用于签名的数据
            sign.update(data);
            //签名
            byte[] signature = sign.sign();
            //将签名signature转为BASE64编码，用于HTTP传输
            Base64 base64 = new Base64();
            String signatureInBase64 = base64.encodeToString(signature);
            System.out.println("\tBase64格式编码的签名串: " + signatureInBase64);
            System.out.println("签名示例结束！\r\n");


            //验签示例
            System.out.println("验签示例...");
            //从文件中读取公钥
            PublicKey publicKeyLoadedFromFile = (PublicKey) getKeyFromFile("PublicKeyX509.der", PUBLIC);
//            PublicKey publicKeyLoadedFromFile =(PublicKey)getKeyFromFile("rsa_public_key.der", PUBLIC);

//            System.out.println("\t从文件中读取公钥...\r\n\t\t验证：从文件中读取的公钥是否与此前生成的公钥一致：："+publicKeyLoadedFromFile.equals(publicKey));
            System.out.println("\t从文件中读取公钥...");

            //从Base64还原得到签名
            byte[] signatureFromBase64 = Base64.decodeBase64(signatureInBase64);
            System.out.println("\t从BASE64还原签名...\r\n\t\t验证：还原得到的签名是否与此前生成的签名一致：：" + Arrays.equals(signatureFromBase64, signature));


            //初始化验签算法
            Signature verifySign = Signature.getInstance("SHA256withRSA");
//            verifySign.initVerify(publicKey);
            //指定验签所用公钥
            verifySign.initVerify(publicKeyLoadedFromFile);

            //data为待验签的数据(明文)
            verifySign.update(data);
            System.out.println("\t待验签的明文串：" + new String(data) + " 签名（BASE64格式）：" + signatureInBase64);
            //验签
            boolean flag = verifySign.verify(signatureFromBase64);
            System.out.println("\t验签是否通过:" + flag);//true为验签成功
            System.out.println("验签示例结束！");


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}