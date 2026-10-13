package com.example.restaurant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
@Service class RecipeService {
 private final RecipeRepo repo;private final CsvReader csv;
 RecipeService(RecipeRepo repo,CsvReader csv){this.repo=repo;this.csv=csv;}
 List<Model.Recipe> list(){return repo.findAll();}
 @Transactional List<Model.Recipe> upload(MultipartFile file){
  List<Model.Recipe> parsed=new ArrayList<>();
  for(String[] row:csv.read(file)){
   if(row.length<3||row[0].isBlank())throw ApiErrors.bad("Invalid recipe CSV row");
   int minutes;try{minutes=Integer.parseInt(row[1]);}catch(NumberFormatException ex){throw ApiErrors.bad("Invalid preparation time");}
   List<String> ingredients=Arrays.stream(row).skip(2).filter(x->!x.isBlank()).toList();
   if(minutes<=0||ingredients.isEmpty())throw ApiErrors.bad("Invalid recipe time or ingredients");
   Model.Recipe r=new Model.Recipe();r.name=row[0];r.minutes=minutes;r.ingredients=new ArrayList<>(ingredients);parsed.add(r);
  }
  repo.saveAll(parsed);return repo.findAll();
 }
}
