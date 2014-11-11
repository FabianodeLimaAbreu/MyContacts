package br.com.mycontacts.fragments;


import java.util.List;

import br.com.mycontacts.Formulario;
import br.com.mycontacts.R;
import br.com.mycontacts.externals.SearchFiltro;
import br.com.mycontacts.lista.dao.ContatoDAO;
import br.com.mycontacts.lista.modelo.Contato;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class Fragment2 extends Fragment {
	private ListView lista;
	private Contato contato;
	private ContatoDAO dao; 
	//Chamada do ContatoDAO � realizada no momento do uso, para conseguir pegar o contexto da classe MainActivity por estarmos em um fragment
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view=inflater.inflate(R.layout.listagem_agenda, null);
		setHasOptionsMenu(true); //Para habilitar ao fragment que crie um novo menu para a tela
		return (view);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
	    //Chamada do searchView para trabalhar com campo de busca
	    SearchView sv = new SearchView(getActivity());
	    sv.setOnQueryTextListener(new SearchFiltro());
	    
	    //Cria item de menu para buscas
	    MenuItem m1 = menu.add(0, 0, 0, "Item 1");
	    //Os Lints das duas linha abaixo podem ser limpados antes de compilar prescionando Ctrl + 1 e clicar: Clear all link Markers
	    m1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	    m1.setActionView(sv);
	    
	    //Os itens do menu estao descritos em um XML na pasta menu
 		//INFLATER: especialista em ler o XML e criar itens de menu
 		
 		inflater.inflate(R.menu.lista_agenda, menu); //metodo inflate, devemos passar o xml que queremos inflar
 		super.onCreateOptionsMenu(menu, inflater);
 		 //fragment specific menu creation
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem itemSelect) {
		int itemClicado = itemSelect.getItemId();//descobrir qual o item clicado
		
		switch (itemClicado) {
		case R.id.novo:
			//Responsavel por ir "daqui" at� "formulario.class"
			//Como estamos em um fragment. Acessamos o contexto da classe (this), utilizamos getActivity() 
			Intent irParaFormulario = new Intent(getActivity(), Formulario.class);
            getActivity().startActivity(irParaFormulario); 
			//Uma vez a Intent preparada, basta que a Activity ListaAgenda execute-a. Para isso usamos o startActivity
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
	
	@Override
	public void onResume() {
    	super.onResume();
    	lista=(ListView) getView().findViewById(R.id.lista);
    	registerForContextMenu(lista);
    	//Esperando o click em um item da lista
        lista.setOnItemClickListener(new OnItemClickListener() {
			//parametros: Adapter da view. View - Cada item da tela. Posicao - posicao da listview. Id - Toda view tem um ID 
        	@Override
			public void onItemClick(AdapterView<?> adapter, View view, int posicao, long id) {
				Contato contatoLigar = (Contato) adapter.getItemAtPosition(posicao);

				/*Intent Impl�cita - Chamar todas as activity do aparelho que sabem fazer discagem
					Para fazer isso precisamos colocar um apelido, no android temos alguns apelidos
					na classe Intent usando o ACTION_CALL
				*/
				Intent irParaTelaDeDiscagem = new Intent(Intent.ACTION_CALL);
					//Precisamos informar p/ que num queremos ligar.
					//Precimos colocar a permiss�o no Manifest para fazer uma liga��o
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
				
				return false; //Colocamos true para consumir o evento, ou seja, n�o executar o proximo evento que seria o click curto.
			}
        });
    	carregaLista();
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
			
			@SuppressWarnings("deprecation")
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				
				AlertDialog ad = new AlertDialog.Builder(getActivity())
	            .create();
			    ad.setTitle("Realmente excluir?");
			    ad.setMessage("Deseja realmente excluir o contato selecionado?");
			    /*ad.setButton(getActivity().getString(R.string.hello_world), new DialogInterface.OnClickListener() {
		
			        public void onClick(DialogInterface dialog, int which) {
			        	ContatoDAO dao = new ContatoDAO(getActivity()); //Chamada o SQLITE
			        	dao.deletar(contato);
						dao.close();
			            dialog.dismiss();
			        }
			    });*/
			    ad.setButton("Sim", new DialogInterface.OnClickListener() { 
			    	public void onClick(DialogInterface arg0, int arg1) { 
			    		ContatoDAO dao = new ContatoDAO(getActivity()); //Chamada o SQLITE
			        	dao.deletar(contato);
						dao.close();
						carregaLista();
			    	} 
			    }); 
			    ad.setButton2("N�o", new DialogInterface.OnClickListener() { 
			    	public void onClick(DialogInterface arg0, int arg1) { 
			    	} 
			    });
			    ad.show();
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
		Intent irParaFormulario = new Intent(getActivity(), Formulario.class);
		
		//contatoSelecionado � um apelido que sera usado para saber quem � o contato na pr�xima pagina, qndo usarmos o intent.getSerializableExtra
		irParaFormulario.putExtra(mostrarOuAlterar, contato);
		startActivity(irParaFormulario);			
	}
	
	private void carregaLista() {
		ContatoDAO dao = new ContatoDAO(getActivity());
        List<Contato> contatos = dao.getLista();
        dao.close();

        //Aparencia
        int layout = android.R.layout.simple_list_item_1;

        //Transforma as string p/ uma View, quem faz isso � o Adapter
        ArrayAdapter<Contato> adapter = new ArrayAdapter<Contato>(getActivity(), layout, contatos);

        //Colocando as string dos nomes nas linhas da ListView
        lista.setAdapter(adapter);
	}
}