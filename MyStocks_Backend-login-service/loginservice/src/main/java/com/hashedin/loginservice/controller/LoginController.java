package com.hashedin.loginservice.controller;

import com.hashedin.loginservice.exception.*;
import com.hashedin.loginservice.model.Stock;
import com.hashedin.loginservice.model.User;
import com.hashedin.loginservice.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/loginUser")
//@CrossOrigin
public class LoginController {

    private LinkedHashMap<String, Object> map = new LinkedHashMap<>();
    @Autowired
    private LoginService loginService;

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody User user,@RequestHeader LinkedHashMap<String, String> headersRequest){
        try{
            map.clear();
            HttpHeaders headers = new HttpHeaders();
            headers.add(user.getUsername(), headersRequest.get("uuid"));
            loginService.signUp(user);
            map.put("stocks",loginService.stockList);
            map.put("uuid", headersRequest.get("uuid"));
            map.put("successMessage","User created successfully");
            map.put("httpStatus",HttpStatus.CREATED.value());
            return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(map);
        }
        catch (UserAlreadyExistsException UserAlreadyExistsException){
            HttpHeaders headers = new HttpHeaders();
            headers.add("errorMessage", UserAlreadyExistsException.getMessage());
            map.put("errorMessage",UserAlreadyExistsException.getMessage());
            map.put("httpStatus",HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(headers).body(map);
        }
    }

    @PostMapping(value = "/login", consumes = {"application/json"})
    public Object login(@RequestBody User user,@RequestHeader LinkedHashMap<String, String> headersRequest){
        map.clear();
        try{
            if(loginService.login(user)){
                //creating headers and also adding stocksList, uuid, success-message and http-status to response body
                HttpHeaders headers = new HttpHeaders();
                //UUID uuid=UUID.randomUUID();
                headers.add(user.getUsername(), headersRequest.get("UUID"));
                //headers.add(user.getUsername(), uuid.toString());
                map.put("stocks",loginService.stockList);
                map.put("uuid",headersRequest.get("uuid"));
                map.put("successMessage","User logged-in successfully");
                map.put("httpStatus",HttpStatus.OK.value());
                return ResponseEntity.status(HttpStatus.OK).headers(headers).body(map);
            }
            map.put("errorMessage","Invalid credentials");
            map.put("httpStatus",HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
        }
        catch (UserAlreadyLoggedInException userAlreadyLoggedInException){
            HttpHeaders headers = new HttpHeaders();
            headers.add("errorMessage", userAlreadyLoggedInException.getMessage());
            map.put("errorMessage",userAlreadyLoggedInException.getMessage());
            map.put("httpStatus",HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(headers).body(map);
        }
        
    }

//    @PostMapping("/validateSecurityQuestion")
//    public ResponseEntity validateSecurityQuestion(@RequestBody User user){
//        map.clear();
//        try{
//            if(loginService.validateSecurityQuestion(user)){
//                map.put("successMessage","Validated successfully");
//                map.put("httpStatus",HttpStatus.OK.value());
//                return ResponseEntity.status(HttpStatus.OK).body(map);
//            }
//            map.put("errorMessage","Validation failed");
//            map.put("httpStatus",HttpStatus.FORBIDDEN.value());
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
//        }
//        catch (UserDoesNotExistException userDoesNotExistException){
//            map.put("errorMessage",userDoesNotExistException.getMessage());
//            map.put("httpStatus",HttpStatus.FORBIDDEN.value());
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
//        }
//    }

    @PostMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestBody User user){
        map.clear();
        try{
            if(loginService.resetPassword(user)){
                map.put("successMessage","Password changed successfully");
                map.put("httpStatus",HttpStatus.OK.value());
                return ResponseEntity.status(HttpStatus.OK).body(map);
            }
            map.put("errorMessage","New password and Confirm password does not match");
            map.put("httpStatus",HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
        }
        catch (UserDoesNotExistException userDoesNotExistException){
            map.put("errorMessage",userDoesNotExistException.getMessage());
            map.put("httpStatus",HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
        }
        catch (ResetPasswordException resetPasswordException){
            map.put("errorMessage",resetPasswordException.getMessage());
            map.put("httpStatus",HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
        }
    }

    @DeleteMapping("/logout")
    public ResponseEntity logOut(@RequestBody User user){
        map.clear();
        try{
            loginService.logOut(user);
            map.put("username",user.getUsername());
            map.put("successMessage","User logged-out successfully");
            map.put("httpStatus",HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }
        catch (UserNotLoggedInException userNotLoggedInException){
            map.put("username",user.getUsername());
            map.put("errorMessage",userNotLoggedInException.getMessage());
            map.put("httpStatus",HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
        }
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> findAllUsers(){
        return new ResponseEntity<>(loginService.findAllUsers(), HttpStatus.OK);
    }

}
