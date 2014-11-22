package br.com.mycontacts.lista.modelo;

import java.io.Serializable;

public class Ligacao implements Serializable{
	private Long id;
	private String nome;
	
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
}
