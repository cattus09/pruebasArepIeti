package edu.escuelaing.arem.ASE.app.services;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class CorreoService {
    public static void enviarCorreo(String destinatario, String asunto, String mensaje) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true"); // Habilitar la autenticación

        // Crear una sesión de correo sin TLS
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("airetupalCVDS@gmail.com", "mizyyfjtlahjotik");
            }
        });

        try {
            Message correo = new MimeMessage(session);
            correo.setFrom(new InternetAddress("airetupalCVDS@gmail.com"));
            correo.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            correo.setSubject(asunto);
            correo.setText(mensaje);

            Transport.send(correo);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}