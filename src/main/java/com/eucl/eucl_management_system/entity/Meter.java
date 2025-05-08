package com.eucl.eucl_management_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "meters",
uniqueConstraints = {
        @UniqueConstraint(columnNames = "meterNumber")
})
public class Meter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 6)
    @Size(min = 6, max = 6, message = "Meter number should be exactly 6 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Meter number must be alphanumeric")
    private String meterNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Meter() {}
    public Meter(String meterNumber, User user) {
        this.meterNumber = meterNumber;
        this.user = user;
    }
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getMeterNumber() {return meterNumber;}
    public void setMeterNumber(String meterNumber) {this.meterNumber = meterNumber;}
    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}
}
