package org.nus.cs5223.game.vo;

public class ErrorMessage extends Message {

	private String error;

	public ErrorMessage(String error) {
		super();
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
