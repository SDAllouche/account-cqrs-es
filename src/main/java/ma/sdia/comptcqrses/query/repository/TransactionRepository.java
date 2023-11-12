package ma.sdia.comptcqrses.query.repository;

import ma.sdia.comptcqrses.query.entities.Account;
import ma.sdia.comptcqrses.query.entities.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<AccountTransaction,Long> {
    AccountTransaction findTop1ByAccountIdOrderByTimestampDesc(String accountId);
}
