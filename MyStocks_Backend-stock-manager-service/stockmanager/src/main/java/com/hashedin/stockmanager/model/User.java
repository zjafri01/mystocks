package com.hashedin.stockmanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String username;
    private String password;

    private String newPassword;
    private String confirmPassword;
    private Integer securityQuestionIndex;
    private String securityAnswer;

    private String panNumber;
    private String contactNo;
    private String emailId;

    private Double walletAmount=0d;

}
