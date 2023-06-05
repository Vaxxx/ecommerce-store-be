package ng.com.createsoftware.ecommercestorebe.service;

import ng.com.createsoftware.ecommercestorebe.model.Product;
import ng.com.createsoftware.ecommercestorebe.model.repository.ProductDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> getProducts(){
        return productDAO.findAll();
    }
}
