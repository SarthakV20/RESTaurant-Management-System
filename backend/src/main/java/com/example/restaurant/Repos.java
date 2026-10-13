package com.example.restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
interface StateRepo extends JpaRepository<Model.State,Long>{
 @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select s from RestaurantState s where s.id = :id") Optional<Model.State> lock(@Param("id") Long id);
}
interface EmployeeRepo extends JpaRepository<Model.Employee,Long>{}
interface IngredientRepo extends JpaRepository<Model.Ingredient,String>{}
interface RecipeRepo extends JpaRepository<Model.Recipe,String>{}
interface OrderRepo extends JpaRepository<Model.FoodOrder,Long>{}
