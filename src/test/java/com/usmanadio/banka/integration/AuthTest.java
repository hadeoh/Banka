package com.usmanadio.banka.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usmanadio.banka.models.user.User;
import com.usmanadio.banka.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    public static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
                .andExpect(content().string(containsString("must not be blank")));
    }

    @Test
    public void create_user_with_existing_email_phone_not_ok() throws Exception {
        User user1 = new User();
        user1.setEmail("phillip@gmail.com");
        user1.setPhoneNumber("090827678372");

        when(userRepository.existsByEmailOrPhoneNumber(user1.getEmail(), user1.getPhoneNumber())).thenReturn(true);
        User user = new User();
        user.setFullName("Phillip Nnamani");
        user.setEmail("phillip@gmail.com");
        user.setPassword("090827678372");
        user.setPhoneNumber("090827678372");
        mockMvc.perform(post("/auth/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user))).andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Email or phone number already exists")));
    }

    @Test
    public void verify_user_already_registered() throws Exception {
        User user1 = new User();
        user1.setEmail("phillip@gmail.com");
        user1.setPhoneNumber("090827678372");
        user1.setEmailVerificationToken("738bgi");

        when(userRepository.findByEmailVerificationToken(user1.getEmailVerificationToken())).thenReturn(user1);
        mockMvc.perform(patch("/auth/verifyEmail/"+ user1.getEmailVerificationToken()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().string(containsString("You are now a verified user of Banka")));
    }

    @Test
    public void verify_user_already_registered_with_wrong_token() throws Exception {
        User user1 = new User();
        user1.setEmail("phillip@gmail.com");
        user1.setPhoneNumber("090827678372");
        user1.setEmailVerificationToken("738bgi");

        when(userRepository.findByEmailVerificationToken(user1.getEmailVerificationToken())).thenReturn(user1);
        mockMvc.perform(patch("/auth/verifyEmail/9433"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to verify that you registered here")));
    }

//    @Test
//    public void login_a_registered_and_verified_user() throws Exception {
//        User user1 = new User();
//        user1.setEmail("phillip@gmail.com");
//        user1.setPhoneNumber("090827678372");
//        user1.setEmailVerificationToken("738bgi");
//        user1.setPassword("1234567");
//        user1.setEmailVerificationStatus(EmailVerificationStatus.VERIFIED);
//
//        when(userRepository.findByEmail(user1.getEmail())).thenReturn(user1);
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail(user1.getEmail());
//        loginRequest.setPassword(user1.getPassword());
//        mockMvc.perform(post("/auth/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(loginRequest))).andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString("User successfully logged in")));
//    }
}
