package no.fint.oneroster.model.vocab;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusType {
	ACTIVE("active"), TOBEDELETED("tobedeleted");

	private final String status;

	StatusType(String status) { this.status = status; }

	@JsonValue
	public String getStatus() { return status; }
}