package com.example.restaurant;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
@RestController @RequestMapping("/api/ingredients") @CrossOrigin(origins="http://localhost:5173") class InventoryController {
 private final InventoryService service;InventoryController(InventoryService s){service=s;}
 record Stock(int quantity){}
 @GetMapping List<Model.Ingredient> list(){return service.list();}
 @PostMapping("/upload") List<Model.Ingredient> upload(@RequestParam("file") MultipartFile file){return service.upload(file);}
 @PostMapping("/{name}/stock") Model.Ingredient stock(@PathVariable String name,@RequestBody Stock body){return service.addStock(name,body.quantity());}
}
