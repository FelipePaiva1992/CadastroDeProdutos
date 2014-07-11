package br.com.mec.cadastrodecliente;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import br.com.mec.WSRest.ConexaoWSRest;
import br.com.mec.model.Pagamento;
import br.com.mec.util.NumberTextWatcher;

public class PagamentoActivity extends Activity {

	EditText txtNome;
	EditText txtCartao;
	EditText txtMes;
	EditText txtAno;
	EditText txtCodigo;
	EditText txtValor;
	static boolean estadodaCompra = false;
	
	private ProgressDialog dialog;
	private Handler handler = new Handler();
	private static final String CATEGORIA = "appTeste";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pagamento);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		txtNome = (EditText) findViewById(R.id.txtNome);

		txtCodigo = (EditText) findViewById(R.id.txtCodigo);
		
		txtCartao = (EditText) findViewById(R.id.txtCredCard);
		
		txtMes = (EditText) findViewById(R.id.txtMes);

		txtAno = (EditText) findViewById(R.id.txtAno);
		
		txtValor = (EditText) findViewById(R.id.txtValor);
		txtValor.addTextChangedListener(new NumberTextWatcher(txtValor));
		txtValor.setText(ListarActivity.txtValorTotal.getText());
		
	}
	
	public void efetuarPagamentoClick(View v) {
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(txtValor.getWindowToken(), 0);
		dialog = ProgressDialog.show(this, "", "Chamando o web service, por favor aguarde...", false, false);
		new Thread() {
			public void run() {
				try {
					ArrayList<ArrayList<String>> parameters = new ArrayList<ArrayList<String>>();

					ArrayList<String> nameValue = new ArrayList<String>();
					nameValue.add("nomeCompleto");
					nameValue.add(txtNome.getText().toString());
					parameters.add(nameValue);

					nameValue = new ArrayList<String>();
					nameValue.add("nuCartao");
					nameValue.add(txtCartao.getText().toString());
					parameters.add(nameValue);

					nameValue = new ArrayList<String>();
					nameValue.add("mesVencimetno");
					nameValue.add(txtMes.getText().toString());
					parameters.add(nameValue);

					nameValue = new ArrayList<String>();
					nameValue.add("anoVencimento");
					nameValue.add(txtAno.getText().toString());
					parameters.add(nameValue);

					nameValue = new ArrayList<String>();
					nameValue.add("codSeguranca");
					nameValue.add(txtCodigo.getText().toString());
					parameters.add(nameValue);

					nameValue = new ArrayList<String>();
					String valor = txtValor.getText().toString();
					valor = valor.replaceAll("[R$-,-.]", "");
					nameValue.add("valorPagamento");
					nameValue.add(valor);
					parameters.add(nameValue);

					// CHAMAR WS E POPULAR LISTA DE EMPRESAS
					ConexaoWSRest ws = new ConexaoWSRest();
					final Pagamento pagamento = ws.EfetuarPagamento("efetuarPagamento", "pagamento/", parameters);

					handler.post(new Runnable() {

						@Override
						public void run() {
							try {
								if (!pagamento.getResultCode().equals("Refused")) {
									Toast.makeText(getBaseContext(), "Sucesso ao efetuar o pagamento\nCod. Aprovação: " + pagamento.getAuthCode(), Toast.LENGTH_LONG).show();
									estadodaCompra = true;
								} else {
									Toast.makeText(getBaseContext(), "Erro ao efetuar o pagamento\nCod. Erro: " + pagamento.getRefusalReason(), Toast.LENGTH_LONG).show();
									estadodaCompra = false;
								}
							} catch (Exception e) {
								Log.e(CATEGORIA, "ResultCode é NULL");
							}

						}
					});

				} catch (Exception e) {
					Log.e(CATEGORIA, e.getMessage());

				} finally {
					dialog.dismiss();
					Intent it = new Intent(getBaseContext(), ListarActivity.class);
					startActivity(it);
				}
			}
		}.start();
		
	}

	

}
