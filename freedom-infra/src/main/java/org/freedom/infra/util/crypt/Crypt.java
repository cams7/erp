package org.freedom.infra.util.crypt;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;


public class Crypt {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		String password = "xy11yx12";
		String passCrypt = crypt(password);
		String passDecrypt = decrypt(passCrypt);

		System.out.println("Senha a criptografar: " + password);
		System.out.println("Senha criptografada: "+ passCrypt);
		System.out.println("Senha decriptografada: "+passDecrypt);

	}

	public static String crypt(String password) {
		return crypt(password, null);
	}
	
	public static String decrypt(String password) {
		return decrypt(password, null);
	}
	
	public static String crypt(String password, char[] ponderosityPass) {
		return cryptDeCrypt(password, ponderosityPass, true);
	}
	
	public static String decrypt(String password, char[] ponderosityPass) {
	    return cryptDeCrypt(password, ponderosityPass, false);	
	}
	
	private static String cryptDeCrypt(String password, char[] ponderosityPass, boolean crypt) {
		
		Cipher cipher;
		SecretKeyFactory skf;
		PBEParameterSpec ps;
		String result = null;
		byte[] ponderosity = new byte[]{1,4,6,7,8,3,5,9};
		if (ponderosityPass==null) {
			ponderosityPass = "yX9oPkR878fx".toCharArray();
		}
		
		try {
			
			cipher = Cipher.getInstance("PBEWithMD5AndDES");
			
			ps = new PBEParameterSpec(ponderosity, 20);
			
			skf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			
			KeySpec ks = new PBEKeySpec(ponderosityPass);
			
			SecretKey sk = skf.generateSecret(ks);
			
			if (crypt) {
				cipher.init(Cipher.ENCRYPT_MODE, sk, ps);
			} else {
				cipher.init(Cipher.DECRYPT_MODE, sk, ps);
			}
			
			byte[] bIn = password.getBytes();
			
			byte[] bOut = cipher.doFinal(bIn);
			
			result = new String(bOut);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
		
	}
	
}
