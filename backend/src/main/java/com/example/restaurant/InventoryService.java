package com.example.restaurant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
@Service class InventoryService {
 private final IngredientRepo repo;private final CsvReader csv;private final RestaurantService restaurant;
 InventoryService(IngredientRepo repo,CsvReader csv,RestaurantService restaurant){this.repo=repo;this.csv=csv;this.restaurant=restaurant;}
 List<Model.Ingredient> list(){return repo.findAll();}
 @Transactional List<Model.Ingredient> upload(MultipartFile file){
  List<Model.Ingredient> parsed=new ArrayList<>();
  for(String[] row:csv.read(file)){
   if(row.length!=2||row[0].isBlank())throw ApiErrors.bad("Invalid ingredient CSV row");
   double cost;try{cost=Double.parseDouble(row[1]);}catch(NumberFormatException ex){throw ApiErrors.bad("Invalid ingredient cost");}
   if(!Double.isFinite(cost)||cost<0)throw ApiErrors.bad("Cost must be nonnegative and finite");
   Model.Ingredient i=repo.findById(row[0]).orElseGet(()->{Model.Ingredient v=new Model.Ingredient();v.name=row[0];return v;});i.cost=cost;parsed.add(i);
  }
  restaurant.locked();repo.saveAll(parsed);return repo.findAll();
 }
 @Transactional Model.Ingredient addStock(String name,int quantity){
  if(quantity<=0)throw ApiErrors.bad("Quantity must be positive");
  restaurant.locked();
  Model.Ingredient i=repo.findById(name).orElseThrow(()->ApiErrors.missing("Ingredient not found"));
  if(i.quantity>Integer.MAX_VALUE-quantity)throw ApiErrors.bad("Stock quantity overflow");
  i.quantity+=quantity;return repo.save(i);
 }
}
