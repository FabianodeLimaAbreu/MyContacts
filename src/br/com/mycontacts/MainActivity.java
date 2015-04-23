package br.com.mycontacts;

import br.com.mycontacts.fragments.Fragment2;
import br.com.mycontacts.fragments.Fragment1;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity {
	private android.app.ActionBar ab;
	
	public String operator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		DescobriOperadora();
	
		ab=getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		//Icone da home (logo) vira botao
		
		//TABS
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//TAB1
		android.app.ActionBar.Tab tab1=ab.newTab();
		tab1.setIcon(R.drawable.ic_action_group);
		tab1.setTag("Contatos");
		//Uma instancia de uma classe que implementa actionbar tablistener
		//Como parametro de entrada um construtor: UsM fragment vai ser vinculado a esta Tab
		tab1.setTabListener((TabListener) new NavegacaoTabs(new Fragment1()));
		//Implementa a tab na actionBar com parametro false para nenhuma ficar selecionada
		ab.addTab(tab1,false);
		
		//TAB2
		android.app.ActionBar.Tab tab2=ab.newTab();
		tab2.setIcon(R.drawable.ic_action_time);
		tab2.setTag("Historico de Ligacoes");
		//Uma instancia de uma classe que implementa actionbar tablistener
		//Como parametro de entrada um construtor: UM fragment vai ser vinculado a esta Tab
		tab2.setTabListener((TabListener) new NavegacaoTabs(new Fragment2()));
		//Implementa a tab na actionBar
		ab.addTab(tab2,false);
		
		//Recuperando o valor do onsaveInstanceState (Final do codigo)
		if(savedInstanceState != null){
			//se nao for nulo recupera
			int indiceTab=savedInstanceState.getInt("indiceTab");
			getActionBar().setSelectedNavigationItem(indiceTab);
		}
		else{
			//Se for nulo, marca a primeira
			getActionBar().setSelectedNavigationItem(0);
		}
	}

	public void DescobriOperadora() {
		//TESTANDO BUSCAR OPERADORA
		//LIGAÇÃO EFETUADA NO FRAGMENT1
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        operator = tm.getSimOperatorName();
		Toast.makeText(this, "OPERADORA: "+ operator, Toast.LENGTH_LONG).show();
	}
	
	private class NavegacaoTabs implements ActionBar.TabListener{
		
		private Fragment frag;
		//Abaixo para trabalhar com FragmentTransation de support
		public NavegacaoTabs(Fragment frag){
			this.frag=frag;
		}
	
		@Override
		public void onTabReselected(android.app.ActionBar.Tab tab,
				android.app.FragmentTransaction ft) {
			//Ao clicar em uma tab que ja esta selecionada		
		}
	
		@Override
		public void onTabSelected(android.app.ActionBar.Tab tab,
				android.app.FragmentTransaction ft) {
			getActionBar().setTitle((CharSequence) tab.getTag());
			// Ao sair de uma TAB e clicar na outra, a "outra tab" é chamada (Inserimos o novo conteudo)
			//fts foi o nome dado pois não vamos usar o Transation da lib nova, e sim da antiga
			FragmentTransaction fts= getSupportFragmentManager().beginTransaction();
			
			if(tab.getPosition() == 0){
				//Caso a tab selecionada seja a inicial (Historico), desabilita o botão voltar via logo
				ab.setDisplayHomeAsUpEnabled(false);
			}
			else{
				ab.setDisplayHomeAsUpEnabled(true);
			}
			fts.replace(R.id.fragmentContainer, frag);
			fts.commit();
		}
	
		@Override
		public void onTabUnselected(android.app.ActionBar.Tab tab,
				android.app.FragmentTransaction ft) {
			// Ao sair de uma TAB e clicar na outra, a "tab que saimos" é chamada (Removemos o conteud anterior)
			FragmentTransaction fts= getSupportFragmentManager().beginTransaction();
			fts.remove(frag);
			//fts.replace(R.id.fragmentContainer, frag);
			fts.commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent= new Intent(this,MainActivity.class);
			//Para tirar todas as atividades da pilha e reiniciar
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//Para permanecer na tab selecionada ao alterar a orientacao de tela
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		//Pega o indice da tab atual selecionada
		outState.putInt("indiceTab", getActionBar().getSelectedNavigationIndex());
	}
}
