package de.hypercdn.commons.imp.crypt;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.generators.PKCS12ParametersGenerator;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.ParametersWithIV;

import java.security.SecureRandom;

/**
 * Used to en/decrypt data using a password
 **/
public class PasswordCrypt{

	private PasswordCrypt(){}

	/**
	 * Used to encrypt the input with a given password.
	 * <p>
	 * This calls {@link PasswordCrypt#encrypt(byte[], String, byte[])} with {@link PasswordCrypt#nullSalt()} as salt </br>
	 *
	 * @param bytes    input bytes
	 * @param password password
	 *
	 * @return encrypted bytes
	 *
	 * @throws InvalidCipherTextException on exception
	 */
	public static byte[] encrypt(byte[] bytes, String password) throws InvalidCipherTextException{
		return encrypt(bytes, password, nullSalt());
	}

	/**
	 * Used to encrypt the input with a given password and salt
	 *
	 * @param bytes    input bytes
	 * @param password password
	 * @param salt     salt
	 *
	 * @return encrypted bytes
	 *
	 * @throws InvalidCipherTextException on exception
	 */
	public static byte[] encrypt(byte[] bytes, String password, byte[] salt) throws InvalidCipherTextException{
		return crypt(bytes, password, salt, true);
	}

	/**
	 * Used to decrypt the input with a given password.
	 * <p>
	 * This calls {@link PasswordCrypt#decrypt(byte[], String, byte[])} with {@link PasswordCrypt#nullSalt()} as salt </br>
	 *
	 * @param bytes    input bytes
	 * @param password password
	 *
	 * @return encrypted bytes
	 *
	 * @throws InvalidCipherTextException on exception
	 */
	public static byte[] decrypt(byte[] bytes, String password) throws InvalidCipherTextException{
		return decrypt(bytes, password, nullSalt());
	}

	/**
	 * Used to decrypt the input with a given password.
	 *
	 * @param bytes    input bytes
	 * @param password password
	 * @param salt     salt
	 *
	 * @return encrypted bytes
	 *
	 * @throws InvalidCipherTextException on exception
	 */
	public static byte[] decrypt(byte[] bytes, String password, byte[] salt) throws InvalidCipherTextException{
		return crypt(bytes, password, salt, false);
	}

	/**
	 * This does the actual en- and decryption
	 *
	 * @param bytes    input bytes
	 * @param password for en-/decryption
	 * @param salt     from the password
	 * @param mode     boolean for encryption
	 *
	 * @return byte[] en-/decrypted data
	 *
	 * @throws InvalidCipherTextException on exception
	 */
	private static byte[] crypt(byte[] bytes, String password, byte[] salt, boolean mode) throws InvalidCipherTextException{
		ParametersWithIV key = (ParametersWithIV) getAESPassKey(password.toCharArray(), salt);
		BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()));
		cipher.init(mode, key);
		byte[] result = new byte[cipher.getOutputSize(bytes.length)];

		int outputLength = 0;
		int bytesProcessed;

		bytesProcessed = cipher.processBytes(bytes, 0, bytes.length, result, 0);
		outputLength += bytesProcessed;
		bytesProcessed = cipher.doFinal(result, bytesProcessed);
		outputLength += bytesProcessed;
		if(outputLength == result.length){
			return result;
		}
		else{
			// remove null padding
			byte[] truncatedOutput = new byte[outputLength];
			System.arraycopy(
				result, 0,
				truncatedOutput, 0,
				outputLength
			);
			return truncatedOutput;
		}
	}

	/**
	 * Returns CipherParameters for the given input
	 *
	 * @param passwd password (char array)
	 * @param salt   salt (byte array)
	 *
	 * @return CipherParameters
	 */
	private static CipherParameters getAESPassKey(char[] passwd, byte[] salt){
		PBEParametersGenerator generator = new PKCS12ParametersGenerator(new SHA512Digest());
		generator.init(
			PBEParametersGenerator.PKCS12PasswordToBytes(passwd), salt, 1024
		);
		return generator.generateDerivedParameters(128, 128);
	}

	/**
	 * Provides default/empty salt
	 *
	 * @return salt
	 */
	private static byte[] nullSalt(){
		return new byte[16];
	}

	/**
	 * Provides a random 16 byte salt
	 *
	 * @return salt
	 */
	public static byte[] genSalt(){
		byte[] bytes = new byte[16];
		new SecureRandom().nextBytes(bytes);
		return bytes;
	}

}
