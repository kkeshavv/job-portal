package com.capg.userservice.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import({SecurityConfig.class, PasswordConfig.class})
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void anyRequest_isPermitted() throws Exception {
        mockMvc.perform(get("/any-endpoint"))
               .andExpect(status().isNotFound()); // 404 not 401 = security permits all
    }
}
