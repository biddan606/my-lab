package dev.biddan.sessionloginusingcookie;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AuthenticationController.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @DisplayName("아이디와 비밀번호가 매칭되면 세션 쿠키를 설정한다.")
    @Test
    void login_success() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
                .id("userId")
                .password("password")
                .build();
        String requestJson = objectMapper.writeValueAsString(request);

        String createdSessionId = "new-session-id";
        given(authenticationService.createSession(request))
                .willReturn(LoginSession.builder()
                        .accessId(createdSessionId)
                        .build());

        // when & then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(cookie().value("SESSION", createdSessionId))
                .andDo(print());
    }

    @Test
    @DisplayName("유효한 세션이라면 200 상태 코드를 반환한다")
    void validSession_success() throws Exception {
        // Given
        String validSessionId = "valid-session-id";
        willDoNothing().given(authenticationService).validateSession(validSessionId);

        // When & Then
        mockMvc.perform(get("/validate")
                        .cookie(new Cookie("SESSION", validSessionId)))
                .andExpect(status().isOk());
    }
}
