package test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class FileEnc {
	
	
	public static PublicKey loadPublicKey(String publicKeyFile) throws Exception {
		String publicKeyPEM = FileUtils.readFileToString(new File(publicKeyFile));
	    // strip of header, footer, newlines, whitespaces
	    publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replaceAll("\\s", "");
	    // decode to get the binary DER representation
	    byte[] publicKeyDER = Base64.getDecoder().decode(publicKeyPEM);
	    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	    PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyDER));
	    return publicKey;
	}
	
	public static PrivateKey loadPrivateKey(String privateKeyFile) throws Exception {
		String privateKeyPEM = FileUtils.readFileToString(new File(privateKeyFile));
	    // strip of header, footer, newlines, whitespaces
	    privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replaceAll("\\s", "");
	    // decode to get the binary DER representation
	    byte[] privateKeyDER = Base64.getDecoder().decode(privateKeyPEM);
	    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	    PKCS8EncodedKeySpec spec=new PKCS8EncodedKeySpec(privateKeyDER);
	    PrivateKey privateKey = keyFactory.generatePrivate(spec);
	    return privateKey;
	}

	
	public static void main(String args[]) throws Exception {
		
		String ALGORITH="RSA/ECB/NoPadding";
		ALGORITH="RSA/ECB/PKCS1Padding";
		String PUBLICKEY="publickey.pem";
		String PRIVATEKEY="privatekey-pkcs8.pem";
		
		//Load public and Private keys from File
		PublicKey publicKey = loadPublicKey(PUBLICKEY);
		PrivateKey privateKey = loadPrivateKey(PRIVATEKEY);
		
		
		Cipher cipher = Cipher.getInstance(ALGORITH);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		
		InputStream inputStream = new FileInputStream("testfile.txt");
	    try {
	        OutputStream outputStream = new CipherOutputStream(new FileOutputStream("test.enc"), cipher);
	        try {
	            IOUtils.copy(inputStream , outputStream);
	        } finally {
	            outputStream.close();
	        }
	    } finally {
	        inputStream.close();
	    }
	    
	    cipher.init(Cipher.DECRYPT_MODE, privateKey);
	    FileInputStream fileInputStream = new FileInputStream("test.enc");
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    IOUtils.copy(fileInputStream, baos);
	    byte[] decrypted = cipher.doFinal(baos.toByteArray());
	    System.out.println(new String(decrypted).trim());
	    FileOutputStream outputStream = new FileOutputStream("java.txt");
	    outputStream.write(decrypted);
	    outputStream.close();
	}

}
