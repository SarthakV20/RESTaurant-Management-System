package com.example.restaurant;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
@RestController @RequestMapping("/api/recipes") @CrossOrigin(origins="http://localhost:5173") class RecipeController {
 private final RecipeService service;RecipeController(RecipeService s){service=s;}
 @GetMapping List<Model.Recipe> list(){return service.list();}
 @PostMapping("/upload") List<Model.Recipe> upload(@RequestParam("file") MultipartFile file){return service.upload(file);}
}
