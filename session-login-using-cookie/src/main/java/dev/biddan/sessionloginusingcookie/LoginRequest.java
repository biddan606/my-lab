package dev.biddan.sessionloginusingcookie;

import lombok.Builder;

@Builder
public record LoginRequest(
        String id,
        String password
) {

}
