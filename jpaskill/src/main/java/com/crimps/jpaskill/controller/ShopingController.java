package com.crimps.jpaskill.controller;

import com.crimps.jpaskill.dto.Customer;
import com.crimps.jpaskill.dto.MyOrder;
import com.crimps.jpaskill.repository.MyOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.List;

@Controller("/shoping")
public class ShopingController {

    @Resource
    private MyOrderRepository myOrderRepository;

    @RequestMapping("testQuery")
    public void testQuery(){

    }

    /**
     * 内连接查询
     */
    @RequestMapping("/q1")
    public void specification1() {

    }

    private void resultPrint(Specification<MyOrder> spec) {

    }
}
