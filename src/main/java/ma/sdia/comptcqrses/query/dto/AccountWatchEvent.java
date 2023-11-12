package ma.sdia.comptcqrses.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.sdia.comptcqrses.commonapi.enums.TransactionType;

import java.time.Instant;
@Data @NoArgsConstructor @AllArgsConstructor
public class AccountWatchEvent{
    private Instant instant;
    private String accountId;
    private double currentBalance;
    private TransactionType type;
    private double transactionAmount;
}
