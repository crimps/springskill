package com.crimps.jpaskill.repository;

import com.crimps.jpaskill.dto.MyOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MyOrderRepository extends JpaRepository<MyOrder, Long>, JpaSpecificationExecutor<MyOrder> {

    @Query("select o from MyOrder o left join Customer c on o.cId = c.id where o.id = :customerId")
    public List<MyOrder> getMyOrderByCustomer(@Param("customerId") Long customerId);
}
