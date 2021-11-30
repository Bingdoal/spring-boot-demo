import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import springboot.test.controller.UserController;
import springboot.test.middleware.exception.GlobalExceptionHandler;
import springboot.test.middleware.exception.StatusException;
import springboot.test.model.dao.UserDao;
import springboot.test.model.entity.User;
import springboot.test.service.UserDaoService;
import springboot.test.utils.StaticApplicationContext;

import java.util.Locale;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {UserController.class})
@Slf4j
public class UserControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private UserController userController;
    @MockBean
    private UserDaoService userDaoService;
    @MockBean
    private UserDao userDao;

    private User user;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        user = new User();
        user.setId(1L);
        user.setName("Tony");
        user.setEmail("Tony@stark.org");
    }

    @Test
    public void testGetOneUser() throws Exception {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(user));
        ResultActions positiveCase = mockMvc.perform(get("/v1/user/1").contentType(MediaType.APPLICATION_JSON));
        positiveCase.andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Tony"));

        ResultActions negativeCase = mockMvc.perform(get("/v1/user/a").contentType(MediaType.APPLICATION_JSON));
        negativeCase.andExpect(status().isBadRequest())
                .andExpect(result -> Assert.assertTrue(
                        result.getResolvedException() instanceof MethodArgumentTypeMismatchException));

        ResultActions notFoundCase = mockMvc.perform(get("/v1/user/2")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.TAIWAN));
        notFoundCase.andDo(print());
        notFoundCase.andExpect(status().isNotFound())
                .andExpect(result -> Assert.assertTrue(
                        result.getResolvedException() instanceof StatusException));
    }
}
