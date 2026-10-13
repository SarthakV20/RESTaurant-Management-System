package com.example.restaurant;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
@RestController @RequestMapping("/api/employees") @CrossOrigin(origins="http://localhost:5173") class EmployeeController {
 private final EmployeeService service;EmployeeController(EmployeeService s){service=s;}
 @GetMapping List<Model.Employee> list(){return service.list();}
 @PostMapping("/upload") List<Model.Employee> upload(@RequestParam("file") MultipartFile file){return service.upload(file);}
 @PostMapping("/{id}/hire") Model.Employee hire(@PathVariable Long id){return service.setEmployed(id,true);}
 @PostMapping("/{id}/dismiss") Model.Employee dismiss(@PathVariable Long id){return service.setEmployed(id,false);}
}
