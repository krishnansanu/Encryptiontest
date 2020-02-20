package test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class BigDataEnc {

	
	public static void main(String[] args) {
	    try {
	        
	        String passphrase="ThisIsASecretKey";
	        String text="This is a Java Sample Text";
	        
	        SecretKeySpec secretKey = new SecretKeySpec(passphrase.getBytes(), "AES");
	        
	        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	        byte[] encryptedText=cipher.doFinal(text.getBytes());
	        System.out.println("Encrypted Text - " + Base64.getEncoder().encodeToString(encryptedText));
	        
	        cipher.init(Cipher.DECRYPT_MODE, secretKey);
	        byte[] decryptedText=cipher.doFinal(encryptedText);
	        System.out.println("Decrypted Text - " + new String(decryptedText).trim());

//	        String str="1234567809876543";
	        String str="ThisIspassphrase";
	        StringBuffer sb = new StringBuffer();
	        //Converting string to character array
	        char ch[] = str.toCharArray();
	        byte[] keyBytes=new byte[ch.length];

	        for(int i = 0; i < ch.length; i++) {
	           String hexString = Integer.toHexString(ch[i]);
//	           System.out.print(hexString);
	           sb.append(hexString);
//	           
	           keyBytes[i]=  hexToByte(hexString);
	        }
	        
	        

	        String result = sb.toString();
//	        byte[] by = Arrays.copyOf(result.getBytes(), 16);
	        byte[] by=result.getBytes();
	        System.out.println(result);
//	        byte[] keyByte=Arrays.copyOf(result.getBytes(),16);
	        System.out.println("Automatic" + by.length);
	        for(byte b:by) {
	        	System.out.print(b);
	        }
//	        System.out.println();
//	        
//	        byte[] keyBytes = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};
//	        byte[] keyBytes = {0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x30, 0x39, 0x38, 0x37, 0x36, 0x35, 0x34, 0x33};
//	        byte[] keyBytes = {0x54, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79};
	        System.out.println("Manual" + keyBytes.length);
	        
	        for(byte b:keyBytes) {
	        	System.out.print(b);
	        }
	        
	        SecretKeySpec sks = new SecretKeySpec(keyBytes, "AES");
	        
	        Cipher dcipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	        dcipher.init(Cipher.DECRYPT_MODE, sks);
	        
	        // read file to byte[]
	        InputStream is = new FileInputStream("BigFileTest/latest.enc");
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        int b;
	        while ((b = is.read()) != -1) {
	            baos.write(b);
	        }
	        byte[] fileBytes = baos.toByteArray();
//
	        byte[] decrypted = dcipher.doFinal(fileBytes);
	        System.out.println(new String(decrypted));
//	        
	        
	        

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static byte hexToByte(String hexString) {
	    int firstDigit = toDigit(hexString.charAt(0));
	    int secondDigit = toDigit(hexString.charAt(1));
	    return (byte) ((firstDigit << 4) + secondDigit);
	}
	 
	private static int toDigit(char hexChar) {
	    int digit = Character.digit(hexChar, 16);
	    if(digit == -1) {
	        throw new IllegalArgumentException(
	          "Invalid Hexadecimal Character: "+ hexChar);
	    }
	    return digit;
	}
}
 