package org.example.authorize.utils.generator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.commons.codec.binary.Base32;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Test {

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, IOException, WriterException, InterruptedException {
//        Base32 base32 = new Base32();

//        String k = "N7ABXHJ4J5TNICNIFWWGOLBL4S6QUAMBI3UZYBICV4TL7EK72LADHBC2FCXSRLCGH7QUZVUFBJSGJWNG3J26CX5S6OFNIJSINX2NNRQ=";

//        HmacOneTimePasswordGenerator generator = new HmacOneTimePasswordGenerator();

//        KeyGenerator keyGenerator = KeyGenerator.getInstance(generator.getAlgorithm());


        // SHA-1 and SHA-256 prefer 64-byte (512-bit) keys; SHA512 prefers 128-byte (1024-bit) keys
//        keyGenerator.init(512);
//
//        Key key = keyGenerator.generateKey();
////        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
//        String encodedKey = base32.encodeToString(key.getEncoded());
//        System.out.println("KEY: " + encodedKey);
//        System.out.println(key.getAlgorithm());

//        byte[] decodedKey = Base64.getDecoder().decode(k);
//        byte[] decodedKey = base32.decode(k);
//        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, generator.getAlgorithm());


//        System.out.println(generator.generateOneTimePassword(originalKey, 1));


//        TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator();
//        while (true) {
//            final Instant now = Instant.now();
//            final Instant later = now.plus(totp.getTimeStep());
//
//            System.out.format("Current password: %06d\n", totp.generateOneTimePassword(originalKey, now));
//            Thread.sleep(1000l);
//        }
//        System.out.format("Future password:  %06d\n", totp.generateOneTimePassword(originalKey, later));


//        String email = "test@gmail.com";
//        String companyName = "My Awesome Company";
//        String barCodeUrl = getGoogleAuthenticatorBarCode("QDWSM3OYBPGTEVSPB5FKVDM3CSNCWHVK", email, companyName);
//        System.out.println(barCodeUrl);
//
//        createQRCode(barCodeUrl, "/home/hungp/workspaces/learn-spring/authorize-with-spring/authorize-BE/qr", 200, 200);
        System.out.println(generateSecretKey());

//        HMACOneTimePasswordGenerator otpGen = new HMACOneTimePasswordGenerator();
//        System.out.println(otpGen.generateWithLongMovingFactor("QDWSM3OYBPGTEVSPB5FKVDM3CSNCWHVK".getBytes(StandardCharsets.UTF_8),
//                1, 6, false, 1));


    }


    public static String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }


    public static String getGoogleAuthenticatorBarCode(String secretKey, String account, String issuer) {
        try {
            return "otpauth://totp/"
                    + URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20")
                    + "?secret=" + URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20")
                    + "&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20")
                    + "&digits=" + URLEncoder.encode("8", "UTF-8").replace("+", "%20")
                    + "&period=" + URLEncoder.encode("40", "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void createQRCode(String barCodeData, String filePath, int height, int width)
            throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE,
                width, height);
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            MatrixToImageWriter.writeToStream(matrix, "png", out);
        }
    }
}
