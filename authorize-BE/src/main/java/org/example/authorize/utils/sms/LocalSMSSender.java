package org.example.authorize.utils.sms;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authorize.config.prop.ApplicationProperties;
import org.example.authorize.utils.PatternFormatter;
import org.example.authorize.utils.constants.Constants;
import org.example.authorize.utils.constants.SystemConstants;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Local SMS Sender.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Profile({SystemConstants.SPRING_PROFILE_LOCAL})
public class LocalSMSSender implements SMSSender, InitializingBean {

    private final ApplicationProperties appProps;
    private final PatternFormatter patternFormatter;

    private String smsLocalFolder;
    private static final String FILE_EXTENSION = ".otp";

    @Override
    public void sendSMS(String phone, String template, Map<String, String> data) {
        Path smsFilePath = Paths.get(smsLocalFolder, phone + Constants.UNDERSCORE +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.DATE_FM_FILE_NAME)) + FILE_EXTENSION);

        try {
            String message = patternFormatter.mergeDataAndTemplate(template, data);
            Files.write(smsFilePath, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.warn("Error occurs while save OTP message to file in local environment", e);
        }
    }

    @Override
    public void afterPropertiesSet() {
        smsLocalFolder = Paths.get(System.getProperty("user.home"), appProps.getName()).toString();
        if (!Files.exists(Paths.get(smsLocalFolder))) {
            try {
                Files.createDirectories(Paths.get(smsLocalFolder));
            } catch (IOException e) {
                log.error("Error occurs while create folder to save OTP value in local environment", e);
            }
        }
    }
}
