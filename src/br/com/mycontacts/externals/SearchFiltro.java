package br.com.mycontacts.externals;

import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;

public class SearchFiltro implements OnQueryTextListener {

	@Override
	public boolean onQueryTextChange(String texto) {
		// Sempre que o texto é mudado
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String texto) {
		// Ao enviar
		Log.i("script", "Enviou:"+texto);
		return true;
	}

}
