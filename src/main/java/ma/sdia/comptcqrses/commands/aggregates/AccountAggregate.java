package ma.sdia.comptcqrses.commands.aggregates;

import lombok.extern.slf4j.Slf4j;
import ma.sdia.comptcqrses.commonapi.events.AccountActivatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import ma.sdia.comptcqrses.commonapi.commands.CreateAccountCommand;
import ma.sdia.comptcqrses.commonapi.commands.CreditAccountCommand;
import ma.sdia.comptcqrses.commonapi.commands.DebitAccountCommand;
import ma.sdia.comptcqrses.commonapi.enums.AccountStatus;
import ma.sdia.comptcqrses.commonapi.events.AccountCreatedEvent;
import ma.sdia.comptcqrses.commonapi.events.AccountCreditedEvent;
import ma.sdia.comptcqrses.commonapi.events.AccountDebitedEvent;
import ma.sdia.comptcqrses.commonapi.execptions.NegativeInitialBalanceException;

@Aggregate
@Slf4j
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private String currency;
    private double balance;
    private AccountStatus status;

    public AccountAggregate(){
        // required by AXON
    }
    @CommandHandler
    public AccountAggregate(CreateAccountCommand command){
        log.info("CreateAccountCommand received");
        if(command.getInitialBalance()<0) throw new NegativeInitialBalanceException("Negative balance");
        AggregateLifecycle.apply(new AccountCreatedEvent(
                command.getId(),
                command.getCurrency(),
                command.getInitialBalance(),
                AccountStatus.CREATED
        ));
    }
    @EventSourcingHandler
    public void on(AccountCreatedEvent event){
        log.info("AccountCreatedEvent sourced");
        this.accountId=event.getId();
        this.balance=event.getBalance();
        this.status=event.getStatus();
        this.currency=event.getCurrency();
        AggregateLifecycle.apply(new AccountActivatedEvent(
                event.getId(),
                AccountStatus.ACTIVATED
        ));
    }

    @EventSourcingHandler
    public void on (AccountActivatedEvent event){
        this.status=event.getStatus();
    }
    @CommandHandler
    public void handle(CreditAccountCommand command){
        log.info("CreditAccountCommand received");
        if(command.getAmount()<0) throw new NegativeInitialBalanceException("Negative Amount");
        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getId(),
                command.getCurrency(),
                command.getAmount()
        ));
    }
    @EventSourcingHandler
    public void on(AccountCreditedEvent event){
        log.info("AccountCreditedEvent sourced");
        this.balance+=event.getAmount();
    }
    @CommandHandler
    public void handle(DebitAccountCommand command){
        log.info("DebitAccountCommand received");
        if(command.getAmount()<0) throw new NegativeInitialBalanceException("Negative Amount");
        if(command.getAmount()>this.balance) throw new RuntimeException("Balance insufficient Exception");
        AggregateLifecycle.apply(new AccountDebitedEvent(
                command.getId(),
                command.getCurrency(),
                command.getAmount()
        ));
    }
    @EventSourcingHandler
    public void on(AccountDebitedEvent event){
        log.info("AccountDebitedEvent sourced");
        this.balance-=event.getAmount();
    }

}
