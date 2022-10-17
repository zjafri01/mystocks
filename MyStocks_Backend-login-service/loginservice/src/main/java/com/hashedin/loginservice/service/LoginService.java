package com.hashedin.loginservice.service;

import com.hashedin.loginservice.exception.*;
import com.hashedin.loginservice.model.Stock;
import com.hashedin.loginservice.model.User;
import com.hashedin.loginservice.repository.LoginRepository;
import com.hashedin.loginservice.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class LoginService {
    private Stock stock1 = new Stock(1,"State Bank of India",461.00,11.66);
    private Stock stock2 = new Stock(2,"HDFC Bank Limited",1301.30,22.87);
    private Stock stock3 = new Stock(3,"ICICI Bank Limited",709.00,23.22);
    private Stock stock4 = new Stock(4,"IRCTC",653.60,18.44);
    private Stock stock5 = new Stock(5,"Reliance ",2602.50,19.66);
    private Stock stock6 = new Stock(6,"SBIN",456.10,21.60);
    private Stock stock7 = new Stock(7,"Bajaj Finance",12406.50,13.16);
    private Stock stock8 = new Stock(8,"JPInfraTec",657.10,9.92);
    private Stock stock9 = new Stock(9,"Paytm",1822.35,5.51);
    private Stock stock10 = new Stock(10,"RPower",76.70,8.65);
    private Stock stock11 = new Stock(11,"ZEELmt",225.56,11.43);
    private Stock stock12 = new Stock(12,"ONGC",152.21,24.54);
    private Stock stock13 = new Stock(13,"INFY",1410.12,23.45);
    private Stock stock14 = new Stock(14,"DMart",3586.45,12.43);
    private Stock stock15 = new Stock(15,"Britannia",3516.23,19.96);
    private Stock stock16 = new Stock(16,"Indigo",1645.65,31.21);
    private Stock stock17 = new Stock(17,"M&M",928.34,17.25);
    private Stock stock18 = new Stock(18,"SunPharma",911.15,34.92);
    private Stock stock19 = new Stock(19,"TataCoffee",192.25,19.32);
    private Stock stock20 = new Stock(20,"CoalIndia",180.60,14.65);
    private Stock stock21 = new Stock(21,"Wipro",444.85,28.67);

    public List<Stock> stockList = List.of(stock1,stock2,stock3,stock4,stock5,stock6,stock7,stock7,stock8,stock9,stock10,
            stock11,stock12,stock13,stock14,stock15,stock16,stock17,stock18,stock19,stock20,stock21);

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private StockRepository stockRepository;

    private LinkedHashMap<String, Long> sessionMap = new LinkedHashMap<>();

    public User signUp(User user) throws UserAlreadyExistsException{
        if(loginRepository.findByUsername(user.getUsername())!=null) {
            throw new UserAlreadyExistsException("User already exists, please try log-in with that user");
        }
        else
            return loginRepository.save(user);
    }

    public boolean login(User user) {
        User userDetails = null;
        try {
            //adding the userId and username to the session Map for session storage
            userDetails = loginRepository.findByUsername(user.getUsername());
            if (user.getUsername().equals(userDetails.getUsername()) && user.getPassword().equals(userDetails.getPassword())) {
                return true;
            }
        }
        catch (NullPointerException nullPointerException) {
            return false;
        }
        return false;
    }

    public boolean validateSecurityQuestion(User user, User userDetails){
        return user.getSecurityQuestionIndex().equals(userDetails.getSecurityQuestionIndex())  &&
                user.getSecurityAnswer().equals(userDetails.getSecurityAnswer());
    }

    public boolean resetPassword(User user) throws UserDoesNotExistException{
        User userDetails = loginRepository.findByUsername(user.getUsername());
        if(userDetails==null) {
            throw new UserDoesNotExistException("User does not exist with the username, please retry with correct user.");
        }
        if(!user.getNewPassword().equals(user.getConfirmPassword())) {
            throw new ResetPasswordException("New password and Confirm password does not match.");
        }
        if(!validateSecurityQuestion(user,userDetails)) {
            throw new ResetPasswordException("Incorrect security answer, please try with the correct answer.");
        }
        if(user.getNewPassword().equals(userDetails.getPassword())){
            throw new ResetPasswordException("New Password entered matches with the old password, please enter a new password.");
        }

        userDetails.setPassword(user.getNewPassword());
        loginRepository.save(userDetails);
        return true;
    }

    public boolean logOut(User user){
//        if(sessionMap.get(user.getUsername())==null){
//            throw new UserNotLoggedInException("Cannot log-out as the user is not available in the users logged-in session");
//        }
        sessionMap.remove(user.getUsername());
        return true;
    }

    public List<User> findAllUsers(){
        return loginRepository.findAll();
    }

    /*@Bean
    private void getAllStocks(){
        stockList = stockRepository.findAll();
    }*/
}
