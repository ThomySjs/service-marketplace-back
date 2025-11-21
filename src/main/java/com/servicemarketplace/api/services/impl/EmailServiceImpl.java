package com.servicemarketplace.api.services.impl;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.servicemarketplace.api.config.JwtUtils;
import com.servicemarketplace.api.config.TokenTypes;
import com.servicemarketplace.api.config.CustomConfig.BrevoConfig;
import com.servicemarketplace.api.services.EmailService;

import lombok.RequiredArgsConstructor;
import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sibApi.TransactionalEmailsApi;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final TemplateEngine templateEngine;
    private final JwtUtils jwtUtils;
    private final BrevoConfig brevoConfig;

    private final String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public boolean validateAddress(String email) {
        return Pattern.compile(regex)
            .matcher(email)
            .matches();
    }

    @Override
    @Async
    public void sendConfirmationEmail(String to, String appUrl, String username) {

        // URL de verificación
        String verificationUrl = appUrl + "/auth/verify?token=" + jwtUtils.generateToken(to, TokenTypes.CONFIRMATION);

        // Procesar template
        Context context = new Context();
        context.setVariable("confirmationUrl", verificationUrl);
        context.setVariable("username", username);
        String htmlContent = templateEngine.process("email-template", context);

        sendEmailUsingBrevo(to, "Confirmación de Correo Electrónico", htmlContent);
    }

    @Override
    @Async
    public void sendRecoveryCode(String to, int code, String expiration) {

        // Procesar template
        Context context = new Context();
        context.setVariable("code", code);
        context.setVariable("expirationMinutes", expiration);
        String htmlContent = templateEngine.process("recovery-code", context);

        sendEmailUsingBrevo(to, "Recuperación de Contraseña", htmlContent);
    }

    private void sendEmailUsingBrevo(String to, String subject, String htmlContent) {
        try {
            ApiClient client = Configuration.getDefaultApiClient();
            client.setApiKey(brevoConfig.getApiKey());

            TransactionalEmailsApi api = new TransactionalEmailsApi(client);

            SendSmtpEmail email = new SendSmtpEmail()
                    .sender(new SendSmtpEmailSender().email(brevoConfig.getDefaultSender()))
                    .to(List.of(new SendSmtpEmailTo().email(to)))
                    .subject(subject)
                    .htmlContent(htmlContent);

            api.sendTransacEmail(email);

        } catch (ApiException e) {
            System.err.println("Error al enviar email con Brevo");
            e.printStackTrace();
        }
    }

}
