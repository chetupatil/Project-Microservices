package com.nokia.ace.utils.sharepoint;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.drools.core.util.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.google.common.base.Joiner;
import com.nokia.ace.connectors.common.model.toolDetails.ToolResponse;
import com.nokia.ace.utils.constants.PmatConstants;

/**
 * @author Chetana
 *
 */
@Component
public class PmatSPTokenGenerator {

	@Autowired
	private RestTemplate restTemplate;

	private static final Logger LOGGER = LoggerFactory.getLogger(PmatSPTokenGenerator.class);

	public List<String> spTokenGen(ToolResponse spToolResponse){
		List<String> cookies = new ArrayList<>();
		String securityToken = generateSecurityTokenForSp(spToolResponse);
		if(!StringUtils.isEmpty(securityToken)) {
			cookies = fetchCookiesFromSecurityToken(securityToken,spToolResponse.getInstanceUrl());
		}
		return cookies;
	}


	public String fetchDigestValueForSp(List<String> cookies, ToolResponse spToolResponse){
		String formDigestValue = "";
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Cookie", Joiner.on(';').join(cookies));
			headers.add("Accept", PmatConstants.SP_ACCEPT);

			HttpEntity<String> entity = new HttpEntity<String>(null,headers);

			ResponseEntity<String> responseEntity = restTemplate
					.exchange(spToolResponse.getInstanceUrl() + PmatConstants.CONTEXT_INFO_SP, HttpMethod.POST, entity, String.class);
			JSONObject digestValueJson = new JSONObject(responseEntity.getBody());

			if(digestValueJson!=null) {
				formDigestValue = digestValueJson.getJSONObject("d").getJSONObject(PmatConstants.CONTEXT_WEB_INFO)
						.getString(PmatConstants.FORM_DIGEST_VALUE); 
			}
		}catch(Exception e) { 
			LOGGER.error("Exception occure while process digest value for sp: "+e.getMessage());
		}
		return formDigestValue; 

	}


	private List<String> fetchCookiesFromSecurityToken(String securityToken, String host) {
		List<String> cookies = new ArrayList<>();

		try {
			String apiUrl = host + PmatConstants.W_SIGN_IN_SP; 

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			HttpEntity<String> entity = new HttpEntity<String>(securityToken,headers);

			HttpHeaders headerResult = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class).getHeaders();

			cookies = headerResult.get(PmatConstants.SET_COOKIES);

		}catch(Exception ex) {
			LOGGER.error("Exception occure while process fetch cookies form security token for sp: "+ex.getMessage());
		}
		return cookies;
	}


	private String generateSecurityTokenForSp(ToolResponse spToolResponse) {
		String securityToken = "";

		try {

			String postString = buildSecuityTokenEnvelope(spToolResponse.getInstanceUrl(),spToolResponse.getUserName(),spToolResponse.getPassword());
			HttpHeaders headers = new HttpHeaders();
			headers.add("Accept", PmatConstants.APP_FORM_URL);
			headers.add("Content-Type", PmatConstants.APP_FORM_URL);
			HttpEntity<String> entity = new HttpEntity<String>(postString,headers);

			ResponseEntity<String> responseEntity = restTemplate.exchange(PmatConstants.SHAREPOINT_URL, HttpMethod.POST, entity, String.class);

			String spToken = responseEntity.getBody();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			if(!StringUtils.isEmpty(spToken)) {
				Document doc = builder.parse(new InputSource(new StringReader(spToken)));
				Node nodeName = doc.getFirstChild().getLastChild().getLastChild();
				Node n = nodeName.getFirstChild();
				while (n != null) {
					if(n.getNodeName().equals(PmatConstants.WST_REQUESTED_SECURITY_TOKEN)) {
						securityToken  = n.getFirstChild().getTextContent();
						break;
					}
					n = n.getNextSibling();
				}
			}
		}catch(Exception e) {
			LOGGER.error("Exception occure while process generationg security token for sp: "+e.getMessage());
		}
		return securityToken;	
	}


	private String buildSecuityTokenEnvelope(String hostUrl,String email, String password) {

		String paramString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<s:Envelope xmlns:a=\"http://www.w3.org/2005/08/addressing\" xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\r\n"
				+ "	<s:Header>\r\n"
				+ "		<a:Action s:mustUnderstand=\"1\">http://schemas.xmlsoap.org/ws/2005/02/trust/RST/Issue</a:Action>\r\n"
				+ "		<a:ReplyTo>\r\n"
				+ "			<a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address>\r\n"
				+ "		</a:ReplyTo>\r\n"
				+ "		<a:To s:mustUnderstand=\"1\">https://login.microsoftonline.com/extSTS.srf</a:To>\r\n"
				+ "		<o:Security s:mustUnderstand=\"1\" xmlns:o=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\r\n"
				+ "			<o:UsernameToken>\r\n"
				+ "				<o:Username>"+email+"</o:Username>\r\n"
				+ "				<o:Password>"+password+"</o:Password>\r\n"
				+ "			</o:UsernameToken>\r\n"
				+ "		</o:Security>\r\n"
				+ "	</s:Header>\r\n"
				+ "	<s:Body>\r\n"
				+ "		<t:RequestSecurityToken xmlns:t=\"http://schemas.xmlsoap.org/ws/2005/02/trust\">\r\n"
				+ "			<wsp:AppliesTo xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\">\r\n"
				+ "				<a:EndpointReference>\r\n"
				+ "					<a:Address>"+hostUrl+"/_forms/default.aspx?wa=wsignin1.0</a:Address>\r\n"
				+ "				</a:EndpointReference>\r\n"
				+ "			</wsp:AppliesTo>\r\n"
				+ "			<t:KeyType>http://schemas.xmlsoap.org/ws/2005/05/identity/NoProofKey</t:KeyType>\r\n"
				+ "			<t:RequestType>http://schemas.xmlsoap.org/ws/2005/02/trust/Issue</t:RequestType>\r\n"
				+ "			<t:TokenType>urn:oasis:names:tc:SAML:1.0:assertion</t:TokenType>\r\n"
				+ "		</t:RequestSecurityToken>\r\n"
				+ "	</s:Body>\r\n"
				+ "</s:Envelope>";

		return paramString;

	}

}
