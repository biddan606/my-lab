package dev.biddan.sessionloginusingcookie;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UUIDSessionGenerator implements SessionIdGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
