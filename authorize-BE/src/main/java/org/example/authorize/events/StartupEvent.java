package org.example.authorize.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Startup event class.
 */
@Slf4j
@Component
public class StartupEvent {

    /**
     * Once the app is ready, it will check for the existence of an Super Admin account,
     * if it doesn't exist, create a new one.
     *
     * @author hungp
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onStartApp() {

    }
}
