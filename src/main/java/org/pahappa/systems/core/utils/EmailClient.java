/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pahappa.systems.core.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import org.pahappa.systems.core.services.SystemSettingService;

import org.pahappa.systems.core.utils.sendgrid.Content;
import org.pahappa.systems.core.utils.sendgrid.Email;
import org.pahappa.systems.core.utils.sendgrid.Mail;
import org.pahappa.systems.models.SystemSetting;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.CustomLogger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 *
 * @author HP
 */
public class EmailClient {

    private static String SMTPHOST="smtp.gmail.com";
    private static int SMTP_PORT=587;
    private static String SMTP_ADDRESS="mail.pahappa@gmail.com";
    private static String SMTP_PASSWORD="pass@2020@Pahappa";
    private static String FROM_EMAIL="mail.pahappa@gmail.com";
    private static String SENDGRID_URL = "https://api.sendgrid.com/v3/mail/send";
    private static String SENDGRID_SENDER_ADDRESS = "mail.pahappa@gmail.com";
    private static String SENDGRID_BEARER_TOKEN = "";

    public static void initSendGridMailSettings() {
        SystemSetting appSetting = ApplicationContextProvider.getBean(SystemSettingService.class).getAppSetting();
        if (appSetting != null) {
            SENDGRID_URL = (appSetting.getSendGridUrl() != null || !appSetting.getSendGridUrl().isEmpty())
                    ? appSetting.getSendGridUrl()
                    : SENDGRID_URL;
            SENDGRID_SENDER_ADDRESS = (appSetting.getSendGridSenderAddress() != null
                    || !appSetting.getSendGridSenderAddress().isEmpty()) ? appSetting.getSendGridSenderAddress()
                    : SENDGRID_SENDER_ADDRESS;
            SENDGRID_BEARER_TOKEN = (appSetting.getSendGridBearerToken() != null
                    || !appSetting.getSendGridBearerToken().isEmpty()) ? appSetting.getSendGridBearerToken()
                    : SENDGRID_BEARER_TOKEN;

        }
    }

    public static void initMailSettings() {
          SystemSetting appSetting = ApplicationContextProvider.getBean(SystemSettingService.class).getAppSetting();

        SMTPHOST = (appSetting.getSmtpHost() != null) ? appSetting.getSmtpHost()
                : SMTPHOST;
        SMTP_PORT = Integer.parseInt(appSetting.getSmtpPort());
        SMTP_ADDRESS = (appSetting.getSmtpAddress() != null) ? appSetting.getSmtpAddress()
                : SMTPHOST;
        SMTP_PASSWORD = (appSetting.getSmtpPassword() != null) ? appSetting.getSmtpPassword()
                : SMTPHOST;
    }

    public static JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(SMTPHOST);
        mailSender.setPort(SMTP_PORT);

        mailSender.setUsername(SMTP_ADDRESS);
        mailSender.setPassword(SMTP_PASSWORD);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    public static void sendSimpleMessage(String to, String subject, String text) {
        initMailSettings();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@aapu.com");
        message.setTo(to);
        message.setFrom(FROM_EMAIL);
        message.setSubject(subject);
        message.setText(text);
        getJavaMailSender().send(message);

    }

    public static String sendGridSimpleMessage(String to, String subject, String html) throws IOException {
        initSendGridMailSettings();

        Email fromAddress = new Email(SENDGRID_SENDER_ADDRESS);
        Email toAddress = new Email(to);
        Content content = new Content("text/html", html);
        Mail mail = new Mail(fromAddress, subject, toAddress, content);
        String jsonBody = mail.build();

        CustomLogger.log("Json " + jsonBody);

        URLConnection urlConnection = new URL(SENDGRID_URL).openConnection();
        urlConnection.setDoOutput(true);

        HttpURLConnection http = (HttpURLConnection) urlConnection;
        http.setRequestMethod("POST");
        http.setRequestProperty("Accept", "application/json");
        http.setRequestProperty("Content-Type", "application/json");
        http.setRequestProperty("Authorization", String.format("Bearer %s", SENDGRID_BEARER_TOKEN));

        byte[] out = jsonBody.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = http.getOutputStream();
        stream.write(out);

        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
        http.disconnect();
        return http.getResponseMessage();
    }
}
