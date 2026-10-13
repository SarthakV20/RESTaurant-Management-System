package com.example.restaurant;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/restaurant") @CrossOrigin(origins="http://localhost:5173") class RestaurantController {
 private final RestaurantService service;RestaurantController(RestaurantService s){service=s;}
 @GetMapping Model.State get(){return service.get();}
 @PostMapping("/open") Model.State open(){return service.open();}
 @PostMapping("/close") Model.State close(){return service.close();}
}
