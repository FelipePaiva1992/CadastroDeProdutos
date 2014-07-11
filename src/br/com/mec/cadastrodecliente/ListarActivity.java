package br.com.mec.cadastrodecliente;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import br.com.mec.zxing.IntentIntegrator;
import br.com.mec.zxing.IntentResult;

public class ListarActivity extends Activity {

	static TextView txtValorTotal;
	TextView txtPreco;
	EditText txtQuant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listar);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		txtValorTotal = (TextView) findViewById(R.id.txtValorTotal);
		txtPreco = (TextView) findViewById(R.id.txtPreco);
		if (PagamentoActivity.estadodaCompra) {
			limparCarrinho();
		}

		somarTodosProdutos();
	}

	@Override
	protected void onResume() {
		super.onResume();

		SQLiteDatabase db = openOrCreateDatabase("produtos.db",
				Context.MODE_PRIVATE, null);

		// Tabela Clientes
		StringBuilder sqlCliente = new StringBuilder();
		sqlCliente.append("CREATE TABLE IF NOT EXISTS carrinho_produtos(");
		sqlCliente.append("_id INTEGER PRIMARY KEY,");
		sqlCliente.append("nome VARCHAR(50),");
		sqlCliente.append("preco VALRCHAR(50));");
		db.execSQL(sqlCliente.toString());

		Cursor cursor = db.rawQuery("SELECT * FROM carrinho_produtos", null);

		String[] from = { "_id", "nome", "preco" };
		int[] to = { R.id.txtID, R.id.txtNome, R.id.txtPreco };
		SimpleCursorAdapter ad = new SimpleCursorAdapter(getBaseContext(),
				R.layout.listar_model, cursor, from, to, 0);

		ListView ltwDados = (ListView) findViewById(R.id.listView1);

		ltwDados.setAdapter(ad);

		ltwDados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {

				SQLiteCursor c = (SQLiteCursor) adapter.getAdapter().getItem(
						position);
				ApagarClick(c.getInt(0));

			}
		});

		db.close();
	}

	@Override
	public void onBackPressed() {
		// do nothing
	}

	public void ApagarClick(int id) {
		try {
			final SQLiteDatabase db = openOrCreateDatabase("produtos.db",
					Context.MODE_PRIVATE, null);

			final int _id = id;

			Builder msg = new Builder(ListarActivity.this);
			msg.setMessage("Deseja apagar este produto?");
			msg.setNegativeButton("Não", null);
			msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (db.delete("carrinho_produtos", "_id=?",
							new String[] { String.valueOf(_id) }) > 0) {
						Toast.makeText(getBaseContext(),
								"Sucesso ao apagar o produto",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getBaseContext(),
								"Erro ao apagar o produto", Toast.LENGTH_SHORT)
								.show();
					}

					Intent it = new Intent(getBaseContext(),
							ListarActivity.class);
					startActivity(it);
				}

			});

			msg.show();

		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT)
					.show();
		}

	}

	public void somarTodosProdutos() {
		SQLiteDatabase db = openOrCreateDatabase("produtos.db",
				Context.MODE_PRIVATE, null);

		Cursor cursor = db
				.rawQuery("SELECT preco FROM carrinho_produtos", null);

		ArrayList<Double> arrayList = new ArrayList<Double>();
		while (cursor.moveToNext()) {
			try {
				arrayList.add(Double.valueOf(cursor
						.getString(cursor.getColumnIndex("preco"))
						.replaceAll("[R$]", "").replaceAll(",", ".")));
			} catch (Exception e) {
				Log.e("appTeste", e.getMessage());
			}
		}

		Double resultado = Double.valueOf(0);
		for (int i = 0; i < arrayList.size(); i++) {
			try {
				resultado = resultado + arrayList.get(i);
			} catch (Exception e) {
				Log.e("appTeste", e.getMessage());
			}

		}

		txtValorTotal.setText("R$" + resultado.toString());

	}

	public void limparCarrinhoClick(View v) {
		Builder msg = new Builder(ListarActivity.this);
		msg.setMessage("Deseja limpar este carrinho?");
		msg.setNegativeButton("Não", null);
		msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				SQLiteDatabase db = openOrCreateDatabase("produtos.db",
						Context.MODE_PRIVATE, null);

				// Tabela Clientes
				StringBuilder sqlCliente = new StringBuilder();
				sqlCliente.append("DROP TABLE carrinho_produtos;");
				db.execSQL(sqlCliente.toString());

				// Tabela Clientes
				sqlCliente = new StringBuilder();
				sqlCliente
						.append("CREATE TABLE IF NOT EXISTS carrinho_produtos(");
				sqlCliente.append("_id INTEGER PRIMARY KEY,");
				sqlCliente.append("nome VARCHAR(50),");
				sqlCliente.append("preco VALRCHAR(50));");
				db.execSQL(sqlCliente.toString());

				Toast.makeText(getBaseContext(), "Carrinho Limpo",
						Toast.LENGTH_SHORT).show();

				Intent it = new Intent(getBaseContext(), ListarActivity.class);
				startActivity(it);
			}

		});

		msg.show();

	}

	public void limparCarrinho() {

		SQLiteDatabase db = openOrCreateDatabase("produtos.db",
				Context.MODE_PRIVATE, null);

		// Tabela Clientes
		StringBuilder sqlCliente = new StringBuilder();
		sqlCliente.append("DROP TABLE carrinho_produtos;");
		db.execSQL(sqlCliente.toString());

		// Tabela Clientes
		sqlCliente = new StringBuilder();
		sqlCliente.append("CREATE TABLE IF NOT EXISTS carrinho_produtos(");
		sqlCliente.append("_id INTEGER PRIMARY KEY,");
		sqlCliente.append("nome VARCHAR(50),");
		sqlCliente.append("preco VALRCHAR(50));");
		db.execSQL(sqlCliente.toString());

		Toast.makeText(getBaseContext(), "Carrinho Limpo", Toast.LENGTH_SHORT)
				.show();

	}

	public void pagarClick(View v) {
		Builder msg = new Builder(ListarActivity.this);
		msg.setMessage("Deseja fechar o pedido?");
		msg.setNegativeButton("Não", null);
		msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent it = new Intent(getBaseContext(),
						PagamentoActivity.class);
				startActivity(it);
			}

		});

		msg.show();

	}

	public void adicionarProdutoClick(View v) {
		IntentIntegrator integrator = new IntentIntegrator(ListarActivity.this);
		integrator.initiateScan();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		
		if (scanResult != null) {
			if(scanResult.getContents()== null||scanResult.getContents() == ""){
				Toast.makeText(getBaseContext(), "Produto não encontrado",
						Toast.LENGTH_LONG).show();
			}else{
				if (scanResult.getContents().equals("7896843200034")) {
					CadastrarClick("Agua bioleve", "R$1,59");
				} else if (scanResult.getContents().equals("7899264301892")) {
					CadastrarClick("Caderno Velho", "R$5,99");
				} else if (scanResult.getContents().equals("7895800304228")) {
					CadastrarClick("Trident Aberto", "R$0,99");
				} else {
					Log.i("appTeste", scanResult.getContents());
					Toast.makeText(getBaseContext(), "Produto não encontrado",
							Toast.LENGTH_LONG).show();
				}
			}
			
		}
	}

	public void CadastrarClick(String nmProd, String vlProd) {

		try {
			SQLiteDatabase db = openOrCreateDatabase("produtos.db",
					Context.MODE_PRIVATE, null);

			ContentValues ctv = new ContentValues();
			ctv.put("nome", nmProd.toString());
			ctv.put("preco", vlProd.toString());

			if (db.insert("carrinho_produtos", "_id", ctv) > 0) {
				Toast.makeText(getBaseContext(),
						"Sucesso ao cadastrar o produto", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getBaseContext(), "Erro ao cadastrar o produto",
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT)
					.show();
		}

		Intent it = new Intent(getBaseContext(), ListarActivity.class);
		startActivity(it);
	}

	// public void somarPrecoTotal(){
	// try {
	// int quant = Integer.valueOf(txtQuant.getText().toString());
	// Double valor =
	// Double.valueOf(txtPreco.getText().toString().replaceAll("[R$]",
	// "").replaceAll(",", "."));
	// Double resultado = quant*valor;
	// txtValorTotal.setText(String.valueOf(resultado));
	// } catch (Exception e) {
	// Log.e("appTeste", e.getMessage());
	// }
	//
	// }

}
