package com.oneape.octopus.service;

import java.util.Map;

/**
 * 邮件发送接口
 */
public interface MailService {
    /**
     * 发送简单邮件
     *
     * @param tos     String 接收人 多个以";"隔开
     * @param title   String 邮件标题
     * @param content String 邮件内容
     */
    void sendSimpleMail(String tos,
                        String title,
                        String content) throws Exception;

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
    void sendSimpleMail(String from,
                        String tos,
                        String cc,
                        String bcc,
                        String title,
                        String content) throws Exception;

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
    void sendCustomMailWithFrom(String from,
                                String tos,
                                String cc,
                                String bcc,
                                String title,
                                String content,
                                Map<String, byte[]> fileStreams) throws Exception;
}
