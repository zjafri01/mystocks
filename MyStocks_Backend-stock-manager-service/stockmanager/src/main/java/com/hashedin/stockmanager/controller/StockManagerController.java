package com.hashedin.stockmanager.controller;

import com.hashedin.stockmanager.exception.UserAlreadyExistsException;
import com.hashedin.stockmanager.exception.UserAlreadyLoggedInException;
import com.hashedin.stockmanager.exception.UserDoesNotExistException;
import com.hashedin.stockmanager.exception.UserNotLoggedInException;
import com.hashedin.stockmanager.model.StockBuySellModel;
import com.hashedin.stockmanager.model.User;
import org.json.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


import java.util.*;

@RestController
@RequestMapping("/mystocks")
@CrossOrigin
public class StockManagerController {

    private static Logger logger = LoggerFactory.getLogger(StockManagerController.class);

    @Value("${stock-manager.login-service}")
    private String loginServiceURIPath;

    @Value("${stock-manager.user-stock-service}")
    private String userStockServiceURIPath;

    @Autowired
    public RestTemplate restTemplate;

    private LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();

    private LinkedHashMap<String, String> sessionMap = new LinkedHashMap<>();

    private HashMap<String,String> invalidSessionMessage = new HashMap<>();

    //----------------------------------------------login-service APIs start--------------------------------------------

    @PostMapping("/login")
    public Object login(@RequestBody User user){
        logger.info(user.getUsername()+" Trying to login... ");
//        String.format("Hello %1$s, your name is %1$s and the time is %2$t", name, time)
//        uuid, username, action, path
        ResponseEntity response;
        responseMap.clear();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            UUID uuid=UUID.randomUUID();
            headers.set("UUID",uuid.toString());
            //logger.info(String.format("UUID-%1$s User-%2$s Action Path-/mystocks/login", uuid, user.getUsername()));
            logger.info("UUID-"+uuid.toString()+" user-"+user.getUsername()+" Action-login Path-/mystocks/login");
            HttpEntity<User> entity = new  HttpEntity<>(user,headers);
            sessionMap.put(uuid.toString(),user.getUsername());
            response = restTemplate.exchange(loginServiceURIPath+"loginUser/login", HttpMethod.POST, entity, Object.class);
            return response.getBody();
        }
        catch (HttpClientErrorException exception){
            return ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
        }
        catch (UserAlreadyLoggedInException userAlreadyLoggedInException){
            HttpHeaders headers = new HttpHeaders();
            headers.add("errorMessage", userAlreadyLoggedInException.getMessage());
            responseMap.put("errorMessage",userAlreadyLoggedInException.getMessage());
            responseMap.put("httpStatus",HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(headers).body(responseMap);
        }
    }

    //3methods
    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody User user){
        responseMap.clear();
        try{
            HttpHeaders headers = new HttpHeaders();
            UUID uuid=UUID.randomUUID();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("UUID",uuid.toString());
            HttpEntity<User> entity = new  HttpEntity<>(user,headers);
            sessionMap.put(uuid.toString(),user.getUsername());
            ResponseEntity response = restTemplate.exchange(loginServiceURIPath+"loginUser/signup", HttpMethod.POST, entity, Object.class);
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }
        catch (HttpStatusCodeException exception){
            return ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
        }
    }

    @PostMapping("/validateSecurityQuestion")
    public ResponseEntity validateSecurityQuestion(@RequestBody User user){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<User> entity = new  HttpEntity<>(user,headers);
            ResponseEntity response = restTemplate.exchange(loginServiceURIPath+"loginUser/validateSecurityQuestion",HttpMethod.POST,entity, Object.class);
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }
        catch (HttpStatusCodeException exception){
            return ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestBody User user){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<User> entity = new  HttpEntity<>(user,headers);
            ResponseEntity response = restTemplate.exchange(loginServiceURIPath+"loginUser/resetPassword",HttpMethod.POST,entity, Object.class);
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }
        catch (HttpStatusCodeException exception){
            return ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
        }
    }

    @DeleteMapping("/logout")
    public ResponseEntity logOut(@RequestBody User user, @RequestHeader LinkedHashMap<String, String> headersRequest){
        responseMap.clear();
            if(sessionMap.get(headersRequest.get("uuid"))==null){
                responseMap.put("username",user.getUsername());
                responseMap.put("errorMessage","Cannot log-out as the user is not available in the users logged-in session");
                responseMap.put("httpStatus",HttpStatus.FORBIDDEN.value());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseMap);
            }
            sessionMap.remove(headersRequest.get("uuid"));
            responseMap.put("username",user.getUsername());
            responseMap.put("successMessage","User logged-out successfully");
            responseMap.put("httpStatus",HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(responseMap);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<Object[]> getUsers(){
        Object[] usersList = restTemplate.getForObject("http://localhost:8081/loginUser/getUsers",Object[].class);
        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }

    //----------------------------------------------login-service APIs end----------------------------------------------


    //------------------------------------------user-stock-service APIs start-------------------------------------------

    @GetMapping("/getAllStocks")
    public ResponseEntity<Object[]> getAllStocks(){
        Object[] response = restTemplate.getForObject(userStockServiceURIPath+"stocks/getAllStocks", Object[].class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/getWallet")
    public ResponseEntity getWallet(@RequestBody User user, @RequestHeader LinkedHashMap<String, String> headersRequest){
        if(!checkIsValidSession(user.getUsername(), headersRequest.get("uuid"))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(invalidSessionMessage);
        }
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<User> entity = new  HttpEntity<>(user,headers);
            ResponseEntity response = restTemplate.exchange(userStockServiceURIPath+"stocks/getWallet",HttpMethod.POST,entity, Object.class);
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }
        catch (HttpStatusCodeException exception){
            return ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
        }
    }

    @PostMapping("/getUserPortfolio")
    public ResponseEntity getUserPortfolio(@RequestBody User user, @RequestHeader LinkedHashMap<String, String> headersRequest){
        if(!checkIsValidSession(user.getUsername(), headersRequest.get("uuid"))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(invalidSessionMessage);
        }
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<User> entity = new  HttpEntity<>(user,headers);
            ResponseEntity response = restTemplate.exchange(userStockServiceURIPath+"stocks/getUserPortfolio",HttpMethod.POST,entity, Object.class);
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }
        catch (HttpStatusCodeException exception){
            return ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
        }
    }

    @PostMapping("/getUserTransactions")
    public ResponseEntity getUserTransactions(@RequestBody User user, @RequestHeader LinkedHashMap<String, String> headersRequest){
        if(!checkIsValidSession(user.getUsername(), headersRequest.get("uuid"))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(invalidSessionMessage);
        }
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<User> entity = new  HttpEntity<>(user,headers);
            ResponseEntity response = restTemplate.exchange(userStockServiceURIPath+"stocks/getUserTransactions",HttpMethod.POST,entity, Object.class);
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }
        catch (HttpStatusCodeException exception){
            return ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
        }
    }

    @PostMapping("/getUserWalletTransactions")
    public ResponseEntity getUserWalletTransactions(@RequestBody User user, @RequestHeader LinkedHashMap<String, String> headersRequest){
        if(!checkIsValidSession(user.getUsername(), headersRequest.get("uuid"))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(invalidSessionMessage);
        }
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<User> entity = new  HttpEntity<>(user,headers);
            ResponseEntity response = restTemplate.exchange(userStockServiceURIPath+"stocks/getUserWalletTransactions",HttpMethod.POST,entity, Object.class);
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }
        catch (HttpStatusCodeException exception){
            return ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
        }
    }

    @PostMapping("/addMoney")
    public ResponseEntity addMoney(@RequestBody User user, @RequestHeader LinkedHashMap<String, String> headersRequest){
        if(!checkIsValidSession(user.getUsername(), headersRequest.get("uuid"))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(invalidSessionMessage);
        }
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<User> entity = new  HttpEntity<>(user,headers);
            ResponseEntity response = restTemplate.exchange(userStockServiceURIPath+"stocks/addMoney",HttpMethod.POST,entity, Object.class);
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }
        catch (HttpStatusCodeException exception){
            return ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
        }
    }

    /*@PostMapping("/buy")
    public ResponseEntity buyStocks(@RequestBody StockBuySellModel stockBuySellObj){
        if(!checkIsValidSession(stockBuySellObj.getUsername())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(invalidSessionMessage);
        }
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<StockBuySellModel> entity = new  HttpEntity<>(stockBuySellObj,headers);
            ResponseEntity response = restTemplate.exchange(userStockServiceURIPath+"stocks/buy",HttpMethod.POST,entity, Object.class);
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }
        catch (HttpStatusCodeException exception){
            return ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
        }
    }*/

    @PostMapping("/buy")
    public ResponseEntity buyStocks(@RequestBody StockBuySellModel stockBuySellObj,@RequestHeader LinkedHashMap<String, String> headersRequest){
        System.out.println(headersRequest.get("uuid"));
        if(!checkIsValidSession(stockBuySellObj.getUsername(),headersRequest.get("uuid"))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(invalidSessionMessage);
        }
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<StockBuySellModel> entity = new  HttpEntity<>(stockBuySellObj,headers);
            ResponseEntity response = restTemplate.exchange(userStockServiceURIPath+"stocks/buy",HttpMethod.POST,entity, Object.class);
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }
        catch (HttpStatusCodeException exception){
            return ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
        }
    }

    @PostMapping("/sell")
    public ResponseEntity sellStocks(@RequestBody StockBuySellModel stockBuySellObj, @RequestHeader LinkedHashMap<String, String> headersRequest){
        if(!checkIsValidSession(stockBuySellObj.getUsername(),headersRequest.get("uuid"))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(invalidSessionMessage);
        }
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<StockBuySellModel> entity = new  HttpEntity<>(stockBuySellObj,headers);
            ResponseEntity response = restTemplate.exchange(userStockServiceURIPath+"stocks/sell",HttpMethod.POST,entity, Object.class);
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }
        catch (HttpStatusCodeException exception){
            return ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
        }
    }

    //User API to get individual User's Notifications
    @PostMapping("/getNotifications")
    public ResponseEntity getNotifications(@RequestBody User user, @RequestHeader LinkedHashMap<String, String> headersRequest){
        if(!checkIsValidSession(user.getUsername(), headersRequest.get("uuid"))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(invalidSessionMessage);
        }
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.add("uuid", headersRequest.get("uuid"));
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<User> entity = new  HttpEntity<>(user,headers);
            ResponseEntity response = restTemplate.exchange(userStockServiceURIPath+"stocks/getNotifications",HttpMethod.POST,entity, Object.class);
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }
        catch (HttpStatusCodeException exception){
            return ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
        }
    }

    //User API to get individual User's Notifications
    @PostMapping("/updateNotifications")
    public ResponseEntity updateNotifications(@RequestBody User user, @RequestHeader LinkedHashMap<String, String> headersRequest){
        if(!checkIsValidSession(user.getUsername(), headersRequest.get("uuid"))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(invalidSessionMessage);
        }
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.add("uuid", headersRequest.get("uuid"));
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<User> entity = new  HttpEntity<>(user,headers);
            ResponseEntity response = restTemplate.exchange(userStockServiceURIPath+"stocks/updateNotifications",HttpMethod.POST,entity, Object.class);
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }
        catch (HttpStatusCodeException exception){
            return ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
        }
    }

    //------------------------------------------user-stock-service APIs end-------------------------------------------


    //Method to check if session is valid and is it present in session map
//    private boolean checkIsValidSession(String username) {
//        if(sessionMap.get(username)==null){
//            invalidSessionMessage.put("errorMessage","User is not available in the current users logged-in session, please login again.");
//            return false;
//        }
//        return true;
//    }
    private boolean checkIsValidSession(String username, String uuid) {
        if(!(sessionMap.get(uuid)!=null && sessionMap.get(uuid).equals(username))){
            invalidSessionMessage.put("errorMessage","User is not available in the current users logged-in session, please login again.");
            return false;
        }
        return true;
    }

}
