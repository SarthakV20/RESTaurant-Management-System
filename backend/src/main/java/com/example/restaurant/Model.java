package com.example.restaurant;
import jakarta.persistence.*;
import java.util.*;
class Model {
 @Entity(name="RestaurantState") public static class State { @Id public Long id=1L; public boolean open=false; public long completedOrders=0; public double rating=0; }
 @Entity(name="Employee") public static class Employee { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id; public String name; public double efficiency; public boolean employed=false; }
 @Entity(name="Ingredient") public static class Ingredient { @Id public String name; public double cost; public int quantity=0; }
 @Entity(name="Recipe") public static class Recipe { @Id public String name; public int minutes; @ElementCollection(fetch=FetchType.EAGER) public List<String> ingredients=new ArrayList<>(); }
 @Entity(name="FoodOrder") public static class FoodOrder { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id; @ElementCollection(fetch=FetchType.EAGER) public List<String> recipes=new ArrayList<>(); public String status; public Integer stars; }
}
