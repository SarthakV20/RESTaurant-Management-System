package com.example.restaurant;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/orders") @CrossOrigin(origins="http://localhost:5173") class OrderController {
 private final OrderService service;OrderController(OrderService s){service=s;}
 record NewOrder(List<String> recipes){}
 record Rating(int stars){}
 @GetMapping List<Model.FoodOrder> list(){return service.list();}
 @PostMapping Model.FoodOrder create(@RequestBody NewOrder body){return service.create(body.recipes());}
 @PostMapping("/{id}/complete") Model.FoodOrder complete(@PathVariable Long id){return service.complete(id);}
 @PostMapping("/{id}/rate") Model.FoodOrder rate(@PathVariable Long id,@RequestBody Rating body){return service.rate(id,body.stars());}
}
