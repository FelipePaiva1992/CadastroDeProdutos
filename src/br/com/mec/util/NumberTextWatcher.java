package br.com.mec.util;

import java.text.NumberFormat;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class NumberTextWatcher implements TextWatcher {
	
	private EditText et;

	public NumberTextWatcher(EditText et) {
		this.et = et;
	}

	@SuppressWarnings("unused")
	private static final String TAG = "NumberTextWatcher";

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	private boolean isUpdating = false;
	private NumberFormat nf = NumberFormat.getCurrencyInstance();

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// Evita que o m�todo seja executado varias vezes.
		// Se tirar ele entre em loop
		if (isUpdating) {
			isUpdating = false;
			return;
		}

		isUpdating = true;
		String str = s.toString();
		// Verifica se j� existe a m�scara no texto.
		boolean hasMask = ((str.indexOf("R$") > -1 || str.indexOf("$") > -1) && (str
				.indexOf(".") > -1 || str.indexOf(",") > -1));
		// Verificamos se existe m�scara
		if (hasMask) {
			// Retiramos a m�scara.
			str = str.replaceAll("[R$]", "").replaceAll("[,]", "")
					.replaceAll("[.]", "");
		}

		try {
			// Transformamos o n�mero que est� escrito no EditText em
			// monet�rio.
			str = nf.format(Double.parseDouble(str) / 100);
			et.setText(str);
			et.setSelection(et.getText().length());
		} catch (NumberFormatException e) {
			s = "";
		}
	}

}