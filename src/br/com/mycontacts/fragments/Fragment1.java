package br.com.mycontacts.fragments;

import java.util.List;

import br.com.mycontacts.R;
import br.com.mycontacts.lista.dao.ContatoDAO;
import br.com.mycontacts.lista.dao.ligacaoDAO;
import br.com.mycontacts.lista.modelo.Contato;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Fragment1 extends Fragment {
	private ListView history;
	private ligacaoDAO dao = new ligacaoDAO(getActivity());
	private Contato contato;
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
    	carregaLista();
    }

	private void carregaLista() {
		ContatoDAO dao = new ContatoDAO(getActivity());
        List<Contato> contatos = dao.getLista();
        dao.close();

        //Aparencia
        int layout = android.R.layout.simple_list_item_1;

        //Transforma as string p/ uma View, quem faz isso é o Adapter
        ArrayAdapter<Contato> adapter = new ArrayAdapter<Contato>(getActivity(), layout, contatos);

        //Colocando as string dos nomes nas linhas da ListView
        history.setAdapter(adapter);
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.historico, menu); //Aqui deve ser R.menu.principal, mas nao consigo mecher
	    super.onCreateOptionsMenu(menu,inflater);
	    //fragment specific menu creation
	}
}
