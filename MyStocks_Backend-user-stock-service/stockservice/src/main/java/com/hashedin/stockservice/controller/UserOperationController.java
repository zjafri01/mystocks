package com.hashedin.stockservice.controller;

import com.hashedin.stockservice.exception.*;
import com.hashedin.stockservice.model.*;
import com.hashedin.stockservice.service.UserOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/stocks")
@CrossOrigin
public class UserOperationController {

    @Autowired
    private UserOperationService userOperationService;

    private LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();

    @PostMapping("/getWallet")
    public ResponseEntity getWallet(@RequestBody User user){
        responseMap.clear();
        try{
            Double walletAmount = userOperationService.getWallet(user);
            responseMap.put("walletAmount",walletAmount);
            responseMap.put("statusCode",HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);
        }
        catch (UserDoesNotExistException userDoesNotExistException){
            HttpHeaders headers = new HttpHeaders();
            headers.add("errorMessage", userDoesNotExistException.getMessage());
            responseMap.put("errorMessage",userDoesNotExistException.getMessage());
            responseMap.put("statusCode",HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(headers).body(responseMap);
        }
    }

    @PostMapping("/getUserPortfolio")
    public ResponseEntity getUserPortfolio(@RequestBody User user){
        responseMap.clear();
        try{
            LinkedHashMap<String, Object> userPortfolio = userOperationService.getUserPortfolio(user);
            responseMap.put("userPortfolio",userPortfolio);
            responseMap.put("statusCode",HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);
        }
        catch (UserDoesNotExistException userDoesNotExistException){
            HttpHeaders headers = new HttpHeaders();
            headers.add("errorMessage", userDoesNotExistException.getMessage());
            responseMap.put("errorMessage",userDoesNotExistException.getMessage());
            responseMap.put("statusCode",HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(headers).body(responseMap);
        }
    }

    @PostMapping("/getUserTransactions")
    public ResponseEntity getUserTransactions(@RequestBody User user){
        responseMap.clear();
        try{
            List<TransactionHistory> userTransactionsList = userOperationService.getUserTransactions(user);
            responseMap.put("userTransactionsHistory",userTransactionsList);
            responseMap.put("statusCode",HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(responseMap);
        }
        catch (UserDoesNotExistException userDoesNotExistException){
            HttpHeaders headers = new HttpHeaders();
            headers.add("errorMessage", userDoesNotExistException.getMessage());
            responseMap.put("errorMessage",userDoesNotExistException.getMessage());
            responseMap.put("statusCode",HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(headers).body(responseMap);
        }
    }

    @PostMapping("/getUserWalletTransactions")
    public ResponseEntity getUserWalletTransactions(@RequestBody User user){
        responseMap.clear();
        try{
            List<WalletHistory> userTransactionsList = userOperationService.getUserWalletTransactions(user);
            responseMap.put("getUserWalletTransactions",userTransactionsList);
            responseMap.put("statusCode",HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(responseMap);
        }
        catch (UserDoesNotExistException userDoesNotExistException){
            HttpHeaders headers = new HttpHeaders();
            headers.add("errorMessage", userDoesNotExistException.getMessage());
            responseMap.put("errorMessage",userDoesNotExistException.getMessage());
            responseMap.put("statusCode",HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(headers).body(responseMap);
        }
    }

    @PostMapping("/addMoney")
    public ResponseEntity addMoney(@RequestBody User user){
        responseMap.clear();
        try{
            Double walletAmount = userOperationService.addMoney(user);
            HttpHeaders headers = new HttpHeaders();
            headers.add("successMessage", "Amount added to wallet successfully");
            responseMap.put("walletAmount",walletAmount);
            responseMap.put("successMessage","Amount added to wallet successfully");
            responseMap.put("successCode",HttpStatus.CREATED.value());
            return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(responseMap);
        }
        catch (UserDoesNotExistException userDoesNotExistException){
            HttpHeaders headers = new HttpHeaders();
            headers.add("errorMessage", userDoesNotExistException.getMessage());
            responseMap.put("errorMessage",userDoesNotExistException.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(headers).body(responseMap);
        }
    }

    @PostMapping("/buy")
    public ResponseEntity buyStocks(@RequestBody StockBuySellModel stockBuySellObj){
        responseMap.clear();
        try{
            stockBuySellObj.setStatus(userOperationService.buyStocks(stockBuySellObj));
            HttpHeaders headers = new HttpHeaders();
            headers.add("successMessage", "Transaction successfully completed");
            stockBuySellObj.setResponseMessage("Transaction successfully completed");
            stockBuySellObj.setStatusCode(HttpStatus.CREATED.value());
            return new ResponseEntity<>(stockBuySellObj,headers,HttpStatus.CREATED);
        }
        catch (UserDoesNotExistException userDoesNotExistException){
            stockBuySellObj.setStatus("failed");
            HttpHeaders headers = new HttpHeaders();
            headers.add("errorMessage", userDoesNotExistException.getMessage());
            stockBuySellObj.setResponseMessage(userDoesNotExistException.getMessage());
            stockBuySellObj.setStatusCode(HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(headers).body(stockBuySellObj);
        }
        catch (StockDoesNotExists stockDoesNotExists){
            stockBuySellObj.setStatus("failed");
            HttpHeaders stockError = new HttpHeaders();
            stockError.add("errorMessage", stockDoesNotExists.getMessage());
            stockBuySellObj.setResponseMessage(stockDoesNotExists.getMessage());
            stockBuySellObj.setStatusCode(HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(stockError).body(stockBuySellObj);
        }
        catch (InsufficientWalletAmountException insufficientWalletAmountException){
            stockBuySellObj.setStatus("failed");
            HttpHeaders walletError = new HttpHeaders();
            walletError.add("errorMessage", insufficientWalletAmountException.getMessage());
            stockBuySellObj.setResponseMessage(insufficientWalletAmountException.getMessage());
            stockBuySellObj.setStatusCode(HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(walletError).body(stockBuySellObj);
        }
    }

    @PostMapping("/sell")
    public ResponseEntity sellStocks(@RequestBody StockBuySellModel stockBuySellObj){
        responseMap.clear();
        try{
            stockBuySellObj.setStatus(userOperationService.sellStocks(stockBuySellObj));
            HttpHeaders headers = new HttpHeaders();
            headers.add("successMessage", "Transaction successfully completed");
            stockBuySellObj.setResponseMessage("Transaction successfully completed");
            stockBuySellObj.setStatusCode(HttpStatus.CREATED.value());
            return new ResponseEntity<>(stockBuySellObj,headers,HttpStatus.CREATED);
        }
        catch (UserDoesNotExistException userDoesNotExistException){
            stockBuySellObj.setStatus("failed");
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error-Message", userDoesNotExistException.getMessage());
            stockBuySellObj.setResponseMessage(userDoesNotExistException.getMessage());
            stockBuySellObj.setStatusCode(HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(headers).body(stockBuySellObj);
        }
        catch (StockDoesNotExists stockDoesNotExists){
            stockBuySellObj.setStatus("failed");
            HttpHeaders stockError = new HttpHeaders();
            stockError.add("errorMessage", stockDoesNotExists.getMessage());
            stockBuySellObj.setResponseMessage(stockDoesNotExists.getMessage());
            stockBuySellObj.setStatusCode(HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(stockError).body(stockBuySellObj);
        }
        catch (InsufficientStocksAvailableException insufficientStocksAvailableException){
            stockBuySellObj.setStatus("failed");
            HttpHeaders walletError = new HttpHeaders();
            walletError.add("errorMessage", insufficientStocksAvailableException.getMessage());
            stockBuySellObj.setResponseMessage(insufficientStocksAvailableException.getMessage());
            stockBuySellObj.setStatusCode(HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(walletError).body(stockBuySellObj);
        }
    }

    @PostMapping("/getNotifications")
    public ResponseEntity getNotifications(@RequestBody User user, @RequestHeader LinkedHashMap<String, String> headersRequest){
        responseMap.clear();
        try{
            List<Notification> userNotificationsList = userOperationService.getUserNotifications(user, headersRequest.get("uuid"));
            responseMap.put("userNotifications",userNotificationsList);
            responseMap.put("statusCode",HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(responseMap);
        }
        catch (UserDoesNotExistException userDoesNotExistException){
            HttpHeaders headers = new HttpHeaders();
            headers.add("errorMessage", userDoesNotExistException.getMessage());
            responseMap.put("errorMessage",userDoesNotExistException.getMessage());
            responseMap.put("statusCode",HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(headers).body(responseMap);
        }
    }

    @PostMapping("/updateNotifications")
    public ResponseEntity updateNotifications(@RequestBody User user){
        responseMap.clear();
        try{
            userOperationService.updateNotifications(user);
            responseMap.put("successMessage","Notifications Read Successfully");
            responseMap.put("statusCode",HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);
        }
        catch (UserDoesNotExistException userDoesNotExistException){
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error-Message", userDoesNotExistException.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(headers).body("");
        }
    }


    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> findAllUsers(){
        return new ResponseEntity<>(userOperationService.findAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/getAllStocks")
    public ResponseEntity<List<Stock>> getAllStocks(){
        return new ResponseEntity<>(userOperationService.getAllStocks(), HttpStatus.OK);
    }


    @GetMapping("/hello")
    public String hello(){
        return "Hello";
    }
}
