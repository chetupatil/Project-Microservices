package com.nokia.ace.connector.sonar.error;

import java.util.List;

import com.nokia.ace.core.commons.exception.ACEBaseException;
import com.nokia.ace.core.commons.exception.AceError;

public class SonarExecutionException extends ACEBaseException {
	public SonarExecutionException() {
		super();
	}

	public SonarExecutionException(String exceptionMessage) {
		super(exceptionMessage);
	}

	public SonarExecutionException(String exceptionMessage, Throwable t) {
		super(exceptionMessage, t);
	}

	public SonarExecutionException(List<AceError> errors) {
		super(errors);
	}

	public SonarExecutionException(AceError error) {
		super(error);
	}

	public SonarExecutionException(String exceptionMessage, List<AceError> errors) {
		super(exceptionMessage, errors);
	}

	public SonarExecutionException(String exceptionMessage, AceError error) {
		super(exceptionMessage, error);
	}

	public SonarExecutionException(String exceptionMessage, List<AceError> errors, Throwable t) {
		super(exceptionMessage, errors, t);
	}

	public SonarExecutionException(String exceptionMessage, AceError error, Throwable t) {
		super(exceptionMessage, error, t);
	}

}
