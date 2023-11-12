package ma.sdia.comptcqrses.query.repository;

import ma.sdia.comptcqrses.query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {
}
