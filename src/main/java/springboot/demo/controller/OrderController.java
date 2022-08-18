package springboot.demo.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springboot.demo.dto.OrderDto;
import springboot.demo.dto.enums.OrderStatus;
import springboot.demo.model.dao.OrderDao;
import springboot.demo.model.entity.Order;

@Slf4j
@RestController()
@RequestMapping("/v1/order")
public class OrderController {

  @Autowired
  private OrderDao orderDao;
  @Autowired
  private ObjectMapper mapper;

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public void createOrder(@RequestBody @Valid OrderDto orderDto) {
    Order order = new Order();
    order.setUserId(orderDto.getUserId());
    order.setItem(orderDto.getItem());
    order.setStatus(OrderStatus.SUCCESS);
    orderDao.save(order);
  }

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public List<Order> getOrder() {
    log.info("{}", mapper.valueToTree(orderDao.findAll()).toPrettyString());
    return orderDao.findAll();
  }
}
