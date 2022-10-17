package com.hashedin.loginservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Audit {
    @Id
    private Long auditId;
    @Column
    private Date timestamp;
    @OneToOne
    private User user;
    @Column
    private String uuid;
    @Column
    private String path;
    @Column
    private String action;
}
