package com.example.restaurant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service class RestaurantService {
 private final StateRepo states;
 RestaurantService(StateRepo states){this.states=states;}
 @Transactional Model.State get(){return states.findById(1L).orElseGet(()->states.save(new Model.State()));}
 // All stock/order mutations lock this same state row, serializing competing requests.
 Model.State locked(){get();return states.lock(1L).orElseThrow();}
 @Transactional Model.State open(){Model.State s=locked();s.open=true;s.completedOrders=0;s.rating=0;return states.save(s);}
 @Transactional Model.State close(){Model.State s=locked();s.open=false;return states.save(s);}
}
