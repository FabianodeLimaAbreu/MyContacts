package br.com.mycontacts.fragments;

import java.util.List;

import br.com.mycontacts.R;
import br.com.mycontacts.lista.dao.ContatoDAO;
import br.com.mycontacts.lista.dao.ligacaoDAO;
import br.com.mycontacts.lista.modelo.Contato;
import br.com.mycontacts.lista.modelo.Ligacao;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    	carregaListaLigacoes();
    }
	
	private void carregaListaLigacoes() {
		ligacaoDAO daoLig = new ligacaoDAO(getActivity());
        List<Ligacao> ligacoes = daoLig.getListaLigacao();
        daoLig.close();
        
        //Aparencia
        int layout = android.R.layout.simple_list_item_1;

        //Transforma as string p/ uma View, quem faz isso é o Adapter
        //Log.i("ADAPTER", ""+ligacoes.get(0));
        ArrayAdapter<Ligacao> adapterligacoes = new ArrayAdapter<Ligacao>(getActivity(), layout, ligacoes);

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
