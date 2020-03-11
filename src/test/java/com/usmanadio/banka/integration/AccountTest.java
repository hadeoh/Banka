package com.usmanadio.banka.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usmanadio.banka.dto.account.AccountRequest;
import com.usmanadio.banka.dto.auth.LoginRequest;
import com.usmanadio.banka.models.account.Account;
import com.usmanadio.banka.models.account.AccountType;
import com.usmanadio.banka.models.account.DomiciliaryAccountType;
import com.usmanadio.banka.models.user.EmailVerificationStatus;
import com.usmanadio.banka.models.user.User;
import com.usmanadio.banka.repositories.AccountRepository;
import com.usmanadio.banka.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

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

    public String loginUser(Integer v) throws Exception {
        User user = new User();
        user.setFullName("Phillip Nnamani");
        user.setEmail(v +"phillip@gmail.com");
        user.setPassword(bCryptPasswordEncoder.encode("1234567"));
        user.setPhoneNumber("090827678372");
        user.setEmailVerificationToken("token");
        user.setEmailVerificationStatus(EmailVerificationStatus.VERIFIED);
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(user.getEmail());
        loginRequest.setPassword("1234567");
        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginRequest))).andDo(print()).andReturn();
        return result.getResponse().getHeader("token");
    }

    @Test
    public void createAccount_status201_created() throws Exception {
        String loginResult = loginUser(6);
        Account account = new Account();
        account.setAccountType(AccountType.CURRENT);
        mockMvc.perform(post("/accounts")
                .header("Authorization", "Bearer " + loginResult)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(account))).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("You have successfully created a CURRENT account")));
    }

    @Test
    public void createDomiciliaryAccount_status201_created() throws Exception {
        String loginResult = loginUser(7);
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountType(AccountType.DOMICILIARY);
        accountRequest.setDomiciliaryAccountType(DomiciliaryAccountType.DOLLAR);
        mockMvc.perform(post("/accounts")
                .header("Authorization", "Bearer " + loginResult)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(accountRequest))).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("You have successfully created a DOMICILIARY account")));
    }
}
