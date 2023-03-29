package htl.steyr.mygrover.controller;

import htl.steyr.mygrover.MyGroverApplication;
import htl.steyr.mygrover.model.Customer;
import htl.steyr.mygrover.repository.CustomerRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {MyGroverApplication.class})
class CustomerRestControllerTest extends AbstractRestTest {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    @BeforeAll
    public void setUp(){
        super.setUp();
        super.setUpBearerToken();
    }

    @Test
    @Order(1)
    public void testCreateCustomer() throws Exception {
        mvc.perform(post(baseUri + "/customer/create")
                        .content("{\"name\":\"TestCustomer\", \"email\": \"testEmail\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .header("authorization", bearerToken))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void testUpdateCustomer() throws Exception {
        Customer newestCustomer = customerRepository.findFirstByOrderByIdDesc();

        mvc.perform(post(baseUri + "/customer/update")
                        .content("{\"id\": " + newestCustomer.getId() + ", \"name\": \"testCustomer2\", \"email\": \"testEmail2\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .header("authorization", bearerToken))
                .andExpect(status().isOk());

        assertEquals("testCustomer2", customerRepository.findFirstByOrderByIdDesc().getName());
    }

    @Test
    @Order(3)
    public void testLoadCustomer() throws Exception {
        Customer newestCustomer = customerRepository.findFirstByOrderByIdDesc();

        MvcResult resultLoadAll = mvc.perform(get(baseUri + "/customer/load")
                        .header("authorization", bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        if(!resultLoadAll.getResponse().getContentAsString().contains(
                "{\"id\":" + newestCustomer.getId() + ",\"name\":\"testCustomer2\",\"email\":\"testEmail2\"}"
        )){
            Assertions.fail();
        }

        MvcResult resultLoadOne = mvc.perform(get(baseUri + "/customer/load/" + newestCustomer.getId())
                        .header("authorization", bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        if(!resultLoadOne.getResponse().getContentAsString().equals(
                "[{\"id\":" + newestCustomer.getId() + ",\"name\":\"testCustomer2\",\"email\":\"testEmail2\"}]"
        )){
            Assertions.fail();
        }
    }

    @Test
    @Order(4)
    public void testDeleteCustomer() throws Exception {
        Customer newestCustomer = customerRepository.findFirstByOrderByIdDesc();

        mvc.perform(delete(baseUri + "/customer/delete/" + newestCustomer.getId())
                        .header("authorization", bearerToken))
                .andExpect(status().isOk());

        // If findFirstByOrderByIdDesc is null then delete was also successful
        if(customerRepository.findFirstByOrderByIdDesc() != null){
            assertNotEquals(newestCustomer.getId(), customerRepository.findFirstByOrderByIdDesc().getId());
        }
    }
}
