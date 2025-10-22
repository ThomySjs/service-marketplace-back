package com.servicemarketplace.api.services.impl;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.servicemarketplace.api.config.JwtUtils;
import com.servicemarketplace.api.config.TokenTypes;
import com.servicemarketplace.api.services.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final JwtUtils jwtUtils;

    private final String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public boolean validateAddress(String email) {
        return Pattern.compile(regex)
            .matcher(email)
            .matches();
    }

    @Override
    @Async
    public void sendConfirmationEmail(String to, String appUrl) {

        //Obtiene la url de la aplicacion y la concatena con el token para ser enviado como link de confirmacion
        StringBuilder verificationRoute = new StringBuilder();
        verificationRoute.append(appUrl);
        verificationRoute.append("/auth/verify?token=");
        verificationRoute.append(jwtUtils.generateToken(to, TokenTypes.CONFIRMATION));

        //Crea el contexto para guardar las variables y despues las procesa junto con el template de thymeleaf
        Context context = new Context();
        context.setVariable("url", verificationRoute.toString());
        String processedString = templateEngine.process("email-template", context);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setSubject("Confirmation email");
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setText(processedString, true);
            mailSender.send(message);
        }catch (MessagingException e) {
            log.info("Ocurrio un error el enviar el email.");
        }
    }

    @Override
    @Async
    public void sendRecoveryCode(String to, int code) {
        //Crea el contexto para guardar las variables y despues las procesa junto con el template de thymeleaf
        Context context = new Context();
        context.setVariable("code", code);
        String processedString = templateEngine.process("recovery-code", context);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setSubject("Recovery code");
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setText(processedString, true);
            mailSender.send(message);
        }catch (MessagingException e) {
            log.info("Ocurrio un error el enviar el email.");
        }
    }

}
