package condominio.proyecto_condominio.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.File;
import java.util.Properties;

public class CorreoService {

    // 👇 ideal: cargar esto desde config, no hardcode
    final String correo = "pruebas.unitarias45@gmail.com";
    final String password = "eakd bkvi tniv tthm";

    public void enviarCorreo(String rutaPDF, String destinatario) throws Exception {

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        props.put("mail.mime.charset", "UTF-8");
        props.put("mail.smtp.connectiontimeout", "4000");
        props.put("mail.smtp.timeout", "3000");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(correo, password);
            }
        });

        // 🔥 IMPORTANTE: evita “spam score bajo”
        session.setDebug(false);

        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(correo, "Sistema de Condominio"));

        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(destinatario)
        );

        message.setSubject(new String("Comprobante de pago - Condominio".getBytes(), "UTF-8"));

        // 🔥 Texto más humano (esto reduce spam)
        MimeBodyPart texto = new MimeBodyPart();

        String html
                = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "</head>"
                + "<body style='margin:0;padding:0;background:#eef2f7;font-family:Arial'>"
                + "<div style='max-width:640px;margin:30px auto;background:#ffffff;border-radius:14px;overflow:hidden;box-shadow:0 10px 25px rgba(0,0,0,0.08)'>"
                // HEADER
                + "<div style='background:linear-gradient(135deg,#1e3a8a,#2563eb);padding:22px;text-align:center;color:white'>"
                + "<h1 style='margin:0;font-size:20px;letter-spacing:1px'>Condominio Vista Verde</h1>"
                + "<p style='margin:5px 0 0;font-size:12px;opacity:0.9'>Notificación oficial de sistema</p>"
                + "</div>"
                // CUERPO
                + "<div style='padding:28px;color:#333'>"
                + "<h2 style='margin:0 0 10px;font-size:18px;color:#111'>Confirmación de transacción</h2>"
                + "<p style='font-size:14px;line-height:1.6;color:#444'>"
                + "Estimado residente,"
                + "</p>"
                + "<p style='font-size:14px;line-height:1.6;color:#444'>"
                + "Le informamos que se ha registrado correctamente un movimiento en el sistema administrativo de "
                + "<b>Condominio Vista Verde</b>."
                + "</p>"
                // BLOQUE INFO
                + "<div style='margin-top:20px;padding:18px;border-radius:10px;background:#f8fafc;border:1px solid #e5e7eb'>"
                + "<p style='margin:0;font-size:13px;color:#555'><b>Fecha de registro:</b> "
                + java.time.LocalDate.now()
                + "</p>"
                + "<p style='margin:8px 0 0;font-size:13px;color:#555'><b>Tipo de operación:</b> Pago de cuota mensual</p>"
                + "<p style='margin:8px 0 0;font-size:13px;color:#555'><b>Origen:</b> Sistema interno Vista Verde</p>"
                + "</div>"
                // MENSAJE SEGURIDAD
                + "<div style='margin-top:22px;font-size:13px;color:#666;line-height:1.5'>"
                + "Este mensaje ha sido generado automáticamente. Por seguridad, no comparta este correo."
                + "</div>"
                + "</div>"
                // FOOTER
                + "<div style='text-align:center;padding:18px;font-size:12px;color:#888;background:#f1f5f9'>"
                + "© " + java.time.Year.now().getValue() + " Condominio Vista Verde. Todos los derechos reservados."
                + "</div>"
                + "</div>"
                + "</body></html>";

        texto.setContent(html, "text/html; charset=UTF-8");

        // 📎 adjunto
        MimeBodyPart adjunto = new MimeBodyPart();

        File file = new File(rutaPDF);
        adjunto.attachFile(file);

        adjunto.setFileName("Comprobante_Pago.pdf");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(texto);
        multipart.addBodyPart(adjunto);

        message.setContent(multipart);

        // 🔥 headers anti-spam básicos
        message.setHeader("X-Mailer", "JavaMail");
        message.setHeader("Precedence", "bulk");

        Transport.send(message);

        System.out.println("Correo enviado correctamente");
    }

    public void enviarCorreoPagosPendientes(
            String rutaPDF,
            String destinatario,
            String mes,
            int anio
    ) throws Exception {

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        props.put("mail.mime.charset", "UTF-8");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(correo, password);
            }
        });

        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(
                correo,
                "Condominio Vista Verde - Administración"
        ));

        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(destinatario)
        );

        message.setSubject(
                "📊 Reporte de Pagos Pendientes | " + mes + " " + anio
        );

        // =========================
        // HTML MÁS PRO
        // =========================
        String html
                = "<!DOCTYPE html>"
                + "<html>"
                + "<body style='margin:0;padding:0;background:#eef2f7;font-family:Arial'>"
                + "<div style='max-width:640px;margin:30px auto;background:#ffffff;border-radius:14px;overflow:hidden;box-shadow:0 10px 25px rgba(0,0,0,0.08)'>"
                // HEADER (igual base pero más claro admin)
                + "<div style='background:linear-gradient(135deg,#1e3a8a,#2563eb);padding:22px;text-align:center;color:white'>"
                + "<h1 style='margin:0;font-size:20px'>Condominio Vista Verde</h1>"
                + "<p style='margin:5px 0 0;font-size:12px;opacity:0.9'>Panel de Administración - Reporte de Morosidad</p>"
                + "</div>"
                // CUERPO
                + "<div style='padding:28px;color:#333'>"
                + "<h2 style='margin:0 0 10px;font-size:18px'>Reporte de pagos pendientes</h2>"
                + "<p style='font-size:14px;line-height:1.6'>"
                + "Este correo está dirigido al <b>administrador del sistema</b>. "
                + "Se adjunta el reporte actualizado de propietarios con pagos pendientes."
                + "</p>"
                // INFO BOX MEJORADO
                + "<div style='margin-top:18px;padding:14px;border-radius:10px;background:#f8fafc;border:1px solid #e5e7eb'>"
                + "<p style='margin:0;font-size:13px'><b>📅 Período:</b> " + mes + " " + anio + "</p>"
                + "<p style='margin:8px 0 0;font-size:13px'><b>🕒 Generado:</b> " + java.time.LocalDate.now() + "</p>"
                + "</div>"
                // ALERTA ADMIN (nuevo toque importante)
                + "<div style='margin-top:16px;padding:12px;border-left:4px solid #f59e0b;background:#fffbeb;border-radius:8px'>"
                + "<p style='margin:0;font-size:13px;color:#92400e'>"
                + "⚠ Este reporte contiene únicamente información de control administrativo."
                + "</p>"
                + "</div>"
                + "<p style='margin-top:18px;font-size:13px;color:#555'>"
                + "El archivo adjunto contiene el detalle completo de casas con pagos pendientes."
                + "</p>"
                + "</div>"
                // FOOTER (igual pero más claro)
                + "<div style='text-align:center;padding:16px;font-size:12px;color:#888;background:#f1f5f9'>"
                + "Sistema de Administración • Condominio Vista Verde © "
                + java.time.Year.now().getValue()
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        MimeBodyPart texto = new MimeBodyPart();
        texto.setContent(html, "text/html; charset=UTF-8");

        // =========================
        // ADJUNTO
        // =========================
        MimeBodyPart adjunto = new MimeBodyPart();

        File file = new File(rutaPDF);
        adjunto.attachFile(file);

        adjunto.setFileName(
                "Reporte_Pagos_Pendientes_" + mes + "_" + anio + ".pdf"
        );

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(texto);
        multipart.addBodyPart(adjunto);

        message.setContent(multipart);

        Transport.send(message);

        System.out.println("📨 Correo enviado al administrador correctamente");
    }

    public void enviarCredencialesBD(String destinatario, String usuario, String contrasenaBD) throws Exception {

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.mime.charset", "UTF-8");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(correo, password);
            }
        });

        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(correo, "Sistema de Condominio"));

        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(destinatario)
        );

        message.setSubject("Credenciales de Acceso al Sistema");

        String html
                = "<!DOCTYPE html>"
                + "<html>"
                + "<body style='font-family:Arial;background:#eef2f7;padding:20px'>"
                + "<div style='max-width:600px;margin:auto;background:#fff;padding:25px;border-radius:10px'>"
                + "<h2 style='color:#1e3a8a'>Acceso al sistema</h2>"
                + "<p>Se te recuerda tus credenciales de acceso:</p>"
                + "<hr>"
                + "<p><b>Usuario:</b> " + usuario + "</p>"
                + "<p><b>Contraseña:</b> " + contrasenaBD + "</p>"
                + "<hr>"
                + "<p style='font-size:12px;color:#666'>Si no solicitaste esta información, ignora este correo.</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        MimeBodyPart texto = new MimeBodyPart();
        texto.setContent(html, "text/html; charset=UTF-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(texto);

        message.setContent(multipart);

        Transport.send(message);

        System.out.println("Credenciales enviadas correctamente");
    }
}
