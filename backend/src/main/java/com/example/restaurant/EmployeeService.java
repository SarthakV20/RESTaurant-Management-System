package com.example.restaurant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
@Service class EmployeeService {
 private final EmployeeRepo repo;private final CsvReader csv;
 EmployeeService(EmployeeRepo repo,CsvReader csv){this.repo=repo;this.csv=csv;}
 List<Model.Employee> list(){return repo.findAll();}
 @Transactional List<Model.Employee> upload(MultipartFile file){
  List<Model.Employee> parsed=new ArrayList<>();
  for(String[] row:csv.read(file)){
   if(row.length!=2||row[0].isBlank())throw ApiErrors.bad("Invalid employee CSV row");
   double efficiency;
   try{efficiency=Double.parseDouble(row[1]);}catch(NumberFormatException ex){throw ApiErrors.bad("Invalid employee efficiency");}
   if(!Double.isFinite(efficiency)||efficiency<0)throw ApiErrors.bad("Efficiency must be nonnegative and finite");
   Model.Employee e=new Model.Employee();e.name=row[0];e.efficiency=efficiency;parsed.add(e);
  }
  repo.saveAll(parsed);return repo.findAll();
 }
 @Transactional Model.Employee setEmployed(Long id,boolean employed){
  Model.Employee e=repo.findById(id).orElseThrow(()->ApiErrors.missing("Employee not found"));
  if(e.employed==employed)throw ApiErrors.bad(employed?"Already hired":"Already unemployed");
  e.employed=employed;return repo.save(e);
 }
}
