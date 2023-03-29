package htl.steyr.mygrover.controller;

import htl.steyr.mygrover.MyGroverApplication;
import htl.steyr.mygrover.model.Brand;
import htl.steyr.mygrover.model.Model;
import htl.steyr.mygrover.repository.BrandRepository;
import htl.steyr.mygrover.repository.ModelRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {MyGroverApplication.class})
class ModelRestControllerTest extends AbstractRestTest {

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    ModelRepository modelRepository;

    @Override
    @Test
    @BeforeAll
    public void setUp(){
        super.setUp();
        super.setUpBearerToken();

        if(brandRepository.findById(1L).isEmpty()){
            Brand tempBrand = new Brand();
            tempBrand.setName("testBrand");

            brandRepository.save(tempBrand);
        }
    }

    @Test
    @Order(1)
    public void testCreateModel() throws Exception {
        mvc.perform(post(baseUri + "/model/create")
                        .content("{\"name\": \"TestModel\", \"price\": 6.9, \"brand\": " + brandRepository.findFirstByOrderByIdDesc().getId() + "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .header("authorization", bearerToken))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void testUpdateModel() throws Exception {
        Model newestModel = modelRepository.findFirstByOrderByIdDesc();

        mvc.perform(post(baseUri + "/model/update")
                        .content("{\"id\": " + newestModel.getId() + ", \"name\": \"testModel2\", \"price\": 10.0, \"brand\": 1}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .header("authorization", bearerToken))
                .andExpect(status().isOk());

        assertEquals("testModel2", modelRepository.findFirstByOrderByIdDesc().getName());
    }

    @Test
    @Order(3)
    public void testLoadModel() throws Exception {
        Model newestModel = modelRepository.findFirstByOrderByIdDesc();

        MvcResult resultLoadAll = mvc.perform(get(baseUri + "/model/load")
                        .header("authorization", bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        if(!resultLoadAll.getResponse().getContentAsString().contains(
                "{\"id\":" + newestModel.getId() + ",\"name\":\"testModel2\",\"price\":10.0}"
        )){
            Assertions.fail();
        }

        MvcResult resultLoadOne = mvc.perform(get(baseUri + "/model/load/" + newestModel.getId())
                        .header("authorization", bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        if(!resultLoadOne.getResponse().getContentAsString().equals(
                "[{\"id\":" + newestModel.getId() + ",\"name\":\"testModel2\",\"price\":10.0}]"
        )){
            Assertions.fail();
        }
    }

    @Test
    @Order(4)
    public void testLoadModelForBrand() throws Exception {
        Model newestModel = modelRepository.findFirstByOrderByIdDesc();

        MvcResult result = mvc.perform(get(baseUri + "/model/brand/1")
                        .header("authorization", bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        if(!result.getResponse().getContentAsString().contains(
                "{\"id\":" + newestModel.getId() + ",\"name\":\"testModel2\",\"price\":10.0}"
        )){
            Assertions.fail();
        }
    }

    @Test
    @Order(5)
    public void testDeleteModel() throws Exception {
        Model newestModel = modelRepository.findFirstByOrderByIdDesc();

        mvc.perform(delete(baseUri + "/model/delete/" + newestModel.getId())
                        .header("authorization", bearerToken))
                .andExpect(status().isOk());

        // If findFirstByOrderByIdDesc is null then delete was also successful
        if(modelRepository.findFirstByOrderByIdDesc() != null){
            assertNotEquals(newestModel.getId(), modelRepository.findFirstByOrderByIdDesc().getId());
        }
    }
}
