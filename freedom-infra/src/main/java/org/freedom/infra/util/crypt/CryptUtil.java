package org.freedom.infra.util.crypt;

import java.io.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * Projeto: <a
 * href="http://sourceforge.net/projects/freedom-erp/">Freedom-infra</a> <br>
 * Este programa é licenciado de acordo com a LPG-PC <br>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada
 * pela Fundação do Software Livre (FSF); <BR>
 * <br>
 * 
 * <Descrição da classe>
 * 
 * @author Robson Sanchez
 * @version 0.0.1 – 25/06/2008
 * 
 * @since 25/06/2008
 */
public class CryptUtil {

	/**
	 * 
	 */
	public static final String DEFAULT_CHARSET = "UTF-16BE";

	/**
	 * 
	 * @return <Descrição do retorno>
	 * @since 25/06/2008
	 */
	public static byte[] generateKey() {
		return generateKey(null);
	}

	/**
	 * 
	 * @param key
	 *            <Descrição do parametro>
	 * @return <Descrição do retorno>
	 * @since 25/06/2008
	 */
	public static byte[] generateKey(String key) {

		byte[] encKey = new byte[] { 107, 101, 121, 64, 101, 110, 99, 114, 121, 112, 116, 105, 111, 110 };
		if (key != null) {
			encKey = key.getBytes();
		}
		return encKey;
	}

	/**
	 * 
	 * @param source
	 *            <Descrição do parametro>
	 * @param encKey
	 *            <Descrição do parametro>
	 * @return <Descrição do retorno>
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalStateException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @since 25/06/2008
	 */
	public static String encrypt(String source, byte[] encKey) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalStateException,
			IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec skeySpec = new SecretKeySpec(encKey, "Blowfish");
		Cipher cipher = Cipher.getInstance("Blowfish");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] sourceBytes = source.getBytes(DEFAULT_CHARSET);
		byte[] encryptedSource = cipher.doFinal(sourceBytes);

		return new String(encryptedSource, DEFAULT_CHARSET);
	}

	/**
	 * 
	 * @param encryptedSource
	 *            <Descrição do parametro>
	 * @param encKey
	 *            <Descrição do parametro>
	 * @return <Descrição do retorno>
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalStateException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @since 25/06/2008
	 */
	public static String decrypt(String encryptedSource, byte[] encKey) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalStateException, IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec skeySpec = new SecretKeySpec(encKey, "Blowfish");
		Cipher cipher = Cipher.getInstance("Blowfish");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] encryptedSourceBytes = encryptedSource.getBytes(DEFAULT_CHARSET);
		byte[] source = cipher.doFinal(encryptedSourceBytes);

		return new String(source, DEFAULT_CHARSET);
	}

	/**
	 * 
	 * @param source
	 *            <Descrição do parametro>
	 * @param destination
	 *            <Descrição do parametro>
	 * @param encKey
	 *            <Descrição do parametro>
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalStateException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws IOException
	 * @since 25/06/2008
	 */
	public static void encrypt(String source, String destination, byte[] encKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalStateException,
			IllegalBlockSizeException, BadPaddingException, IOException {
		SecretKeySpec skeySpec = new SecretKeySpec(encKey, "Blowfish");
		Cipher cipher = Cipher.getInstance("Blowfish");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		File inputFile = new File(source);
		File outputFile = new File(destination);
		if (inputFile.isFile()) {
			if (outputFile.exists()) {
				outputFile.delete();
			}
			FileInputStream in = new FileInputStream(inputFile);
			FileOutputStream out = new FileOutputStream(outputFile);
			CipherOutputStream cout = new CipherOutputStream(out, cipher);
			int length = 0;
			byte[] buffer = new byte[8];
			while (( length = in.read(buffer) ) != -1) {
				cout.write(buffer, 0, length);
			}
			cout.flush();
			cout.close();
			out.close();
			in.close();
		}
	}

	/**
	 * 
	 * @param source
	 *            <Descrição do parametro>
	 * @param destination
	 *            <Descrição do parametro>
	 * @param encKey
	 *            <Descrição do parametro>
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalStateException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws IOException
	 * @since 25/06/2008
	 */
	public static void decrypt(String source, String destination, byte[] encKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalStateException,
			IllegalBlockSizeException, BadPaddingException, IOException {
		SecretKeySpec skeySpec = new SecretKeySpec(encKey, "Blowfish");
		Cipher cipher = Cipher.getInstance("Blowfish");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		File inputFile = new File(source);
		File outputFile = new File(destination);
		if (inputFile.isFile()) {
			if (outputFile.exists()) {
				outputFile.delete();
			}
			FileInputStream in = new FileInputStream(inputFile);
			FileOutputStream out = new FileOutputStream(outputFile);
			CipherInputStream cin = new CipherInputStream(in, cipher);
			int length = 0;
			byte[] buffer = new byte[8];
			while (( length = cin.read(buffer) ) != -1) {
				out.write(buffer, 0, length);
			}
			out.flush();
			out.close();
			cin.close();
			in.close();
		}
	}
}