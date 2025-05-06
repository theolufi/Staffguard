package com.project.staffguard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.staffguard.config.DataInitializer;
import com.project.staffguard.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        properties = {
                "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
                "spring.datasource.driver-class-name=org.h2.Driver",
                "spring.datasource.username=sa",
                "spring.datasource.password=",
                "spring.jpa.hibernate.ddl-auto=create-drop"
        }
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthIntegrationTest {

    /**
     * Disable the real DataInitializer (which seeds roles and is failing under H2)
     * by supplying a no-op mock in a nested TestConfiguration.
     */
    @TestConfiguration
    static class NoOpDataInitializerConfig {
        @Bean
        public DataInitializer dataInitializer() {
            return Mockito.mock(DataInitializer.class);
        }
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private MockHttpSession session;

    @BeforeEach
    void loginAsAdmin() throws Exception {
        // Manually register the admin user since our DataInitializer is now a no-op
        UserDTO admin = new UserDTO();
        admin.setUsername("admin");
        admin.setPassword("adminpass");
        admin.setRoles(java.util.Set.of("ADMIN"));

        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(admin))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(r -> { r.setSecure(true); return r; }))
                .andExpect(status().isOk());

        // Now log in
        UserDTO credentials = new UserDTO();
        credentials.setUsername("admin");
        credentials.setPassword("adminpass");

        MvcResult login = mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(credentials))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(r -> { r.setSecure(true); return r; }))
                .andExpect(status().isOk())
                .andReturn();

        session = (MockHttpSession) login.getRequest().getSession(false);
        assertThat(session).isNotNull();
    }

    @Test
    void meReturnsUser_whenAuthenticated() throws Exception {
        mvc.perform(get("/api/auth/me")
                        .session(session)
                        .with(r -> { r.setSecure(true); return r; }))
                .andExpect(status().isOk());
    }

    @Test
    void meReturns401_whenNotAuthenticated() throws Exception {
        mvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized());
    }
}
