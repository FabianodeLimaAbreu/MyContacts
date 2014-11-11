package br.com.mycontacts;

import java.util.List;

import br.com.mycontacts.lista.dao.ContatoDAO;
import br.com.mycontacts.lista.dao.ligacaoDAO;
import br.com.mycontacts.lista.modelo.Contato;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Historico extends ActionBarActivity {
	private ListView history;
	private ligacaoDAO dao = new ligacaoDAO(Historico.this);
	private Contato contato;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.historico);
		history = (ListView) findViewById(R.id.historico);
		registerForContextMenu(history);
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
        history.setAdapter(adapter);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.historico, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.delregistro) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
