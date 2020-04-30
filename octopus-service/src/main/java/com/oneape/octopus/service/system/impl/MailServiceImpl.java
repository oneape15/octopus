package com.oneape.octopus.service.system.impl;

import com.oneape.octopus.config.props.MailProperties;
import com.oneape.octopus.service.system.MailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Resource
    private MailProperties mailProps;

    /**
     * 发送简单邮件
     *
     * @param tos     String 接收人 多个以";"隔开
     * @param title   String 邮件标题
     * @param content String 邮件内容
     */
    @Override
    public void sendSimpleMail(String tos, String title, String content) throws Exception {
        sendSimpleMail(null, tos, null, null, title, content);
    }

    /**
     * 发送简单邮件
     *
     * @param from    String 发送人
     * @param tos     String 接收人 多个以";"隔开
     * @param cc      String 抄送人 多个以";"隔开
     * @param bcc     String 密送   多个以";"隔开
     * @param title   String 邮件标题
     * @param content String 邮件内容
     */
    @Override
    public void sendSimpleMail(String from, String tos, String cc, String bcc, String title, String content) throws Exception {
        sendCustomMailWithFrom(from, tos, cc, bcc, title, content, null);
    }

    /**
     * 发送自定义邮件（不需要通过模板）
     *
     * @param from        发送人
     * @param tos         String 接收人 多个以";"隔开
     * @param cc          String 抄送人 多个以";"隔开
     * @param bcc         String 密送   多个以";"隔开
     * @param title       String 邮件标题
     * @param content     String 邮件内容
     * @param fileStreams 附件列表，key：文件名，value：byte文件流
     */
    @Override
    public void sendCustomMailWithFrom(String from,
                                       String tos,
                                       String cc,
                                       String bcc,
                                       String title,
                                       String content,
                                       Map<String, byte[]> fileStreams) throws Exception {

        if (StringUtils.isBlank(from)) {
            from = mailProps.getSmtpUsername();
        }
        if (StringUtils.isBlank(content)) {
            content = "您好";
        }
        Properties props = new Properties();
        if (StringUtils.equals(mailProps.getSecureSmtp(), "starttls")) {
            props.put("mail.smtp.starttls.enable", "true");
        }
        if (StringUtils.isNotBlank(mailProps.getSmtpPort())) {
            props.put("mail.smtp.port", mailProps.getSmtpPort());
        }
        props.put("mail.smtp.host", mailProps.getSmtpServer());

        // If you're sending to multiple recipients, if one recipient address fails, by default no email is sent to the other recipients
        // set the sendpartial property to true to have emails sent to the valid addresses, even if invalid ones exist
        props.put("mail.smtp.sendpartial", "true");

        Session session;
        if (StringUtils.isNoneBlank(mailProps.getSmtpUsername(), mailProps.getSmtpPassword())) {
            props.put("mail.smtp.auth", "true");
            session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mailProps.getSmtpUsername(), mailProps.getSmtpPassword());
                }
            });
        } else {
            session = Session.getDefaultInstance(props, null);
        }

        //enable session debug
        session.setDebug(true);

        // Create a message
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));

        // Set the TO: address(es)
        if (tos != null) {
            InternetAddress[] addressTo = changeAddress(tos);
            msg.setRecipients(Message.RecipientType.TO, addressTo);
        }

        // Set the CC: address(es)
        if (cc != null) {
            InternetAddress[] addressCc = changeAddress(cc);
            msg.setRecipients(Message.RecipientType.CC, addressCc);
        }

        // Set the BCC: address(es)
        if (bcc != null) {
            InternetAddress[] addressBcc = changeAddress(bcc);
            msg.setRecipients(Message.RecipientType.BCC, addressBcc);
        }

        // Setting the Subject and Content Type
        msg.setSubject(title);
        if (fileStreams == null || fileStreams.size() == 0) {
            msg.setContent(content, "text/html;charset=utf-8");
        } else {
            MimeMultipart mp = new MimeMultipart();

            MimeBodyPart text = new MimeBodyPart();
            text.setDisposition(Part.INLINE);
            text.setContent(content, "text/html;charset=utf-8");
            mp.addBodyPart(text);
            for (String key : fileStreams.keySet()) {
                MimeBodyPart filePart = new MimeBodyPart();
                byte[] bytes = fileStreams.get(key);
                ByteArrayDataSource bds = new ByteArrayDataSource(bytes, "application/octet-stream");
                DataHandler dh = new DataHandler(bds);
                filePart.setFileName(MimeUtility.encodeText(key));
                filePart.setDisposition(Part.ATTACHMENT);
                filePart.setDescription("Attached file: " + key);
                filePart.setDataHandler(dh);
                mp.addBodyPart(filePart);
            }

            msg.setContent(mp);
        }

        // UpdateStatus the e-mail
        Transport.send(msg);
    }

    private InternetAddress[] changeAddress(String strAddress) throws AddressException {
        String[] arr = StringUtils.split(strAddress, ";");
        InternetAddress[] addresses = new InternetAddress[arr.length];
        for (int i = 0; i < arr.length; i++) {
            addresses[i] = new InternetAddress(arr[i]);
        }
        return addresses;
    }
}
