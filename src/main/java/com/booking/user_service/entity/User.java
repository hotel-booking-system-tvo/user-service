package com.booking.user_service.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="\"user\"")
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "\"user_id\"", columnDefinition = "uniqueidentifier")
    private String id;
    
    @Column(name = "\"fullName\"", columnDefinition = "NVARCHAR(255)")
    private String fullName;
    
    @Column(name = "\"username\"", columnDefinition = "NVARCHAR(255)")
    private String username;

    @Column(name = "\"email\"")
    private String email;

    @JsonIgnore
    @Column(name = "\"password\"")
    private String password;

    @Column(name = "\"gender\"")
    private String gender;

    @Column(name = "\"phone\"")
    private String phone;

    @Column(name = "\"status\"")
    private boolean status;
    
    @Column(name = "\"active\"")
    private boolean active;
    
	@CreationTimestamp
	@Column(name = "\"createdate\"", nullable = false, updatable = false)
	@CreatedDate
	private Date createdDate;
	
	@Column(name = "\"updateddate\"")
	@LastModifiedDate
	private Date updatedDate;
	
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
