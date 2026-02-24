package io.github.sanghyeon.boardservice.global.security;

import io.github.sanghyeon.boardservice.domain.member.entity.Member;
import io.github.sanghyeon.boardservice.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SessionAuthFlowTest {

    @Autowired MockMvc mockMvc;
    @Autowired MemberRepository memberRepository;
    @Autowired PasswordEncoder passwordEncoder;

    /***
     * 테스트용 계정 준비 (로그인 성공을 위해 DB에 존재해야 함)
     */
    @BeforeEach
    void setUp() {
        Member testMember = new Member(
                "test1@example.com",
                passwordEncoder.encode("1234"),
                "test1"
        );
        memberRepository.save(testMember);
    }

    @Test
    void login_me_logout_me401_flow() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "test1@example.com")
                        .param("password", "1234"))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);
        assertThat(session).isNotNull();

        mockMvc.perform(get("/api/auth/test").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test1@example.com"))
                .andExpect(jsonPath("$.nickname").value("test1"))
                .andExpect(jsonPath("$.authorities[0]").value("ROLE_USER"));

        mockMvc.perform(post("/api/auth/logout")
                        .with(csrf())
                        .session(session))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/auth/test").session(session))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void me_without_login_is_401() throws Exception {
        mockMvc.perform(get("/api/auth/test"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_wrong_password_is_401() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "test1@example.com")
                        .param("password", "WRONG"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_without_csrf_is_403() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "test1@example.com")
                        .param("password", "1234"))
                .andExpect(status().isForbidden());
    }

    @Test
    void logout_without_csrf_is_403() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "test1@example.com")
                        .param("password", "1234"))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);
        assertThat(session).isNotNull();

        mockMvc.perform(post("/api/auth/logout").session(session))
                .andExpect(status().isForbidden());
    }

    @Test
    void login_with_invalid_csrf_is_403() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .with(csrf().useInvalidToken())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "test1@example.com")
                        .param("password", "1234"))
                .andExpect(status().isForbidden());
    }
}