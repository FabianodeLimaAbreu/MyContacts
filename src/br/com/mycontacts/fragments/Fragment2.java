package br.com.mycontacts.fragments;

import java.util.List;

import br.com.mycontacts.R;
import br.com.mycontacts.externals.ListaContatosAdapter;
import br.com.mycontacts.externals.ListaLigacoesAdapter;
import br.com.mycontacts.lista.dao.ContatoDAO;
import br.com.mycontacts.lista.dao.ligacaoDAO;
import br.com.mycontacts.lista.modelo.Contato;
import br.com.mycontacts.lista.modelo.Ligacao;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Fragment2 extends Fragment {
	private ListView history;
	private ligacaoDAO daoLig;
	private Ligacao contato;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view=inflater.inflate(R.layout.historico, null);
		setHasOptionsMenu(true); //Para habilitar ao fragment que crie um novo menu para a tela
		return(view);
	}
	@Override
    public void onResume() {
    	super.onResume();
    	history = (ListView) getView().findViewById(R.id.historico);
    	history.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,int posicao, long id) {
				// TODO Auto-generated method stub
				/*Ligacao contatoLigar = (Ligacao) adapter.getItemAtPosition(posicao);
				Contato registroChamada = (Contato) adapter.getItemAtPosition(posicao);
				
				Intent irParaTelaDeDiscagem = new Intent(Intent.ACTION_CALL);
				Uri discarPara = Uri.parse("tel: " + contatoLigar.getTelefone());
				
				ligacaoDAO daoLigacao = new ligacaoDAO(getActivity()); 
				daoLigacao.salva(registroChamada); //Salvando conteúdo
				
				irParaTelaDeDiscagem.setData(discarPara);
				startActivity(irParaTelaDeDiscagem);*/
			}
		});
    	carregaListaLigacoes();
    }
	
	private void carregaListaLigacoes() {
		ligacaoDAO daoLig = new ligacaoDAO(getActivity());
        List<Ligacao> ligacoes = daoLig.getListaLigacao();
        daoLig.close();
        
        //Aparencia
        //int layout = android.R.layout.simple_list_item_1;

        //Transforma as string p/ uma View, quem faz isso é o Adapter
        //Log.i("ADAPTER", ""+ligacoes.get(0));
        ListaLigacoesAdapter adapterligacoes = new ListaLigacoesAdapter(ligacoes,this.getActivity());

        //Colocando as string dos nomes nas linhas da ListView
        history.setAdapter(adapterligacoes);
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.historico, menu); //Aqui deve ser R.menu.principal, mas nao consigo mecher
	    super.onCreateOptionsMenu(menu,inflater);
	    //fragment specific menu creation
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.delregistro) {
			ligacaoDAO daoLig = new ligacaoDAO(getActivity());
			daoLig.removeAll();
			daoLig.close();
			carregaListaLigacoes();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
