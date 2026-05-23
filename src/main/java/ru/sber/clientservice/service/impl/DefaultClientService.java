package ru.sber.clientservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sber.clientservice.entity.Client;
import ru.sber.clientservice.exception.ClientNotFoundException;
import ru.sber.clientservice.repository.ClientRepository;
import ru.sber.clientservice.service.ClientService;
import ru.sber.dto.ClientResponseDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultClientService implements ClientService {

    private final ClientRepository clientRepository;

    @Transactional(readOnly = true)
    @Override
    public ClientResponseDto getClientByDealId(String dealId) {
        log.info("Fetching client info for dealId: {}", dealId);
        Client client = clientRepository.findByDealId(dealId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found for dealId: " + dealId));

        return toDto(client, dealId);
    }

    private ClientResponseDto toDto(Client client, String dealId) {
        return ClientResponseDto.builder()
                .dealId(dealId)
                .fullName(client.getFullName())
                .inn(client.getInn())
                .build();
    }
}
