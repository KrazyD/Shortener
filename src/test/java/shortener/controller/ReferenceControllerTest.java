package shortener.controller;

import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import shortener.config.AppConfig;
import shortener.config.WebMvcConfig;

import javax.servlet.ServletContext;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, WebMvcConfig.class})
@WebAppConfiguration
public class ReferenceControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void wacConfiguredProperly() {
        ServletContext servletContext = wac.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(wac.getBean("referenceController"));
    }

    @Test
    public void getAccount() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk());
//                .andExpect(content().contentType("application/json"));
//                .andExpect(jsonPath("$.name").value("Lee"));
    }

    @Test
    public void useShortRef() {
    }

    @Test
    public void createReferences() {
    }

    @Test
    public void getReferences() {
    }

    @Test
    public void updateReferences() {
    }

    @Test
    public void removeReferences() {
    }
}