package com.hashedin.stockservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Notification_Table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="notification_sequence")
    @SequenceGenerator(name = "notification_sequence", sequenceName = "notification_sequence")
    private Long notificationId;
    @Column
    private String username;
    @Column
    private String notificationMessage;
    @Column
    private String status;
    @Column
    private Date date;

    public Notification(String username, String notificationMessage, String status, Date date) {
        this.username = username;
        this.notificationMessage = notificationMessage;
        this.status = status;
        this.date = date;
    }

}
