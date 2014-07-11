package br.com.mec.model;

public class Pagamento {

	private String PspReference;
	private String ResultCode;
	private String AuthCode;
	private String RefusalReason;
	
	public String getPspReference() {
		return PspReference;
	}
	public void setPspReference(String pspReference) {
		PspReference = pspReference;
	}
	public String getResultCode() {
		return ResultCode;
	}
	public void setResultCode(String resultCode) {
		ResultCode = resultCode;
	}
	public String getAuthCode() {
		return AuthCode;
	}
	public void setAuthCode(String authCode) {
		AuthCode = authCode;
	}
	public String getRefusalReason() {
		return RefusalReason;
	}
	public void setRefusalReason(String refusalReason) {
		RefusalReason = refusalReason;
	}
	
	
}
