package org.pahappa.systems.core.utils;

import com.cloudinary.Cloudinary;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sers.webutils.model.utils.SortField;

import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.util.Random;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class AppUtils {

    public static String MEMBER_ROLE_NAME = "Aapu member";
    public static String PROF_DATASET_NAME = "Professionals";

    public static String CLOUDINARY_CLOUD_NAME = "revival-gateway";
    public static String CLOUDINARY_API_KEY = "114516888855596";
    public static String CLOUDINARY_API_SECRET = "gG2NUayJtKGuFcQln1yvxYPTMzQ";
    public static String SCRIPTURES_API_KEY = "a07ad608d1a7d71629319bcf7acae795";
    public static String SCRIPTURES_API_BIBLE_ID = "de4e12af7f28f599-02";

    public static final String FLUTTER_SECRET_KEY = "FLWSECK-433eeec81d5f1e5bdb811111db9484dd-X";
    public final static SortField DEFAULT_SORT_FIELD = new SortField("Date Created Desc", "dateCreated", true);
    final static String senderGridApiKey = "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";

    private static String smtpHost, smtpPort, senderAddress, senderPassword;

    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpHost);
        mailSender.setPort(Integer.parseInt(smtpPort.trim()));

        mailSender.setUsername(senderAddress);
        mailSender.setPassword(senderPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    private JavaMailSender emailSender;

    public static void sendEmail(
            String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@revivalgateway.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setReplyTo("noreply@revivalgateway.com");
        new AppUtils().getJavaMailSender().send(message);

    }

    /**
     * Uploads image bytes to cloudinary and returns the public URL to access
     * the image. The public_id represents the name used to store the image. You
     * can also specify the directory by including a "/" in the public_id eg
     * myfolder/pics/myImageName
     *
     * @param contents - the image bytes
     * @param public_id - unique image identifier
     * @return
     */
    public String uploadCloudinaryImage(byte[] contents, String public_id) {
        System.out.println("Started image upload...");
        try {
            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", CLOUDINARY_CLOUD_NAME,
                    "api_key", CLOUDINARY_API_KEY,
                    "api_secret", CLOUDINARY_API_SECRET,
                    "secure", true));
            System.out.println(Arrays.toString(contents));
            Map uploadResult = cloudinary.uploader().upload(contents, ObjectUtils.asMap("public_id", public_id));
            String imageUrl = uploadResult.get("secure_url").toString();
            System.out.println("Image url = " + imageUrl);
            return imageUrl;
        } catch (IOException ex) {
            Logger.getLogger(AppUtils.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public static void main(String[] args) {
        sendEmail("mutebiraymond695@gmail.com", "Test", "testing...");
    }

    public static String convertToYouTubeEmbededUrl(String url) {
        return url.replace("watch?v=", "embed/");

    }

    public static String convertToYouTubeWatchableUrl(String url) {
        return url.replace("embed/", "watch?v=");

    }

    public static String getRandomString(int length) {
        final Random random = new Random();
        String ALLOWED_CHARACTERS = "0123456789QWERTYUIOPASDFGHJKLZXCVBNM";
        final StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        }
        return sb.toString();
    }

}
