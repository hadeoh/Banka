package com.usmanadio.banka.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    public static String asJsonString(final Object object) {
//        try {
//            return new ObjectMapper().writeValueAsString(object);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public String loginUser(Integer v) throws Exception {
//        User user = new User();
//        user.setFullName("Phillip Nnamani");
//        user.setEmail(v +"phillip@gmail.com");
//        user.setPassword(bCryptPasswordEncoder.encode("1234567"));
//        user.setPhoneNumber("090827678372");
//        user.setEmailVerificationToken("token");
//        user.setEmailVerificationStatus(EmailVerificationStatus.VERIFIED);
//        userRepository.save(user);
//
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail(user.getEmail());
//        loginRequest.setPassword("1234567");
//        MvcResult result = mockMvc.perform(post("/auth/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(loginRequest))).andDo(print()).andReturn();
//        return result.getResponse().getHeader("token");
//    }
//
//    public String createAccount() {
//        Account account = new Account();
//        account.setAccountType(AccountType.SAVINGS);
//        account = accountRepository.save(account);
//        return account.getAccountNumber();
//    }
//
//    @Test
//    public void createAccount_status201_created() throws Exception {
//        String loginResult = loginUser(6);
//        System.out.println(loginResult);
//        Account account = new Account();
//        account.setAccountType(AccountType.CURRENT);
//        mockMvc.perform(post("/accounts")
//                .header("Authorization", "Bearer " + loginResult)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(account))).andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(content().string(containsString("You have successfully created a CURRENT account")));
//    }
//
//    @Test
//    public void createDomiciliaryAccount_status201_created() throws Exception {
//        String loginResult = loginUser(7);
//        AccountRequest accountRequest = new AccountRequest();
//        accountRequest.setAccountType(AccountType.DOMICILIARY);
//        accountRequest.setDomiciliaryAccountType(DomiciliaryAccountType.DOLLAR);
//        mockMvc.perform(post("/accounts")
//                .header("Authorization", "Bearer " + loginResult)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(accountRequest))).andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(content().string(containsString("You have successfully created a DOMICILIARY account")));
//    }
//
//    @Test
//    public void set_account_status_202_accepted() throws Exception {
//        String loginResult = loginUser(11);
//        String accountNumber = createAccount();
//        AccountStatusRequest accountStatusRequest = new AccountStatusRequest();
//        accountStatusRequest.setAccountStatus(AccountStatus.INACTIVE);
//        mockMvc.perform(patch("/accounts/"+accountNumber)
//                .header("Authorization", "Bearer " + loginResult)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(accountStatusRequest))).andDo(print())
//                .andExpect(status().isAccepted())
//                .andExpect(content().string(containsString("Account deactivated successfully")));
//    }
    @Test
    public void printYes() {
        System.out.println("yes");
        System.out.println("I got tested");
    }
}
