package htl.steyr.mygrover.controller;

import htl.steyr.mygrover.MyGroverApplication;
import htl.steyr.mygrover.model.User;
import htl.steyr.mygrover.repository.UserRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MyGroverApplication.class)
@WebAppConfiguration
public class AbstractRestTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    UserRepository userRepository;

    protected MockMvc mvc;

    protected String baseUri = "http://localhost:8080";

    protected String bearerToken;

    protected void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected void setUpBearerToken() {
        try {
            User user = new User("test@test.com", "test");
            user.setToken("test");
            user.setTokenCreatedAt(new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse("01-01-9999"));

            userRepository.save(user);

            bearerToken = "Bearer test";
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}
