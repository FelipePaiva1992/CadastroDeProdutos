/*	
	{
	    "empresa": [
	        {
	            "adLogo": "resources/imagem/houston_trust.png",
	            "idEmpresas": "1",
	            "nmEmpresa": "Houston Trust",
	            "nuTelefone": "11 5181 5050"
	        },
	        {
	            "adLogo": "resources/imagem/acqua.png",
	            "idEmpresas": "2",
	            "nmEmpresa": "Acqua Manager RJ",
	            "nuTelefone": "21 2508 4200"
	        },
	        {
	            "adLogo": "resources/imagem/cocm.png",
	            "idEmpresas": "3",
	            "nmEmpresa": "Clinica Open Center",
	            "nuTelefone": "11 3772 5292"
	        },
	        {
	            "adLogo": "resources/imagem/mobilidade.png",
	            "idEmpresas": "4",
	            "nmEmpresa": "Mobilidade em Campo",
	            "nuTelefone": "11 5182 9999"
	        },
	        {
	            "adLogo": "resources/imagem/acqua.png",
	            "idEmpresas": "5",
	            "nmEmpresa": "Acqua Manager SP",
	            "nuTelefone": "11 5152 4040"
	        }
	    ]
	}
 */

package br.com.mec.WSRest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.util.JsonReader;
import android.util.Log;
import br.com.mec.model.Pagamento;

@SuppressLint("NewApi")
public class ConexaoWSRest {

    private static final String URL_WS = "http://portalacqua.sytes.net:8100/WSRestFull/api/";
	private static final String CATEGORIA = "appTeste";
    
    @SuppressWarnings("resource")
	@SuppressLint("NewApi")
   	public Pagamento EfetuarPagamento(String nomeMetodod, String path, ArrayList<ArrayList<String>> param) throws IOException{
       	
       	String link;
       	
       	//SE NÃO EXISTIR PARAMETROS
       	if(param == null){
       		link = URL_WS + path + nomeMetodod;
       	}else{
   		//SE EXISTIR PARAMETROS
       		String parametrosUrl = montarURLParam(param);
       		link = URL_WS + path + nomeMetodod + parametrosUrl;
       	}    	
       	Log.i(CATEGORIA, link);
       	
       	//CRIA UMA URL COM O LINK OBTIDO
       	URL url = null;
   		try {
   			url = new URL(link);
   		} catch (MalformedURLException e) {
   			Log.e(CATEGORIA, e.getMessage());
   		}
   		
   		//INICIA UM READER COM A URL
   		Reader forecastReader = new InputStreamReader(url.openStream());
   		
   		//INICIA UM JSONREADER A PARTIR DO READER OBTIDO ANTERIORMENTE
   		JsonReader forecastJsonReader = new JsonReader(forecastReader);
   		
   		String name;
   		
   		forecastJsonReader.beginObject();

   		
   		Pagamento pagamento = new Pagamento();
   		
   		//ENQUANTO EXISTIR ITEM NO ARRAY
   		while(forecastJsonReader.hasNext()){		

   			name = forecastJsonReader.nextName(); 

			if(name.equals("authCode")){
				pagamento.setAuthCode(forecastJsonReader.nextString());
			}else if(name.equals("pspReference")){
				pagamento.setPspReference(forecastJsonReader.nextString());
			}else if (name.equals("resultCode")) {
				pagamento.setResultCode(forecastJsonReader.nextString());
			} else if (name.equals("refusalReason")) {
				pagamento.setRefusalReason(forecastJsonReader.nextString());
			}  				
   			//FINALIZA O OBJETO ATUAL
   		}
   		
   		return pagamento;
       }
       
    
    
    @SuppressWarnings("resource")
	@SuppressLint("NewApi")
   	public String cancelarPagamento(String METHOD_NAME, String path, ArrayList<ArrayList<String>> param) throws IOException{
       	
    	String cancelado = null;
       	String link;
       	
       	//SE NÃO EXISTIR PARAMETROS
       	if(param == null){
       		link = URL_WS + path + METHOD_NAME;
       	}else{
   		//SE EXISTIR PARAMETROS
       		String METHOD_PARAMETERS = montarURLParam(param);
       		link = URL_WS + path + METHOD_NAME + METHOD_PARAMETERS;
       	}    	
       	Log.i(CATEGORIA, link);
       	
       	//CRIA UMA URL COM O LINK OBTIDO
       	URL url = null;
   		try {
   			url = new URL(link);
   		} catch (MalformedURLException e) {
   			Log.e(CATEGORIA, e.getMessage());
   		}
   		
   		//INICIA UM READER COM A URL
   		Reader forecastReader = new InputStreamReader(url.openStream());
   		
   		//INICIA UM JSONREADER A PARTIR DO READER OBTIDO ANTERIORMENTE
   		JsonReader forecastJsonReader = new JsonReader(forecastReader);
   		
   		String name;
   		
   		forecastJsonReader.beginObject();
   		
   		//ENQUANTO EXISTIR ITEM NO ARRAY
   		while(forecastJsonReader.hasNext()){		

   			name = forecastJsonReader.nextName(); 

			if(name.equals("cancelado")){
				cancelado  = forecastJsonReader.nextString();
			}				
   			//FINALIZA O OBJETO ATUAL
   		}
   		
   		return cancelado;
       }
       
    
    private String montarURLParam(List<ArrayList<String>> parameters){
    	String URL = "?";
    	for(int i = 0; i < parameters.size(); i++){
    		URL += parameters.get(i).get(0) + "=" + parameters.get(i).get(1);
    		URL += "&";
    	}
    	URL = URL.substring (0, URL.length() - 1);
    	return URL;
    }
    
}