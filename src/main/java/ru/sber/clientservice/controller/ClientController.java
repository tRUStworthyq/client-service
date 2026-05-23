package ru.sber.clientservice.controller;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sber.clientservice.service.ClientService;
import ru.sber.dto.ClientResponseDto;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Клиенты", description = "Операции с данными клиентов по кредитным сделкам")
public class ClientController {

    private final ClientService clientService;

    @Operation(
            summary = "Получить данные клиента по номеру сделки",
            description = "Возвращает ФИО и ИНН клиента, связанного с указанным dealId",
            operationId = "getClientByDealId"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Клиент успешно найден",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ClientResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Клиент с указанным dealId не найден",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @GetMapping(value = "/{dealId}", version = "1")
    @Timed(value = "client.get.by.dealId", description = "Время, затраченное на получение клиента по dealId")
    public ResponseEntity<ClientResponseDto> getClientByDealId(
            @Parameter(
                    description = "Уникальный номер кредитной сделки",
                    example = "CRD-2025-00123",
                    required = true
            )
            @PathVariable String dealId
    ) {
        ClientResponseDto response = clientService.getClientByDealId(dealId);
        return ResponseEntity.ok(response);
    }
}
