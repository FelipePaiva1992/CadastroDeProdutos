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
		// Evita que o método seja executado varias vezes.
		// Se tirar ele entre em loop
		if (isUpdating) {
			isUpdating = false;
			return;
		}

		isUpdating = true;
		String str = s.toString();
		// Verifica se já existe a máscara no texto.
		boolean hasMask = ((str.indexOf("R$") > -1 || str.indexOf("$") > -1) && (str
				.indexOf(".") > -1 || str.indexOf(",") > -1));
		// Verificamos se existe máscara
		if (hasMask) {
			// Retiramos a máscara.
			str = str.replaceAll("[R$]", "").replaceAll("[,]", "")
					.replaceAll("[.]", "");
		}

		try {
			// Transformamos o número que está escrito no EditText em
			// monetário.
			str = nf.format(Double.parseDouble(str) / 100);
			et.setText(str);
			et.setSelection(et.getText().length());
		} catch (NumberFormatException e) {
			s = "";
		}
	}

}