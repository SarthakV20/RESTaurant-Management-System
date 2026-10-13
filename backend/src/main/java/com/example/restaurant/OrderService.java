package com.example.restaurant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service class OrderService {
 private final OrderRepo orders;private final RecipeRepo recipes;private final IngredientRepo ingredients;private final StateRepo states;private final RestaurantService restaurant;
 OrderService(OrderRepo orders,RecipeRepo recipes,IngredientRepo ingredients,StateRepo states,RestaurantService restaurant){this.orders=orders;this.recipes=recipes;this.ingredients=ingredients;this.states=states;this.restaurant=restaurant;}
 List<Model.FoodOrder> list(){return orders.findAll();}
 @Transactional Model.FoodOrder create(List<String> names){
  if(names==null||names.isEmpty()||names.stream().anyMatch(x->x==null||x.isBlank()))throw ApiErrors.bad("At least one valid recipe required");
  Model.State s=restaurant.locked();if(!s.open)throw ApiErrors.bad("Restaurant is closed");
  Map<String,Integer> needed=new HashMap<>();
  for(String name:names){Model.Recipe r=recipes.findById(name).orElseThrow(()->ApiErrors.bad("Unknown recipe: "+name));for(String ingredient:r.ingredients)needed.merge(ingredient,1,Integer::sum);}
  Map<String,Model.Ingredient> found=new HashMap<>();boolean available=true;
  for(var entry:needed.entrySet()){
   Model.Ingredient ingredient=ingredients.findById(entry.getKey()).orElse(null);
   if(ingredient==null||ingredient.quantity<entry.getValue())available=false;
   else found.put(entry.getKey(),ingredient);
  }
  Model.FoodOrder order=new Model.FoodOrder();order.recipes=new ArrayList<>(names);order.status=available?"RECEIVED":"REFUSED";
  if(available){for(var entry:needed.entrySet()){Model.Ingredient i=found.get(entry.getKey());i.quantity-=entry.getValue();ingredients.save(i);}}
  return orders.save(order);
 }
 @Transactional Model.FoodOrder complete(Long id){
  Model.State s=restaurant.locked();Model.FoodOrder o=orders.findById(id).orElseThrow(()->ApiErrors.missing("Order not found"));
  if(!"RECEIVED".equals(o.status))throw ApiErrors.bad("Only received orders can be completed");
  o.status="COMPLETED";s.completedOrders++;states.save(s);return orders.save(o);
 }
 @Transactional Model.FoodOrder rate(Long id,int stars){
  if(stars<0||stars>5)throw ApiErrors.bad("Rating must be between 0 and 5");
  Model.State s=restaurant.locked();Model.FoodOrder o=orders.findById(id).orElseThrow(()->ApiErrors.missing("Order not found"));
  if(!"COMPLETED".equals(o.status)||o.stars!=null)throw ApiErrors.bad("Order must be completed and not previously rated");
  o.stars=stars;orders.save(o);
  // Rating represents all rated completed orders, including historical orders after reopening.
  List<Model.FoodOrder> rated=orders.findAll().stream().filter(x->x.stars!=null).toList();
  s.rating=rated.stream().mapToInt(x->x.stars).average().orElse(0);states.save(s);return o;
 }
}
