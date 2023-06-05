package ng.com.createsoftware.ecommercestorebe.api.controller.order;

import ng.com.createsoftware.ecommercestorebe.model.LocalUser;
import ng.com.createsoftware.ecommercestorebe.model.WebOrder;
import ng.com.createsoftware.ecommercestorebe.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    //the user is stored in the security context, so use the authentication principal
    //to retrieve it
    @GetMapping
    public List<WebOrder> getOrders(@AuthenticationPrincipal LocalUser user ){
        return orderService.getOrders(user);
    }
}
