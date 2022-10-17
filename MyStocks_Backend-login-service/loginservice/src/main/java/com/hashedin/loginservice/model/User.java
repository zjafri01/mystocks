package com.hashedin.loginservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
    private Long userId=0l;

    @Column
    private String username;
    @Column
    private String password;

    @Transient
    private String newPassword;
    @Transient
    private String confirmPassword;

    @Column
    private Integer securityQuestionIndex;
    @Column
    private String securityAnswer;

    @Column
    private String panNumber;
    @Column
    private String contactNo;
    @Column
    private String emailId;
    @Column
    private Double walletAmount=0d;

}
