package com.project.staffguard;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.staffguard.dto.UserDTO;
import com.project.staffguard.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    private MockHttpSession adminSession;
    private MockHttpSession session;

    @BeforeEach
    void loginAsAdmin() throws Exception {
        var credentials = new UserDTO();
        credentials.setUsername("admin");
        credentials.setPassword("adminpass");

        MvcResult login = mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(credentials))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(request -> { request.setSecure(true); return request; })
                )
                .andExpect(status().isOk())
                .andReturn();

        session = (MockHttpSession) login.getRequest().getSession(false);
        assertThat(session).isNotNull();
    }


    @Test
    void getAllUsers_asAdmin_returns200_andList() throws Exception {
        MvcResult result = mvc.perform(get("/api/users")
                        .session(adminSession)
                        .secure(true)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        List<User> users = mapper.readValue(json, new TypeReference<>(){});
        assertThat(users).isNotEmpty();
    }

    @Test
    void getAllUsers_asAnonymous_returns401() throws Exception {
        mvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }
}
