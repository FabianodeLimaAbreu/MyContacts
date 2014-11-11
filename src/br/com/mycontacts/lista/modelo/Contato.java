package br.com.mycontacts.lista.modelo;

import java.io.Serializable;

public class Contato implements Serializable{	
	private Long id;
	private String nome, telefone, email, endereco, foto, horaLigacao;
	int operadora;
	private Double favorito;
	
	@Override
	public String toString() {
		return nome;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	public Double getFavorito() {
		return favorito;
	}
	public void setFavorito(Double favorito) {
		this.favorito = favorito;
	}

	public String getHoraLigacao() {
		return horaLigacao;
	}

	public void setHoraLigacao(String horaLigacao) {
		this.horaLigacao = horaLigacao;
	}

	public int getOperadora() {
		return operadora;
	}

	public void setOperadora(int operadora) {
		this.operadora = operadora;
	}
}
