package htl.steyr.mygrover.controller;

import htl.steyr.mygrover.MyGroverApplication;
import htl.steyr.mygrover.model.User;
import htl.steyr.mygrover.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {MyGroverApplication.class})
class LoginRestControllerTest extends AbstractRestTest {

    @Autowired
    UserRepository userRepository;

    @Override
    @BeforeEach
    public void setUp(){
        super.setUp();

        userRepository.save(new User("test@test.com", "test"));
    }

    @Test
    @Order(1)
    public void testLogin() throws Exception {
        mvc.perform(post(baseUri + "/login")
                        .content("{\"email\":\"test@test.com\", \"password\": \"test\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }
}
