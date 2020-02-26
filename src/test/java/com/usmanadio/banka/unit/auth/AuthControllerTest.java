package com.usmanadio.banka.unit.auth;

import com.usmanadio.banka.controllers.AuthController;
import com.usmanadio.banka.dto.auth.SignUpRequest;
import com.usmanadio.banka.repositories.UserRepository;
import com.usmanadio.banka.responses.Response;
import com.usmanadio.banka.services.auth.AuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class AuthControllerTest {


    @Mock
    private AuthService authService;

    @Mock
    private AuthController authController;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void it_should_return_response_created() {
        SignUpRequest newTestUser = new SignUpRequest("Usman", "usman@gmail.com", "usmansj",
                "9876578");
        Response<String> result = new Response<>(HttpStatus.CREATED);
        result.setData(null);
        result.setMessage("You have successfully signed up on Banka");
        ResponseEntity<Response<String>> response = new ResponseEntity<>(result, HttpStatus.CREATED);
        when(authController.createUser(newTestUser)).thenReturn(response);
    }

    @Test
    public void it_should_verify_and_return_response_accepted() {
        SignUpRequest newTestUser = new SignUpRequest("Usman", "usman@gmail.com", "usmansj",
                "9876578");
        Response<String> result = new Response<>(HttpStatus.BAD_REQUEST);
        result.setData(null);
        ResponseEntity<Response<String>> response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        when(authController.verifyUser("newTestUser")).thenReturn(response);
    }
}
