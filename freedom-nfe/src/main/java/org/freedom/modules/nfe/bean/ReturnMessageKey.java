package org.freedom.modules.nfe.bean;


public class ReturnMessageKey {
	
	private Integer codeReturn;
	private String message;
	private String validKey;
	
	public ReturnMessageKey(Integer codereturn, String message, String validKey) {
		this.codeReturn = codereturn;
		this.message = message;
		this.validKey = validKey;
	}

	public Integer getCodeReturn() {
		return codeReturn;
	}
	public String getMessage() {
		return message;
	}
	public String isValidKey(){
		return validKey;
	}
}
