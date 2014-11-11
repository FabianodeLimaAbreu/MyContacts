package br.com.mycontacts.lista.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.mycontacts.lista.modelo.Contato;


public class ligacaoDAO extends SQLiteOpenHelper {
	private static final String DATABASE = "registroDB";
	private static final int VERSAO = 1;

	public ligacaoDAO(Context context) {
		super(context, DATABASE, null, VERSAO);
	}

	public void salva(Contato contato)  { //pegar os parametros do contato e não da ligacação
		ContentValues values = new ContentValues();
		Contato dados = new Contato();
		values.put("idContato", dados.getId());
		values.put("nome", dados.getNome());
		values.put("hora", dados.getHoraLigacao());
		values.put("operadora", dados.getOperadora());

		getWritableDatabase().insert("Ligacoes", null, values);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String ddl = "CREATE TABLE Ligacoes (id PRIMARY KEY, "
				+ "idContato INT, nome TEXT UNIQUE NOT NULL, hora TEXT, operadora TEXT);";
		db.execSQL(ddl);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String ddl = "DROP TABLE IF EXISTS Ligacoes";
		db.execSQL(ddl);
		this.onCreate(db);
	}

	public List<Contato> getLista() {
		String[] colunas = { "id", "nome", "hora", "operadora" };
		Cursor cursor = getWritableDatabase().query("Ligacoes", colunas, null,
				null, null, null, null);

		ArrayList<Contato> ligacoes = new ArrayList<Contato>();

		while (cursor.moveToNext()) {

			Contato ligacao = new Contato();

			ligacao.setId(cursor.getLong(0));
			ligacao.setNome(cursor.getString(2));
			ligacao.setHoraLigacao(cursor.getString(3));
			//ligacao.setOperadora(cursor.getString(4));

			ligacoes.add(ligacao);
		}
		return ligacoes;
	}
}
