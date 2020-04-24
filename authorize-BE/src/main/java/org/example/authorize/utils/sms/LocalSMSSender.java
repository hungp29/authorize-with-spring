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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
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
        String smsFileName = Paths.get(smsLocalFolder, phone + Constants.UNDERSCORE +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.DATE_FM_FILE_NAME)) + FILE_EXTENSION)
                .toString();

        try {
            String message = patternFormatter.mergeDataAndTemplate(template, data);
            PrintWriter writer = new PrintWriter(smsFileName, "UTF-8");
            writer.println(message);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void afterPropertiesSet() {
        smsLocalFolder = Paths.get(System.getProperty("user.home"), appProps.getName()).toString();
        if (!Files.exists(Paths.get(smsLocalFolder))) {
            try {
                Files.createDirectories(Paths.get(smsLocalFolder));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
