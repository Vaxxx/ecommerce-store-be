package ng.com.createsoftware.ecommercestorebe.model.repository;

import ng.com.createsoftware.ecommercestorebe.model.LocalUser;
import ng.com.createsoftware.ecommercestorebe.model.WebOrder;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface WebOrderDAO extends ListCrudRepository<WebOrder, Long> {
    List<WebOrder> findByUser(LocalUser user);
}
