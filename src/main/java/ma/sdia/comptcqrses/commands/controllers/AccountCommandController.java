package ma.sdia.comptcqrses.commands.controllers;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import ma.sdia.comptcqrses.commonapi.commands.CreateAccountCommand;
import ma.sdia.comptcqrses.commonapi.commands.CreditAccountCommand;
import ma.sdia.comptcqrses.commonapi.commands.DebitAccountCommand;
import ma.sdia.comptcqrses.commonapi.dtos.CreateAccountRequestDTO;
import ma.sdia.comptcqrses.commonapi.dtos.CreditAccountRequestDTO;
import ma.sdia.comptcqrses.commonapi.dtos.DebitAccountRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping("/commands/account")
public class AccountCommandController {

    private CommandGateway commandGateway;
    private EventStore eventStore;

    public AccountCommandController(CommandGateway commandGateway, EventStore eventStore) {
        this.commandGateway = commandGateway;
        this.eventStore = eventStore;
    }

    @PostMapping("/create")
    public CompletableFuture<String> createNewAccount(@RequestBody CreateAccountRequestDTO request){
         return commandGateway.send(new CreateAccountCommand(
                UUID.randomUUID().toString(),
                request.getCurrency(),
                request.getInitialBalance()
        ));
    }

    @PostMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountRequestDTO request){
        return commandGateway.send(new DebitAccountCommand(
                request.getAccountId(),
                request.getCurrency(),
                request.getAmount()
        ));
    }
    @PostMapping("/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountRequestDTO request){
        return commandGateway.send(new CreditAccountCommand(
                request.getAccountId(),
                request.getCurrency(),
                request.getAmount()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exeptionHandler(Exception exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @GetMapping("/eventStore/{id}")
    public Stream eventStore(@PathVariable String id){
        return eventStore.readEvents(id).asStream();
    }
}
