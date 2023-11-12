package ma.sdia.comptcqrses.commonapi.events;

import lombok.Getter;
import ma.sdia.comptcqrses.commonapi.enums.AccountStatus;

public class AccountDebitedEvent extends BaseEvent<String> {
    @Getter private String currency;
    @Getter private double amount;
    public AccountDebitedEvent(String id, String currency, double amount) {
        super(id);
        this.currency = currency;
        this.amount = amount;
    }
}
