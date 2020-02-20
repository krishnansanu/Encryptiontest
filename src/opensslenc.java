import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.apache.commons.io.FileUtils;

public class opensslenc {
	
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
	
	public static void main(String[] args) throws Exception {
		
		
		String ALGORITH="RSA/ECB/NoPadding";
		String PUBLICKEY="publickey.pem";
		String PRIVATEKEY="privatekey-pkcs8.pem";
		String PLAINTEXT = "SAMPLE JAVA ENCRYPTION TEST";
		System.out.println("Text Before Encryption - " + PLAINTEXT);
		
		PublicKey publicKey = loadPublicKey(PUBLICKEY);
		PrivateKey privateKey = loadPrivateKey(PRIVATEKEY);
		
		
//	    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
	    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	    byte[] encrypted = cipher.doFinal(PLAINTEXT.getBytes());
	    System.out.println("Encrypted Text : " + Base64.getEncoder().encodeToString(encrypted));

	    cipher.init(Cipher.DECRYPT_MODE, privateKey);
	    byte[] decrypted = cipher.doFinal(encrypted);
	    System.out.println("Decrypted Text : " + new String(decrypted).trim());
	    
	    
		
//	    File output = new File("output.txt");
//	    FileWriter writ= new FileWriter(output);
//	    writ.write(out);
//	    writ.flush();
	    		
	}

}
