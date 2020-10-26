package com.oneape.octopus.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
@ConfigurationProperties("mail")
public class MailProperties implements Serializable {
    private String smtpServer;
    private String smtpUsername;
    private String smtpPassword;

    private String smtpPort;
    private String secureSmtp;

    private boolean sessionDebug = false;
}
