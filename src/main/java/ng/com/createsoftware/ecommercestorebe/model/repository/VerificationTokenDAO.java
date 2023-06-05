package ng.com.createsoftware.ecommercestorebe.model.repository;

import ng.com.createsoftware.ecommercestorebe.model.LocalUser;
import ng.com.createsoftware.ecommercestorebe.model.VerificationToken;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface VerificationTokenDAO extends ListCrudRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    void deleteByUser(LocalUser user);
}
