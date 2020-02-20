import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.FileUtils;

public class FileDecrypt {
	
	public static void main(String args[]) throws Exception {
		InputStream cipherInputStream = null;
		try {
		    final StringBuilder output = new StringBuilder();
//		    final byte[] secretKey = javax.xml.bind.DatatypeConverter.parseHexBinary("E4A38479A2349177EAE6038A018483318350E7F5430BDC8F82F1974715CB54E5");
//		    final byte[] initVector = javax.xml.bind.DatatypeConverter.parseHexBinary("629E2E1500B6BA687A385D410D5B08E3");
		    
		    String privateKeyPEM = FileUtils.readFileToString(new File("id_rsa-pkcs8.pem"));
		    privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replaceAll("\\s", "");
		    byte[] privateKeyDER = Base64.getDecoder().decode(privateKeyPEM);
		    
		    final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//		    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKey, "AES"), new IvParameterSpec(initVector, 0, cipher.getBlockSize()));
		    
		    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(privateKeyDER, "AES"));
		    
		    
		    cipherInputStream = new CipherInputStream(new FileInputStream("plaintext.enc"), cipher);

		    final String charsetName = "UTF-8";

		    final byte[] buffer = new byte[8192];
		    int read = cipherInputStream.read(buffer);

		    while (read > -1) {
		        output.append(new String(buffer, 0, read, charsetName));
		        read = cipherInputStream.read(buffer);
		    }

		    System.out.println(output);
		} finally {
		    if (cipherInputStream != null) {
		        cipherInputStream.close();
		    }
		}
	}

}
