package ControleDeCriptografia;

import java.security.PrivateKey;
import java.security.PublicKey;

import com.google.gson.Gson;

/**
 * @version 0.2
 * @since 0.1
 * @author Abraao, Aellison, Jose, Pedro
 *
 */
public class MensagemDTO {
	private String mensagem;
	private byte[] chavePublica;

	/**
	 * @version 0.2
	 * @since 0.1
	 * @param mensagem
	 *            Mensagem contendo dados a serem criptografados
	 * @param chavePublica
	 *            Chave publica para envio ao endpoint de comunicacao
	 */
	public MensagemDTO(String mensagem, PublicKey chavePublica) {
		this.mensagem = mensagem;
		this.chavePublica = chavePublica.getEncoded();
	}

	/**
	 * @version 0.2
	 * @since 0.1
	 * @return Retorna a mensagem do MensagemDTO.
	 */
	public String getMensagem() {
		return mensagem;
	}

	/**
	 * @version 0.2
	 * @since 0.1
	 * @param mensagem
	 *            Define a mensagem do MensagemDTO.
	 */
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	/**
	 * @version 0.2
	 * @since 0.1
	 * @return Retorna a chave publica do MensagemDTO em um array de bytes.
	 */
	public byte[] getChavePublica() {
		return chavePublica;
	}

	/**
	 * @version 0.2
	 * @since 0.1
	 * @param chavePublica
	 *            Define o valor da chave publica do MensagemDTO em um array de
	 *            bytes.
	 */
	public void setChavePublica(byte[] chavePublica) {
		this.chavePublica = chavePublica;
	}

	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
