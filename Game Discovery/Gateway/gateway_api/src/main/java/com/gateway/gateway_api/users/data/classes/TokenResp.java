package com.gateway.gateway_api.users.data.classes;

public class TokenResp {

	private Boolean isValid;
	
	public TokenResp() {
		
	}

	public TokenResp(Boolean isValid) {
		this.isValid = isValid;
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}
}
