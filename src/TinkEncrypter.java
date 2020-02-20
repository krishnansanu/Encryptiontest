import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import com.google.crypto.tink.Aead;
import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetReader;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.config.TinkConfig;
import com.google.crypto.tink.subtle.Hex;


public class TinkEncrypter {
	
	public KeysetHandle keysetHandle;
	public Aead aead;
	public String passphrase;
	public TinkEncrypter(String passphrase) {
		try {

			TinkConfig.register();
			keysetHandle =  CleartextKeysetHandle.read(JsonKeysetReader.withFile(new File("my_keyset7.json")));
			aead = keysetHandle.getPrimitive(Aead.class);
			this.passphrase=passphrase;
		}catch(Exception e) {
			System.out.println("Error in Initializing... " + e.getMessage());
		}
	}
	
	public String encryptText(String plainText) {
		String encryptedOutput="";
		try {
			byte[] ciphertext = aead.encrypt(plainText.getBytes(), passphrase.getBytes());
			encryptedOutput=Hex.encode(ciphertext);
		}catch(Exception e) {
			System.out.println("Error in encryption... " + e.getMessage());
		}
		return encryptedOutput;
	}
	
	public String decryptText(byte[] cipherText) {
		byte[] decryptedOutput = null;
		try {
			decryptedOutput=aead.decrypt(cipherText, passphrase.getBytes());
		}catch(Exception e) {
			System.out.println("Error in decryption... " + e.getMessage());
		}
		return new String(decryptedOutput);
	}
	
	public String decryptText(String hexValue) {
		byte[] decryptedOutput = null;
		try {
			byte[] cipherText=Hex.decode(hexValue);
			decryptedOutput=aead.decrypt(cipherText, passphrase.getBytes());
		}catch(Exception e) {
			System.out.println("Error in decryption... " + e.getMessage());
		}
		return new String(decryptedOutput);
	}

	public static void main(String args[]) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("inputString.txt")));
			String tmp;
			while((tmp=br.readLine())!=null) {
				TinkEncrypter tink = new TinkEncrypter("ghijk");
//				String encryptedOutput=tink.encryptText(tmp);
				String encryptedOutput=tmp;
				String decryptedOutput=tink.decryptText(encryptedOutput);
				
//				System.out.println(encryptedOutput + " , " + decryptedOutput);
				System.out.println(decryptedOutput);
			}
		br.close();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}