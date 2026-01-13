package com.nokia.ace.connector.sonar.service;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nokia.ace.connectors.common.AceConnector;
import com.nokia.ace.connectors.common.AceCoreConnectorService;
import com.nokia.ace.connectors.common.input.ConnectorInputData;
import com.nokia.ace.connectors.common.output.ConnectorOutputData;
import com.nokia.ace.core.commons.exception.ACEBaseException;

@Component
@Qualifier("SonarConnectorServiceImpl")
public class SonarConnectorServiceImpl implements AceCoreConnectorService {

	@Autowired
	@Qualifier("SonarConnector")
	private AceConnector<ConnectorInputData, ConnectorOutputData> sonarConnector;
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SonarConnectorServiceImpl.class);

	@Override
	public ConnectorOutputData callConnector(ConnectorInputData sonarInput) throws ACEBaseException {
		LOGGER.info("Triggering sonar connector");
		ConnectorOutputData retVal = null;
		try {
			if (sonarInput != null) {
				if (sonarInput.getConnector() == null) {
					retVal = sonarConnector.connect(sonarInput);
				} else {
					retVal = (ConnectorOutputData) sonarInput.getConnector().connect(sonarInput);
				}
			}
		} catch (RuntimeException e) {
			LOGGER.error("Unable to connect to sonar connector");
		}
		return retVal;
	}

}
