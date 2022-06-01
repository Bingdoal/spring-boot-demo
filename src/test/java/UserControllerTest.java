import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import springboot.demo.controller.UserController;
import springboot.demo.middleware.exception.GlobalExceptionHandler;
import springboot.demo.middleware.exception.RestException;
import springboot.demo.model.dao.UserDao;
import springboot.demo.model.entity.User;
import springboot.demo.service.UserDaoService;

import java.util.Locale;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {UserController.class})
public class UserControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private UserController userController;
    @MockBean
    private UserDaoService userDaoService;
    @MockBean
    private UserDao userDao;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        User user;
        user = new User();
        user.setId(1L);
        user.setName("Tony");
        user.setEmail("Tony@stark.org");
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(user));
    }

    @Test
    public void testGetOneUser() throws Exception {
        ResultActions positiveCase = mockMvc.perform(get("/v1/user/1").contentType(MediaType.APPLICATION_JSON));
        positiveCase.andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Tony"));
    }

    @Test
    public void testGetUserA() throws Exception {
        ResultActions negativeCase = mockMvc.perform(get("/v1/user/A").contentType(MediaType.APPLICATION_JSON));
        negativeCase.andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof MethodArgumentTypeMismatchException));
    }

    @Test
    public void testGetUser2() throws Exception {
        ResultActions notFoundCase = mockMvc.perform(get("/v1/user/2")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.TAIWAN));
        notFoundCase.andDo(print());
        notFoundCase.andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof RestException));
    }
}
