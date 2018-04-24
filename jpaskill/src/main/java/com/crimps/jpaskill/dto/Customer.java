package com.crimps.jpaskill.dto;

import javax.persistence.*;
import java.util.List;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;

//    //一对多，一个客户对应多个订单，关联的字段是订单里的cId字段
//    @OneToMany
//    @JoinColumn(name = "cId")
//    private List<MyOrder> myOrders;

    public Customer(){ }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }
}
