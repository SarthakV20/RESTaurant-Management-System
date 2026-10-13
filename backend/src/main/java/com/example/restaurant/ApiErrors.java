package com.example.restaurant;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
final class ApiErrors {
 private ApiErrors() {}
 static ResponseStatusException bad(String message){return new ResponseStatusException(HttpStatus.BAD_REQUEST,message);}
 static ResponseStatusException missing(String message){return new ResponseStatusException(HttpStatus.NOT_FOUND,message);}
}
