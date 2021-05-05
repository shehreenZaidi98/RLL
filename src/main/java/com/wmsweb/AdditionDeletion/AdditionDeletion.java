package com.wmsweb.AdditionDeletion;

import javax.persistence.*;

@Entity
@Table(name = "addition_deletion")
public class AdditionDeletion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
