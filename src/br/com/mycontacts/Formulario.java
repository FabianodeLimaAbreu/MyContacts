package br.com.mycontacts;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import br.com.mycontacts.lista.dao.ContatoDAO;
import br.com.mycontacts.lista.modelo.Contato;

@SuppressLint("NewApi") public class Formulario extends Activity{
	
	private FormularioHelper helper;
	private String caminhoArquivo;
	
	private Contato contatoMostrar;
	private Contato contatoAlterar;
	
	private Contato contato;
	private ContatoDAO dao; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView é o método mais importante da activity.
		//Ele vincula a Activity Formulario com o layout formulario.xml
		setContentView(R.layout.formulario);
		
		//Para botão de voltar no logo
		android.app.ActionBar ab=getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
        
		Intent intent = getIntent();
		
		contatoMostrar = (Contato) intent.getSerializableExtra("contatoMostrar");
		contatoAlterar = (Contato) intent.getSerializableExtra("contatoAlterar");
		
		//Extrair o codigo em uma classe separada, para evitar que a Activity cresça demais.
		helper = new FormularioHelper(this);
		
		//Agora que todas as Views estão buscadas,devemos colocar dentro de um contato
		ImageButton botao = (ImageButton) findViewById(R.id.botao);

		if(contatoMostrar != null){
			botao.setVisibility(View.INVISIBLE);				
			helper.colocaContatoNoFormulario(contatoMostrar, "contatoMostrar");
		} else if (contatoAlterar != null) {
			helper.colocaContatoNoFormulario(contatoAlterar, "contatoAlterar");
		}
		botao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Contato contato = helper.pegaContatoDoFormulario();
				//especialista para salvar no Sqlite
				ContatoDAO dao = new ContatoDAO(Formulario.this);
				
				if (contatoAlterar == null && contatoMostrar == null){
					dao.salva(contato);
				} else if (contatoAlterar != null) {
					//contato.setId(contato.getId());
					dao.alterar(contato);
				}
				dao.close();			
				
				finish(); //Botão voltar automatico
			}
		});		
		
		ImageView foto = helper.getFoto();
		foto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent irParaCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				
				caminhoArquivo = Environment.getExternalStorageDirectory().toString()
						+"/"+System.currentTimeMillis()+".png";
				
				/*Criando arquivo, deve ser um FILE*/
				File arquivo = new File(caminhoArquivo);
				
				/*Só que no Android a identificação de caminho tem que ser especificado em uma URI*/
				Uri localImagem = Uri.fromFile(arquivo);
				
				/*MediaStore.EXTRA_OUTPUT apelido conhecido pelo android para a Intent de camera saber o local da imagem*/
				irParaCamera.putExtra(MediaStore.EXTRA_OUTPUT, localImagem);
				
				/*O codigo do resultado para tentativa de tirar foto é 123*/
				startActivityForResult(irParaCamera, 123);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*Para saber se estamos voltando da chamada da camera comparamos o requestCode == 123*/
		if (requestCode == 123) {
			if (resultCode == Activity.RESULT_OK) {

				//ok para foto
				helper.carregaImagem(caminhoArquivo);
			} else {
				//cancelou foto
				caminhoArquivo = null;
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater(); //getMenuInflater retorna o menu inflate
		if(contatoMostrar != null){
			//Visualizar
			inflater.inflate(R.menu.viewer_contato, menu); //metodo inflate, devemos passar o xml que queremos inflar
		}
		else{
			inflater.inflate(R.menu.formularios, menu); //Editar formulario
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem itemSelect) {
		int itemClicado = itemSelect.getItemId();//descobrir qual o item clicado
		
		switch (itemClicado) {
			case android.R.id.home:
				Intent irHome=new Intent(this,MainActivity.class); 
				startActivity(irHome);
				break;
			case R.id.add_campo:
				Toast.makeText(Formulario.this, "Adicionar campo", Toast.LENGTH_SHORT).show();
				break;
			case R.id.deletar:
				AlertDialog ad = new AlertDialog.Builder(this)
	            .create();
			    ad.setTitle("Realmente excluir?");
			    ad.setMessage("Deseja realmente excluir o contato selecionado?");
			    ad.setButton("Sim", new DialogInterface.OnClickListener() { 
			    	public void onClick(DialogInterface arg0, int arg1) { 
			    		ContatoDAO dao = new ContatoDAO(Formulario.this); //Chamada o SQLITE
			        	dao.deletar(contatoMostrar);
						dao.close();
						Intent irLista = new Intent(Formulario.this, MainActivity.class);
			            Formulario.this.startActivity(irLista); 
			    	} 
			    }); 
			    ad.setButton2("Não", new DialogInterface.OnClickListener() { 
			    	public void onClick(DialogInterface arg0, int arg1) { 
			    	} 
			    });
			    ad.show();
				break;
			case R.id.editar:
				Intent irParaFormulario = new Intent(this, Formulario.class);
				contato = helper.pegaContatoDoFormulario();
				
				irParaFormulario.putExtra("contatoAlterar", contato);
				startActivity(irParaFormulario);				
				
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(itemSelect);
	}
}
