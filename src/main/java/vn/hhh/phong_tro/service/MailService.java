package vn.hhh.phong_tro.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import vn.hhh.phong_tro.dto.ResetPasswordEmailDto;
import vn.hhh.phong_tro.util.EmailType;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String emailFrom;

    private final SpringTemplateEngine springTemplateEngine;

    /**
     * Send email by Google SMTP
     *
     * @param recipients
     * @param subject
     * @param content    //     * @param files
     * @return
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
//    public String sendEmail(String recipients, String subject, String content, MultipartFile[] files) throws UnsupportedEncodingException, MessagingException {
    public String sendEmail(String recipients, String subject, String content) throws UnsupportedEncodingException, MessagingException {
        log.info("Email is sending ...");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(emailFrom, "TimeKeeping");
//        message.setFrom(new InternetAddress(emailFrom, "TimeKeeping"));


//        if (recipients.contains(",")) { // send to multiple users
//            helper.setTo(InternetAddress.parse(recipients));
//        } else { // send to single user
//            helper.setTo(recipients.trim());
//        }
        // Send attach files
//        if (files != null) {
//            for (MultipartFile file : files) {
//                helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
//            }
//        }
        helper.setTo(recipients);
        helper.setSubject(subject);
//        if (type == EmailType.Auth) {
            helper.setText(content, true);
//        }
        mailSender.send(message);

        log.info("Email has sent to successfully, recipients: {}", recipients);

        return "Sent";
    }

    public <T> String sendEmailV2(String recipients, String subject, T data, EmailType type) throws MessagingException, UnsupportedEncodingException {
        log.info("Email is sending ...");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(emailFrom, "TimeKeeping");
        helper.setTo(recipients);
        helper.setSubject(subject);

        // Xử lý nội dung HTML từ template
        Context context = new Context();
        String htmlContent = "";

        if (type == EmailType.Auth) {
            // Ví dụ: data là ResetPasswordEmailDto
            ResetPasswordEmailDto resetData = (ResetPasswordEmailDto) data;
            context.setVariable("username", resetData.getPhone());
            context.setVariable("resetLink", resetData.getResetLink());

            htmlContent = springTemplateEngine.process("reset-password-email.html", context);
            helper.setText(htmlContent, true);
        }

        // Gửi mail
        mailSender.send(message);
        log.info("Email has sent to successfully, recipients: {}", recipients);

        return "Sent";
    }

}
