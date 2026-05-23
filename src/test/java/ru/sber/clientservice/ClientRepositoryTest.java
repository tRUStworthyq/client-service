package ru.sber.clientservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.sber.clientservice.entity.Client;
import ru.sber.clientservice.repository.ClientRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

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
    void findByDealId_shouldFindClientByDealId() {
        Optional<Client> found = clientRepository.findByDealId("CRD-111");

        assertThat(found).isPresent();
        assertThat(found.get().getFullName()).isEqualTo("Иванов Иван Иванович");
    }
}