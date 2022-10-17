package com.hashedin.stockservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "User_Details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue (strategy= GenerationType.SEQUENCE, generator="user_sequence")
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence")
    private Long userId;
    @Column
    private String username;
    @Column
    private String password;

    private String newPassword;
    private String confirmPassword;

    @Column
    private String panNumber;
    @Column
    private String contactNo;
    @Column
    private String emailId;
    @Column
    private Double walletAmount=0d;

}
