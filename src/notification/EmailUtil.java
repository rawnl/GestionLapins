package notification;

//import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
//import java.util.TimeZone;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {

    final static String fromEmail = "lapinselbenna.management@gmail.com";
    final static String password = "lapinselbennapassword";

    public static void sendEmail(String toEmail, String subject, String body){
        try{
            
            System.out.println(fromEmail+password);
            Authenticator authenticator = new Authenticator(){
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            };

            System.out.println(authenticator.toString());
            
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
            properties.put("mail.smtp.port", "587"); // TLS Port
            properties.put("mail.smtp.auth", "true"); // Enable Authentication
            properties.put("mail.smtp.starttls.enable", "true"); // Enable StartTLS
            properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            
            Session session = Session.getInstance(properties, authenticator);
            MimeMessage msg = new MimeMessage(session);

            //Message Headers :
            msg.addHeader("Content-type", "text/html; charset-UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(fromEmail, "NO-reply-JD"));;
            msg.setReplyTo(InternetAddress.parse(fromEmail, false));
            msg.setSubject(subject, "UTF-8");
            msg.setText(body,"UTF-8");
            
            Date sendDate = new Date();
            
            sendDate.setYear(2021-1900);
            sendDate.setMonth(03);
            sendDate.setDate(02);
            sendDate.setHours(16);
            sendDate.setMinutes(59);
            sendDate.setSeconds(0);

            System.out.println(sendDate);
            System.out.println(sendDate.getTime());
            
            msg.setSentDate(sendDate);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

            Transport.send(msg);
            
            System.out.println("Email successfully sent");

        }catch (Exception e) {
            System.out.println("Error !");
            e.printStackTrace();
        }
    }
    
}
