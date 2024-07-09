package scrips.datamigration.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptAndDecrypt {
//	public static void main(String[] args) {
//		try {
//		// TODO Auto-generated method stub
//		//SecretKey key =generateKey(128);
//			String password = "testPassword";
//		    String salt = "12345678";
//		    SecretKey key =	getKeyFromPassword(password, salt);
//	    String algorithm = "AES/CBC/PKCS5Padding";
//	    IvParameterSpec ivParameterSpec = generateIv();
//	    //[B@467aecef
//	    //[B@7e2d773b
//	    System.out.println( ivParameterSpec.getIV());
//	    
//	   // File inputFile = resource.getFile();
////	    File inputFile = new File("D://abc//ACCOUNT_FILE_UPLOAD.txt");
////		File encryptFile = new File("D://abc//ACCOUNT_FILE_UPLOAD.encrypted");
////		File fileOutEncoded = new File("D://abc//ACCOUNT_FILE_UPLOAD.encoded");
////		File decryptFile = new File("D://abc//ACCOUNT_FILE_UPLOAD.decrypt");
////	
//	    
//	    File inputFile = new File("C://Users//user181//Desktop//RebexTinySftpServer-Binaries-Latest//data//ACCOUNT_FILE_UPLOAD.txt");
//	    File encryptFile = new File("C://Users//user181//Desktop//RebexTinySftpServer-Binaries-Latest//data//ACCOUNT_FILE_UPLOAD.encrypted");
//		File fileOutEncoded = new File("C://Users//user181//Desktop//RebexTinySftpServer-Binaries-Latest//data//ACCOUNT_FILE_UPLOAD.encoded");
//		File decryptFile = new File("C://Users//user181//Desktop//RebexTinySftpServer-Binaries-Latest//data//ACCOUNT_FILE_UPLOAD.decrypt");
////	    encryptOrDecryptFile(Cipher.ENCRYPT_MODE,algorithm, key, ivParameterSpec, inputFile, encryptFile);
//	  // System.out.println(key);
////	   encodeFileToBase64(encryptFile, fileOutEncoded);
//	   
//	 encryptOrDecryptFile(Cipher.DECRYPT_MODE,algorithm, key, ivParameterSpec, encryptFile, decryptFile);
//	   
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//	public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
//	    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//	    keyGenerator.init(n);
//	    SecretKey key = keyGenerator.generateKey();
//	    return key;
//	}
//	
//	
//	private static String initVector = "1234567812345678";
//	public static IvParameterSpec generateIv() throws UnsupportedEncodingException {
//		IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
//	  //  byte[] iv = new byte[16];
//	   // new SecureRandom().nextBytes(iv);
//	  // iv= new byte[] { 75, -12, -51, -74, -102, -74, -1, -115, 5, -8, -42, -53, 103, 119, -32, 28};
//	  // System.out.println( Arrays.toString(iv));
//	   // System.out.println(iv.toString());
//	    //iv="[B@467aecef5t5r4".getBytes();
//	    return iv;
//	}
//	
//	public static void encryptOrDecryptFile(int cipherMode, String algorithm, SecretKey key, IvParameterSpec iv,
//		    File inputFile, File outputFile) throws IOException, NoSuchPaddingException,
//		    NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
//		    BadPaddingException, IllegalBlockSizeException {
//		    
//		    Cipher cipher = Cipher.getInstance(algorithm);
//		    cipher.init(cipherMode, key,iv);
//		    FileInputStream inputStream = new FileInputStream(inputFile);
//		    FileOutputStream outputStream = new FileOutputStream(outputFile);
//		    //cipher.getIV();
//		  
//		    byte[] buffer = new byte[64];
//		    int bytesRead;
//		    while ((bytesRead = inputStream.read(buffer)) != -1) {
//		        byte[] output = cipher.update(buffer, 0, bytesRead);
//		        if (output != null) {
//		            outputStream.write(output);
//		        }
//		    }
//		    byte[] outputBytes = cipher.doFinal();
//		    if (outputBytes != null) {
//		        outputStream.write(outputBytes);
//		    }
//		    
//		    
//		    inputStream.close();
//		    outputStream.flush();
//		    outputStream.close();
//		}
//	
//	private static void encodeFileToBase64(File inputFile,File outputFile) {
//	    try {
//	        byte[] fileContent = Files.readAllBytes(inputFile.toPath());
//	        FileOutputStream outputStream = new FileOutputStream(outputFile);
//	        byte[] str= Base64.getEncoder().encodeToString(fileContent).getBytes(StandardCharsets.UTF_16);
//	        if (str != null) {
//	            outputStream.write(str);
//	        }
//	        outputStream.close();
//	    } catch (IOException e) {
//	        throw new IllegalStateException("could not read file " + inputFile, e);
//	    }
//	}
//	
//
//	public static SecretKey getKeyFromPassword(String password, String salt)
//		    throws NoSuchAlgorithmException, InvalidKeySpecException {
//		    
//		    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//		    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
//		    SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
//		        .getEncoded(), "AES");
//		    return secret;
//		}
}
