package ru.sber.clientservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sber.clientservice.entity.Client;
import ru.sber.clientservice.entity.ClientDeals;
import ru.sber.clientservice.exception.ClientNotFoundException;
import ru.sber.clientservice.repository.ClientRepository;
import ru.sber.clientservice.service.impl.DefaultClientService;
import ru.sber.dto.ClientResponseDto;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private DefaultClientService clientService;

    private Client testClient;

    @BeforeEach
    void setUp() {
        testClient = Client.builder()
                .id("CLT-00123")
                .fullName("Иванов Иван Иванович")
                .inn("123456789012")
                .build();

        testClient.setDeals(List.of(new ClientDeals("CRD-111", testClient)));
    }

    @Test
    void getClientByDealId_ShouldReturnDto_WhenClientExists() {
        when(clientRepository.findByDealId("CRD-111")).thenReturn(Optional.of(testClient));

        ClientResponseDto result = clientService.getClientByDealId("CRD-111");

        assertThat(result.dealId()).isEqualTo("CRD-111");
        assertThat(result.fullName()).isEqualTo("Иванов Иван Иванович");
        assertThat(result.inn()).isEqualTo("123456789012");

    }

    @Test
    void getClientByDealId_ShouldThrowException_WhenClientNotFound() {
        when(clientRepository.findByDealId(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.getClientByDealId("TEST"))
                .isInstanceOf(ClientNotFoundException.class)
                .hasMessageContaining("Client not found for dealId: TEST");
    }
}
