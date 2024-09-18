package dev.biddan.sessionloginusingcookie;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        LoginSession loginSession = authenticationService.createSession(request);

        LoginResponse response = LoginResponse.builder()
                .sessionId(loginSession.getAccessId())
                .build();
        ResponseCookie cookie = ResponseCookie.from("SESSION")
                .value(response.sessionId())
                .maxAge(Duration.ofDays(30))
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    // @CookieValue의 required 설정값이 true이고, SESSION 쿠키값이 없을 경우 MissingRequestCookieException을 발생시킵니다.
    @GetMapping("/validate")
    public ResponseEntity<Void> validateSession(@CookieValue("SESSION") String sessionId) {
        authenticationService.validateSession(sessionId);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
