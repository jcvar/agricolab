package agricolab.api;

import agricolab.model.Order;
import agricolab.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/api/v1/order")
public class OrderAPI {

    private final OrderService orderService;

    @Autowired
    public OrderAPI(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public void postOrder(@RequestBody Order r) {
        orderService.addOrder(r);
    }

    @GetMapping("del/{id}")
    public void deleteOrder(@PathVariable String id ) { orderService.deleteOrder(id);    }

    // GET METHODS
    @GetMapping("/{id}")
    public Order getOrder(@PathVariable String id) {
        return orderService.getOrder(id);
    }
    @PutMapping
    public boolean updateOrder(@RequestBody Order r){
        return orderService.updateOrder(r);
    }
    @GetMapping
    public ArrayList<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/user/{email}")
    public ArrayList<Order> getOrdersByUser(@PathVariable String email) {
        return orderService.getUserOrders(email);
    }

    @GetMapping("/offer/{offerId}")
    public ArrayList<Order> getOfferOrders(@PathVariable String offerId) {
        return orderService.getOfferOrders(offerId);
    }

    @GetMapping("/seller/{email}")
    public ArrayList<Order> getOrderBySeller(@PathVariable String email){
        return orderService.getOrderBySeller(email);
    }

}
