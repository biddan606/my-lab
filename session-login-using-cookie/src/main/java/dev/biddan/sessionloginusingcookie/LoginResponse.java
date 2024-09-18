package dev.biddan.sessionloginusingcookie;

import lombok.Builder;

@Builder
public record LoginResponse(
        String sessionId
) {

}
