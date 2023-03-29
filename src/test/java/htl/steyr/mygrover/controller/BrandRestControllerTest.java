package htl.steyr.mygrover.controller;

import htl.steyr.mygrover.MyGroverApplication;
import htl.steyr.mygrover.model.Brand;
import htl.steyr.mygrover.repository.BrandRepository;
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
class BrandRestControllerTest extends AbstractRestTest {

    @Autowired
    BrandRepository brandRepository;

    @Override
    @BeforeAll
    public void setUp(){
        super.setUp();
        super.setUpBearerToken();
    }

    @Test
    @Order(1)
    public void testCreateBrand() throws Exception {
        mvc.perform(post(baseUri + "/brand/create")
                        .content("{\"name\":\"testBrand\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .header("authorization", bearerToken))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void testUpdateBrand() throws Exception {
        Brand newestBrand = brandRepository.findFirstByOrderByIdDesc();

        mvc.perform(post(baseUri + "/brand/update")
                        .content("{\"id\": " + newestBrand.getId() + ", \"name\":\"testBrand2\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .header("authorization", bearerToken))
                .andExpect(status().isOk());

        assertEquals("testBrand2", brandRepository.findFirstByOrderByIdDesc().getName());
    }

    @Test
    @Order(3)
    public void testLoadBrand() throws Exception {
        Brand newestBrand = brandRepository.findFirstByOrderByIdDesc();

        MvcResult resultLoadAll = mvc.perform(get(baseUri + "/brand/load")
                        .header("authorization", bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        if(!resultLoadAll.getResponse().getContentAsString().contains(
                "{\"id\":" + newestBrand.getId() + ",\"name\":\"testBrand2\"}"
        )){
            Assertions.fail();
        }

        MvcResult resultLoadOne = mvc.perform(get(baseUri + "/brand/load/" + newestBrand.getId())
                        .header("authorization", bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        if(!resultLoadOne.getResponse().getContentAsString().equals(
                "[{\"id\":" + newestBrand.getId() + ",\"name\":\"testBrand2\"}]"
        )){
            Assertions.fail();
        }
    }

    @Test
    @Order(4)
    public void testDeleteBrand() throws Exception {
        Brand newestBrand = brandRepository.findFirstByOrderByIdDesc();

        mvc.perform(delete(baseUri + "/brand/delete/" + newestBrand.getId())
                        .header("authorization", bearerToken))
                .andExpect(status().isOk());

        // If findFirstByOrderByIdDesc is null then delete was also successful
        if(brandRepository.findFirstByOrderByIdDesc() != null) {
            assertNotEquals(newestBrand.getId(), brandRepository.findFirstByOrderByIdDesc().getId());
        }
    }
}
