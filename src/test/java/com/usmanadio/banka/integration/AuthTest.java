package com.usmanadio.banka.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usmanadio.banka.dto.auth.LoginRequest;
import com.usmanadio.banka.models.user.EmailVerificationStatus;
import com.usmanadio.banka.models.user.User;
import com.usmanadio.banka.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public User createUser(Integer v, String token) throws Exception {
        User user = new User();
        user.setFullName("Phillip Nnamani");
        user.setEmail(v +"phillip@gmail.com");
        user.setPassword(bCryptPasswordEncoder.encode("1234567"));
        user.setPhoneNumber("090827678372");
        user.setEmailVerificationToken(token);
        user.setEmailVerificationStatus(EmailVerificationStatus.VERIFIED);
        return userRepository.save(user);
    }

    @Test
    public void create_user_with_correct_values() throws Exception {
        User user = new User();
        user.setEmail("usman@yahoo.com");
        user.setFullName("Adio Olawale");
        user.setPhoneNumber("09034567896");
        user.setPassword("aminat");
        mockMvc.perform(post("/auth/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user))).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("You have successfully signed up on Banka")));
    }

    @Test
    public void create_user_with_incorrect_values_bad_request() throws Exception {
        User user = new User();
        user.setEmail("usman@yahoo.com");
        user.setFullName("Adio Olawale");
        user.setPassword("aminat");
        mockMvc.perform(post("/auth/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Validation Error")));
    }

    @Test
    public void create_user_with_existing_email_phone_not_ok() throws Exception {
        User user = createUser(1, null);
        mockMvc.perform(post("/auth/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user))).andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Email or phone number already exists")));
    }

    @Test
    public void verify_user_already_registered() throws Exception {
        createUser(2, "738bgi");
        mockMvc.perform(patch("/auth/verifyEmail/738bgi"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().string(containsString("You are now a verified user of Banka")));
    }

    @Test
    public void verify_user_already_registered_with_wrong_token() throws Exception {
        mockMvc.perform(patch("/auth/verifyEmail/9433"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to verify that you registered here")));
    }

    @Test
    public void login_a_registered_and_verified_user() throws Exception {
        User user = createUser(3, null);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(user.getEmail());
        loginRequest.setPassword("1234567");
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginRequest))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User successfully logged in")));
    }
}
//./mvnw test -Dspring.profiles.active=test

//./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
