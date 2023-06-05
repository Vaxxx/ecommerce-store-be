package ng.com.createsoftware.ecommercestorebe.service;

import ng.com.createsoftware.ecommercestorebe.model.LocalUser;
import ng.com.createsoftware.ecommercestorebe.model.WebOrder;
import ng.com.createsoftware.ecommercestorebe.model.repository.WebOrderDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private WebOrderDAO webOrderDAO;

    public OrderService(WebOrderDAO webOrderDAO) {
        this.webOrderDAO = webOrderDAO;
    }

    public List<WebOrder> getOrders(LocalUser user ){
        return webOrderDAO.findByUser(user);
    }
}
