package br.com.mycontacts;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import br.com.mycontacts.externals.SearchFiltro;
import br.com.mycontacts.lista.dao.ContatoDAO;
import br.com.mycontacts.lista.modelo.Contato;

@SuppressLint("NewApi") public class ListaContatos extends Activity {

    private ListView lista;
	private Contato contato;
	private ContatoDAO dao = new ContatoDAO(ListaContatos.this);
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listagem_agenda);
        
        lista = (ListView) findViewById(R.id.lista);
        registerForContextMenu(lista);
        
        //Esperando o click em um item da lista
        lista.setOnItemClickListener(new OnItemClickListener() {
			//parametros: Adapter da view. View - Cada item da tela. Posicao - posicao da listview. Id - Toda view tem um ID 
        	@Override
			public void onItemClick(AdapterView<?> adapter, View view, int posicao, long id) {
				/*Toast.makeText(ListaContatos.this, "Click na posicao "+posicao, Toast.LENGTH_SHORT).show();
																			//Toast.length_short é a duração curta ou longa*/
				Contato contatoLigar = (Contato) adapter.getItemAtPosition(posicao);

				/*Intent Implícita - Chamar todas as activity do aparelho que sabem fazer discagem
					Para fazer isso precisamos colocar um apelido, no android temos alguns apelidos
					na classe Intent usando o ACTION_CALL
				*/
				Intent irParaTelaDeDiscagem = new Intent(Intent.ACTION_CALL);
					//Precisamos informar p/ que num queremos ligar.
					//Precimos colocar a permissão no Manifest para fazer uma ligação
				Uri discarPara = Uri.parse("tel: " + contatoLigar.getTelefone());
				irParaTelaDeDiscagem.setData(discarPara);
						
				startActivity(irParaTelaDeDiscagem);
			}
		});
        //click longo
        lista.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int posicao, long id) {
				
				contato = (Contato) adapter.getItemAtPosition(posicao);
				//Toast.makeText(ListaAgenda.this, "Click id  " +id, Toast.LENGTH_LONG).show();
				
				return false; //Colocamos true para consumir o evento, ou seja, não executar o proximo evento que seria o click curto.
			}
        });
    }

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {		
		
		MenuItem contatoSerVisto = menu.add("Ver contato");
		contatoSerVisto.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				indoParaFormulario("contatoMostrar");
				return false;
			}
		});
		
		menu.add("Enviar SMS");
		MenuItem deletar = menu.add("Deletar");
		deletar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				dao.deletar(contato);
				dao.close();
				
				carregaLista();
				return false;
			}
		});
		MenuItem alterar = menu.add("Alterar");
		alterar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {				
				indoParaFormulario("contatoAlterar");
				return false;
			}
		});
		
		menu.add("Enviar e-mail");
		menu.add("Ver no mapa");
		
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	public void indoParaFormulario(String mostrarOuAlterar){
		Intent irParaFormulario = new Intent(ListaContatos.this, Formulario.class);
		
		//contatoSelecionado é um apelido que sera usado para saber quem é o contato na próxima pagina, qndo usarmos o intent.getSerializableExtra
		irParaFormulario.putExtra(mostrarOuAlterar, contato);
		startActivity(irParaFormulario);			
	}
	
    @Override
    protected void onResume() {
    	super.onResume();
    	carregaLista();
    }

	private void carregaLista() {
		ContatoDAO dao = new ContatoDAO(this);
        List<Contato> contatos = dao.getLista();
        dao.close();
        
        //Aparencia
        int layout = android.R.layout.simple_list_item_1;

        //Transforma as string p/ uma View, quem faz isso é o Adapter
        ArrayAdapter<Contato> adapter = new ArrayAdapter<Contato>(this, layout, contatos);

        //Colocando as string dos nomes nas linhas da ListView
        lista.setAdapter(adapter);        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		//Chamada do searchView para trabalhar com campo de busca
		SearchView sv = new SearchView(this);
		sv.setOnQueryTextListener(new SearchFiltro());
		
		
		//Cria item de menu para buscas
		MenuItem m1 = menu.add(0, 0, 0, "search");
		//Os Lints das duas linha abaixo podem ser limpados antes de compilar prescionando Ctrl + 1 e clicar: Clear all link Markers
		m1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		m1.setActionView(sv);
		//Os itens do menu estão descritos em um XML na pasta menu
		//INFLATER: especialista em ler o XML e criar itens de menu
		
		MenuInflater inflater = getMenuInflater(); //getMenuInflater retorna o menu inflate
		inflater.inflate(R.menu.lista_agenda, menu); //metodo inflate, devemos passar o xml que queremos inflar
		
		return (true);	
	}    
	
	@Override
	public boolean onOptionsItemSelected(MenuItem itemSelect) {
		int itemClicado = itemSelect.getItemId();//descobrir qual o item clicado
		
		switch (itemClicado) {
		case R.id.novo:
			//Responsavel por ir "daqui" até "formulario.class"
			Intent irParaFormulario = new Intent(this, Formulario.class);

			//Uma vez a Intent preparada, basta que a Activity ListaAgenda execute-a. Para isso usamos o startActivity
			startActivity(irParaFormulario);
			break;
		case R.id.caller:			
			Intent abrirTecladoLigar = new Intent(Intent.ACTION_DIAL);
			startActivity(abrirTecladoLigar);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(itemSelect);
	}
}