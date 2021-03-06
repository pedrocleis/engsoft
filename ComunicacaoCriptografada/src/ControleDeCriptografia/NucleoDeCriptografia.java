package ControleDeCriptografia;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;

/**
 * @author Abraao, Aellison, Jose, Pedro
 *
 *         Objeto de manipulacaoo de mensageria e criptografia.
 *
 *
 *         *As chaves geradas e utilizadas nessa implementacao sao geradas com o
 *         algorimo RSA e possuem 1024 bits de comprimento.
 */
public class NucleoDeCriptografia {
	private static NucleoDeCriptografia instancia;
	private Cipher cipher;

	private NucleoDeCriptografia() {
		try {
			cipher = Cipher.getInstance("RSA");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @version 0.2
	 * @since 0.1
	 * @return Singleton do Nucleo de Criptografia
	 */
	public static NucleoDeCriptografia getInstancia() {

		if (instancia == null)
			instancia = new NucleoDeCriptografia();

		return instancia;

	}

	/**
	 * @version 0.2
	 * @since 0.1
	 * @param mensagem
	 *            Mensagem a ser criptografada
	 * @param criptografavel
	 *            Objeto criptografavel contendo a chave de criptografia que
	 *            sera utilizada para criptografar a mensagem no campo
	 *            ChavePublicaExterna.
	 * @return Retorna o pacote de criptografado completo em um array de bytes.
	 *
	 *         *As chaves geradas e utilizadas nessa implementacao sao geradas
	 *         com o algorimo RSA e possuem 1024 bits de comprimento.
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public byte[] encriptarMensagem(String mensagem, Criptografavel criptografavel)
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		Gson gson = new Gson();
		MensagemDTO msgDTO = new MensagemDTO(mensagem, criptografavel.getChavePublica());
		mensagem = msgDTO.toString();

		ArrayList<String> retorno = new ArrayList<String>();
		String parcial;
		for (int i = 0; i < mensagem.length() - 1; i += 100) {
			parcial = mensagem.length() >= i + 100 ? mensagem.substring(i, i + 100) : mensagem.substring(i);

			this.cipher.init(Cipher.ENCRYPT_MODE, criptografavel.getChavePublicaExterna());

			retorno.add(Base64.encodeBase64String(cipher.doFinal(parcial.getBytes("UTF-8"))));

		}

		return gson.toJson(retorno).getBytes();

	}

	/**
	 * @version 0.2
	 * @since 0.1
	 * @param mensagem
	 *            Array de bytes a ser decriptografado.
	 * @param criptografavel
	 *            Objeto criptografavel contendo a chave de criptografia que
	 *            sera utilizada para decriptografar a mensagem no campo
	 *            ChavePrivada.
	 * @return Retorna a mensagem decriptografada e insere a chavePublica que
	 *         veio na mensagem no campo ChavePublicaExterna do objeto
	 *         criptografavel.
	 *
	 *         *As chaves geradas e utilizadas nessa implementacao sao geradas
	 *         com o algorimo RSA e possuem 1024 bits de comprimento.
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	public String decriptarMensagem(byte[] mensagem, Criptografavel criptografavel)
			throws InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException,
			NoSuchAlgorithmException, InvalidKeySpecException {
		MensagemDTO msgDTO = null;
		String retorno = "";
		Gson gson = new Gson();
		ArrayList<String> dados = gson.fromJson(new String(mensagem), ArrayList.class);
		// try{

		for (String parcial : dados) {

			this.cipher.init(Cipher.DECRYPT_MODE, criptografavel.getChavePrivada());

			retorno += new String(cipher.doFinal(Base64.decodeBase64(parcial)), "UTF-8");

		}

		msgDTO = gson.fromJson(retorno, MensagemDTO.class);

		criptografavel.setChavePublicaExterna(msgDTO.getChavePublica());

		return msgDTO.getMensagem();
	}
}
