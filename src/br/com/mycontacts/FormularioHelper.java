package br.com.mycontacts;

import java.beans.IndexedPropertyChangeEvent;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;
import br.com.mycontacts.lista.modelo.Contato;

public class FormularioHelper {

	private static final Context Formulario = null;
	private EditText editNome, editTelefone, editEmail, editEndereco;
	private RatingBar ratingFavorito;
	private ImageView foto;
	private Spinner editOperadora;
	private Contato contato;
	ArrayAdapter<String> adapterSpinner;
	ArrayList<String> nomesOperadoras;
	
	public FormularioHelper(Formulario formulario) {
		editNome = (EditText) formulario.findViewById(R.id.nome);
		editTelefone = (EditText) formulario.findViewById(R.id.telefone);
		editEmail = (EditText) formulario.findViewById(R.id.email);
		editEndereco = (EditText) formulario.findViewById(R.id.endereco);
		ratingFavorito = (RatingBar) formulario.findViewById(R.id.favorito);
		foto = (ImageView) formulario.findViewById(R.id.foto);
		editOperadora = (Spinner) formulario.findViewById(R.id.operadora);
     
		String[] nomesOperadoras ={"Claro", "Nextel", "Oi", "Vivo", "Tim"};
			
	    int layoutSpinner = android.R.layout.simple_spinner_item;
	        
	    adapterSpinner = new ArrayAdapter<String>(formulario, layoutSpinner, nomesOperadoras);
	        
	    editOperadora.setAdapter(adapterSpinner);
		
		contato = new Contato();
	}

	public Contato pegaContatoDoFormulario() { 				
		contato.setNome(editNome.getText().toString());
		contato.setTelefone(editTelefone.getText().toString());
		contato.setEmail(editEmail.getText().toString());
		contato.setEndereco(editEndereco.getText().toString());
		contato.setFavorito(Double.valueOf(ratingFavorito.getRating()));
		contato.setOperadora((int) editOperadora.getSelectedItemId());
		
		return contato;
	}

	public void colocaContatoNoFormulario(Contato contatoMostrar, String mostrarOuAlterar) {
		contato = contatoMostrar;
		editNome.setText(contatoMostrar.getNome());
		editTelefone.setText(contatoMostrar.getTelefone());
		editEmail.setText(contatoMostrar.getEmail());
		editEndereco.setText(contatoMostrar.getEndereco());
		ratingFavorito.setRating(contatoMostrar.getFavorito().floatValue());
		editOperadora.setSelection(contatoMostrar.getOperadora());
		
		if (contato.getFoto() != null) {
			carregaImagem(contatoMostrar.getFoto());
		}
		
		if (mostrarOuAlterar == "contatoMostrar") {
			editNome.setEnabled(false);
			editTelefone.setEnabled(false);
			editEmail.setEnabled(false);
			editEndereco.setEnabled(false);	
			ratingFavorito.setEnabled(false);
			foto.setEnabled(false);	
			editOperadora.setEnabled(false);
		}
	}
	
	public ImageView getFoto() {
		return foto;
	}

	public void carregaImagem(String caminhoArquivo) {
		contato.setFoto(caminhoArquivo);
		//Fazer imagem que foi tirada
		Bitmap imagem = BitmapFactory.decodeFile(caminhoArquivo);
		/*Reduzir imagem*/
		Bitmap imagemReduzida = Bitmap.createScaledBitmap(imagem, 100, 100, true);
		//Carregar imagem que foi tirada
		foto.setImageBitmap(imagemReduzida);
	}
}
