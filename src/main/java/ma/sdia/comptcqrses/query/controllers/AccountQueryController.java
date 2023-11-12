package ma.sdia.comptcqrses.query.controllers;

import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import ma.sdia.comptcqrses.query.dto.AccountWatchEvent;
import ma.sdia.comptcqrses.query.entities.Account;
import ma.sdia.comptcqrses.query.entities.AccountTransaction;
import ma.sdia.comptcqrses.query.queries.GetAccountBalanceStream;
import ma.sdia.comptcqrses.query.queries.GetAccountById;
import ma.sdia.comptcqrses.query.queries.GetAllAccounts;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/query/account")
public class AccountQueryController {
    private QueryGateway queryGateway;

    public AccountQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/list")
    public CompletableFuture<List<Account>> accountList(){
        return queryGateway.query(new GetAllAccounts(), ResponseTypes.multipleInstancesOf(Account.class));
    }
    @GetMapping("/byId/{id}")
    public CompletableFuture<Account> accountById(@PathVariable String id){
        return queryGateway.query(new GetAccountById(id), ResponseTypes.instanceOf(Account.class));
    }
    @GetMapping(value = "/{accountId}/watch",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AccountWatchEvent> accountBalance(@PathVariable String accountId){
        SubscriptionQueryResult<AccountWatchEvent, AccountWatchEvent> result = queryGateway.subscriptionQuery(new GetAccountBalanceStream(accountId),
                ResponseTypes.instanceOf(AccountWatchEvent.class), ResponseTypes.instanceOf(AccountWatchEvent.class));
        return result.initialResult().concatWith(result.updates());
    }
}
