package com.lauro.correia.transactionbank.infrastructure.adapters.in.rest.transfer;

import com.lauro.correia.transactionbank.domain.input.port.TransferUseCasePort;
import com.lauro.correia.transactionbank.domain.model.transaction.Transfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
@Slf4j
public class TransferController {

    private final TransferUseCasePort transferUseCasePort;

    @PostMapping
    public ResponseEntity<TransferDTO> send(@RequestBody final TransferDTO transferDTO) {
        final var transaction = transferUseCasePort.execute(Transfer.toDomain(transferDTO));
        return ResponseEntity.ok(TransferDTO.of(transaction));
    }

}
