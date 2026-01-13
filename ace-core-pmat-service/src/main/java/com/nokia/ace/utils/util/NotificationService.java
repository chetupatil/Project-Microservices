package com.nokia.ace.utils.util;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.nokia.ace.utils.constants.PmatConstants;

@Component
public class NotificationService  {
	@Autowired
	private JavaMailSender sender;
	@Autowired
	private TemplateEngine templateEngine;

	
	
	public void notifyDTM(String email, Map<String, Object> model) throws Exception {
		String[] emails = email.split(",");
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());
		String text = emailTemplate(model);
		helper.setFrom(PmatConstants.ACE_MAILID);
		helper.setTo(emails);
		helper.setSubject("PPA Notification");
		helper.setText(text, true);
		sender.send(message);
	}
	
	private String emailTemplate(Map<String, Object> model) {
		Context context = new Context();
		context.setVariables(model);
		return templateEngine.process(PmatConstants.EMAIL_TEMPLATE_PPA, context);	
	}


}
