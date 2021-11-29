import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import springboot.test.controller.UserController;
import springboot.test.model.dao.UserDao;
import springboot.test.model.entity.User;
import springboot.test.service.UserDaoService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UserController.class)
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
                .build();

        user = new User();
        user.setId(1L);
        user.setName("Tony");
        user.setEmail("Tony@stark.org");
    }

    @Test
    public void testGetOneUser() throws Exception {
        when(userDao.findById(1L)).thenReturn(Optional.of(user));
        ResultActions positiveCase = mockMvc.perform(get("/v1/user/1").contentType(MediaType.APPLICATION_JSON));
        positiveCase.andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Tony"));

        ResultActions negativeCase = mockMvc.perform(get("/v1/user/a").contentType(MediaType.APPLICATION_JSON));
        negativeCase.andExpect(status().isBadRequest())
                .andExpect(result -> Assert.assertTrue(
                        result.getResolvedException() instanceof MethodArgumentTypeMismatchException));
    }
}
