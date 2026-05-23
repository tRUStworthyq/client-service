package ru.sber.clientservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("INSERT INTO vw_clients(id, full_name, inn) VALUES(?,?,?)",
                "CLT-TEST-001", "Иванов Иван Иванович", "123456789012");
        jdbcTemplate.update("INSERT INTO vw_client_deals(deal_id, client_id) VALUES(?,?)",
                "CRD-111", "CLT-TEST-001");
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM vw_client_deals WHERE deal_id = ?", "CRD-111");
        jdbcTemplate.update("DELETE FROM vw_clients WHERE id = ?", "CLT-TEST-001");
    }

    @Test
    void getClientByDealId_ShouldReturnClient() throws Exception {
        mvc.perform(get("/api/clients/CRD-111")
                .header("X-API-Version", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dealId").value("CRD-111"))
                .andExpect(jsonPath("$.fullName").value("Иванов Иван Иванович"))
                .andExpect(jsonPath("$.inn").value("123456789012"));
    }

    @Test
    void getClientByDealId_ShouldReturn404_WhenNotFound() throws Exception {
        mvc.perform(get("/api/clients/TEST")
                .header("X-API-Version", "1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Client not found for dealId: TEST"));
    }

}