package ng.com.createsoftware.ecommercestorebe.model.repository;

import ng.com.createsoftware.ecommercestorebe.model.Product;
import org.springframework.data.repository.ListCrudRepository;

public interface ProductDAO extends ListCrudRepository<Product, Long> {

}
