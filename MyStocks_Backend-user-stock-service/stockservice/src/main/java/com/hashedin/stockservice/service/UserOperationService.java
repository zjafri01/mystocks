package com.hashedin.stockservice.service;

import com.hashedin.stockservice.exception.InsufficientStocksAvailableException;
import com.hashedin.stockservice.exception.InsufficientWalletAmountException;
import com.hashedin.stockservice.exception.StockDoesNotExists;
import com.hashedin.stockservice.exception.UserDoesNotExistException;
import com.hashedin.stockservice.model.*;
import com.hashedin.stockservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserOperationService {
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

    private List<Stock> stockList = List.of(stock1,stock2,stock3,stock4,stock5,stock6,stock7,stock7,stock8,stock9,stock10,
            stock11,stock12,stock13,stock14,stock15,stock16,stock17,stock18,stock19,stock20,stock21);

    //private List<Stock> stockList = null;
    @Autowired
    private UserOperationRepository userOperationRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private UserStockDetailsRepository userStockDetailsRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    public Double getWallet(User user) {
        User userDetails = null;
        userDetails = userOperationRepository.findUserByUsername(user.getUsername()); // wallet 0
        if(userDetails==null)
            throw new UserDoesNotExistException("User does not exist with the username, please retry with correct user.");
        return userDetails.getWalletAmount();
    }

    public LinkedHashMap<String, Object> getUserPortfolio(User user){
        HashMap<Integer, Double> userStockQuantityMap = new HashMap<>();
        User userDetails = null;
        userDetails = userOperationRepository.findUserByUsername(user.getUsername());
        if(userDetails==null)
            throw new UserDoesNotExistException("User does not exist with the username, please retry with correct user.");
        List<UserStockDetails> userStockDetailsList = userStockDetailsRepository.findUserStockDetailsByUser(userDetails);
        //List<Stock> listOfAllStocks

        for(UserStockDetails userStockDetails: userStockDetailsList){
            if(!userStockQuantityMap.containsKey(userStockDetails.getStock().getStockId())){
                System.out.println("x");
                Double userStockQuantityDouble = userStockDetailsRepository.findCurrentUserStockQuantity(userStockDetails.getStock().getStockId(),userDetails.getUserId());
                if(userStockQuantityDouble>0){
                    userStockQuantityMap.put(userStockDetails.getStock().getStockId(), userStockQuantityDouble);
                }
            }
        }
        List<Object> listOfUserStocks = new ArrayList<>();

        LinkedHashMap<String, Object> portfolioMap = new LinkedHashMap<>();
        portfolioMap.put("username",userDetails.getUsername());
        portfolioMap.put("contactNo",userDetails.getContactNo());
        portfolioMap.put("email",userDetails.getEmailId());
        portfolioMap.put("panNo",userDetails.getPanNumber());


        for(Integer stockId: userStockQuantityMap.keySet()){
            for (Stock stock: stockList){
                if(stock.getStockId().equals(stockId)){
                    HashMap<String, Object> stockHashMap = new HashMap<>();
                    stockHashMap.put("stockId",stock.getStockId());
                    stockHashMap.put("stockName",stock.getStockName());
                    stockHashMap.put("price",stock.getPrice());
                    stockHashMap.put("peRatio",stock.getPeRatio());
                    stockHashMap.put("quantity",userStockQuantityMap.get(stock.getStockId()));
                    listOfUserStocks.add(stockHashMap);
                    //stockHashMap.clear();
                }
            }

        }
        portfolioMap.put("stocks",listOfUserStocks);
        return portfolioMap;
    }


    public List<TransactionHistory> getUserTransactions(User user){
        User userDetails = userOperationRepository.findUserByUsername(user.getUsername()); // wallet 0
        if(userDetails==null)
            throw new UserDoesNotExistException("User does not exist with the username, please retry with correct user.");
        List<Transaction> userTransactionList = transactionRepository.findTransactionByUser(userDetails);
        List<TransactionHistory> userTransactionHistoryList = new ArrayList<>();
        Collections.reverse(userTransactionList);
        for(Transaction transaction: userTransactionList){
            if(transaction.getOperationType().equals("buy") || transaction.getOperationType().equals("sell")){
                Order order = orderDetailsRepository.getById(transaction.getOrder().getOrderId());
                TransactionHistory transactionHistory = new TransactionHistory(transaction.getTransactionId(),transaction.getOperationType(),
                        transaction.getTransactionAmount(), transaction.getTransactionDate(), order.getQuantity(),order.getStock().getStockName(),order.getStock().getPrice(), order.getStock());
                if(userTransactionHistoryList.size()<5)
                    userTransactionHistoryList.add(transactionHistory);
                else break;
            }
        }
        return userTransactionHistoryList;
    }

    public List<WalletHistory> getUserWalletTransactions(User user){
        User userDetails = userOperationRepository.findUserByUsername(user.getUsername()); // wallet 0
        if(userDetails==null)
            throw new UserDoesNotExistException("User does not exist with the username, please retry with correct user.");
        List<Transaction> userTransactionList = transactionRepository.findTransactionByUser(userDetails);
        List<WalletHistory> userTransactionHistoryList = new ArrayList<>();
        Collections.reverse(userTransactionList);
        for(Transaction transaction: userTransactionList){
            if(transaction.getOperationType().equals("addMoney")){
                WalletHistory walletHistory = new WalletHistory(transaction.getTransactionId(),transaction.getOperationType(),
                        transaction.getTransactionAmount(), transaction.getTransactionDate());
                if(userTransactionHistoryList.size()<5)
                    userTransactionHistoryList.add(walletHistory);
                else break;
            }
        }
        return userTransactionHistoryList;
    }

    public Double addMoney(User user) {
        User userDetails = null;
        Transaction transaction = new Transaction();

        userDetails = userOperationRepository.findUserByUsername(user.getUsername()); // wallet 0
        if(userDetails==null)
            throw new UserDoesNotExistException("User does not exist with the username, please retry with correct user.");

        //Adding Money to wallet
        userDetails.setWalletAmount(userDetails.getWalletAmount()+user.getWalletAmount());

        userOperationRepository.save(userDetails);

        transaction.setUser(userDetails);
        transaction.setTransactionDate(new java.util.Date());
        transaction.setTransactionAmount(user.getWalletAmount());
        transaction.setOperationType("addMoney");
        transaction.setTransactionStatus("success");
        Order userOrder = new Order(null, null, new Date(), "addMoney",user.getWalletAmount());
        transaction.setOrder(userOrder);
        userOrder.setOrderStatus("success");
        orderDetailsRepository.save(userOrder);
        Notification notification = new Notification(userDetails.getUsername(),  "You have transferred "+ user.getWalletAmount().toString() + " rupees to your Wallet successfully", "0", new Date());
        notificationRepository.save(notification);
        transactionRepository.save(transaction);
        return userDetails.getWalletAmount();
    }


    public String buyStocks(StockBuySellModel stockBuySellObj) {
        User userDetails = null;
        Transaction userStockTransaction = null;
        Double totalPrice = 0d;
        Double walletAmount = 0d;
        boolean found = false;
        userDetails = userOperationRepository.findUserByUsername(stockBuySellObj.getUsername()); // wallet 0
        if(userDetails!=null){
            totalPrice = stockBuySellObj.getQuantity()*stockBuySellObj.getPrice();
            walletAmount = userDetails.getWalletAmount();
        }

        Stock buyStock = null;
        for(Stock stock: stockList){
            if(stock.getStockId().equals(stockBuySellObj.getStockId())){
                buyStock=stock;
                found = true;
                break;
            }
        }

        //Updating order table with the order details
        //Order userOrder = new Order(buyStock, stockBuySellObj.getQuantity(), stockBuySellObj.getOperationDate(), stockBuySellObj.getOperationType(),totalPrice);
        Order userOrder = new Order(buyStock, stockBuySellObj.getQuantity(), new Date(), stockBuySellObj.getOperationType(),totalPrice);
        if(userDetails==null){
            userOrder.setOrderStatus("failed");
            orderDetailsRepository.save(userOrder);
            throw new UserDoesNotExistException("User does not exist with the username, please retry with correct user.");
        }

        if(!found) {
            userOrder.setOrderStatus("failed");
            orderDetailsRepository.save(userOrder);
            throw new StockDoesNotExists("Stock does not exists with the entered stock id.");
        }
        //Updating Wallet if amount is less than wallet amount.
        if(walletAmount==null || walletAmount==0 || totalPrice>walletAmount){
            userOrder.setOrderStatus("failed");
            orderDetailsRepository.save(userOrder);
            throw new InsufficientWalletAmountException("Wallet amount insufficient to do the purchase, please recharge immediately.");
        }
        else {
            userDetails.setWalletAmount(userDetails.getWalletAmount() - totalPrice);
            Double totalAvailableQuantity = calculateTotalAvailableStocksForUser(userDetails, buyStock);
            userOperationRepository.save(userDetails);
            userOrder.setOrderStatus("success");
            UserStockDetails userStockDetails = new UserStockDetails(userDetails,buyStock,stockBuySellObj.getOperationType(),stockBuySellObj.getPrice(),(totalAvailableQuantity+stockBuySellObj.getQuantity()),new Date());
            userStockDetailsRepository.save(userStockDetails);
            userStockTransaction = new Transaction(userDetails,userOrder,new Date(),totalPrice,stockBuySellObj.getOperationType(),userOrder.getOrderStatus());
            orderDetailsRepository.save(userOrder);
            Notification notification = new Notification(userDetails.getUsername(),  "You have purchased "+ stockBuySellObj.getStockName() + " stocks successfully", "0", new Date());
            notificationRepository.save(notification);
            transactionRepository.save(userStockTransaction);
        }
        return "success";
    }

    public String sellStocks(StockBuySellModel stockBuySellObj) {
        User userDetails = null;
        List<Order> userOrderDetails = null;
        Double totalPrice = 0d;
        Double walletAmount = 0d;
        boolean isValidStock = false;

        // Checking for stock validity
        Stock sellStock = null;
        for(Stock stock: stockList){
            if(stock.getStockId().equals(stockBuySellObj.getStockId())){
                sellStock=stock;
                isValidStock = true;
                break;
            }
        }

        userDetails = userOperationRepository.findUserByUsername(stockBuySellObj.getUsername()); // wallet 0

        //Updating order table with the order details
        Order userOrder = new Order(sellStock, stockBuySellObj.getQuantity(), new Date(), stockBuySellObj.getOperationType(),totalPrice);

        //Check for valid user
        if(userDetails==null){
            userOrder.setOrderStatus("failed");
            orderDetailsRepository.save(userOrder);
            throw new UserDoesNotExistException("User does not exist with the username, please retry with correct user.");
        }

        if(!isValidStock) {
            userOrder.setOrderStatus("failed");
            orderDetailsRepository.save(userOrder);
            throw new StockDoesNotExists("Stock does not exists with the entered stock id.");
        }
        Double totalAvailableQuantity = calculateTotalAvailableStocksForUser(userDetails,sellStock);

        //if available quantity is greater or equal to sell quantity
        if(totalAvailableQuantity >= stockBuySellObj.getQuantity()){
            totalPrice= stockBuySellObj.getQuantity()* stockBuySellObj.getPrice();
        }
        else{
            throw new InsufficientStocksAvailableException("Insufficient stocks available to sell");
        }

        //Now calculating the total amount for transaction and fetching the wallet amount
        walletAmount = userDetails.getWalletAmount();

        //Updating Wallet if user contains stock more or equal to the sell request.
        userDetails.setWalletAmount(walletAmount + totalPrice);
        userOperationRepository.save(userDetails);
        userOrder.setOrderStatus("success");
        userOrder.setOrderAmount(totalPrice);
        UserStockDetails userStockDetails = new UserStockDetails(userDetails,sellStock,stockBuySellObj.getOperationType(),stockBuySellObj.getPrice(),(totalAvailableQuantity-stockBuySellObj.getQuantity()),new Date());
        userStockDetailsRepository.save(userStockDetails);
        orderDetailsRepository.save(userOrder);
        Notification notification = new Notification(userDetails.getUsername(),  "You have sold "+ stockBuySellObj.getStockName() + " stock successfully", "0", new Date());
        notificationRepository.save(notification);
        Transaction userStockTransaction = new Transaction(userDetails,userOrder,new Date(),totalPrice,stockBuySellObj.getOperationType(),userOrder.getOrderStatus());
        transactionRepository.save(userStockTransaction);
        return "success";
    }

    public List<Notification> getUserNotifications(User user, String uuid){
        User userDetails = userOperationRepository.findUserByUsername(user.getUsername()); // wallet 0
        if(userDetails==null)
            throw new UserDoesNotExistException("User does not exist with the username, please retry with correct user.");
        List<Notification> userNotificationList = notificationRepository.findNotificationByUsername(user.getUsername());
        Collections.reverse(userNotificationList);
        return userNotificationList;
    }

    public boolean updateNotifications(User user) {
        User userDetails = null;
        userDetails = userOperationRepository.findUserByUsername(user.getUsername()); // wallet 0
        if(userDetails==null)
            throw new UserDoesNotExistException("User does not exist with the username, please retry with correct user.");
        List<Notification> notificationList = notificationRepository.findNotificationByUsername(userDetails.getUsername());
        for (Notification notification1: notificationList) {
            if(notification1.getStatus().equals("0")) notification1.setStatus("1");
        }
        notificationRepository.saveAll(notificationList);
        return true;
    }

    public List<User> findAllUsers() {
        return userOperationRepository.findAll();
    }

    private Double calculateTotalAvailableStocksForUser(User userDetails, Stock stock){
        Double userStockQuantity = userStockDetailsRepository.findCurrentUserStockQuantity(stock.getStockId(),userDetails.getUserId());
        if(userStockQuantity!=null){
            return userStockQuantity;
        }
        else return 0d;
    }

    public List<Stock> getAllStocks(){
        stockList = stockRepository.findAll();
        return stockList;
    }

    @Bean
    public void saveAllStocks(){
        //stockRepository.saveAll(this.stockList);
        for(Stock stock: this.stockList){
            stockRepository.save(stock);
        }
    }
}
