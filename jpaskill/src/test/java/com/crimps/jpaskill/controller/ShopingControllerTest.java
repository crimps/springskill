package com.crimps.jpaskill.controller;

import com.crimps.jpaskill.JpaskillApplication;
import com.crimps.jpaskill.dto.Customer;
import com.crimps.jpaskill.dto.MyOrder;
import com.crimps.jpaskill.repository.MyOrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpaskillApplication.class})
public class ShopingControllerTest {

    @Resource
    MyOrderRepository myOrderRepository;

    @Test
    public void testQuery() {
        List<MyOrder> myOrderList = myOrderRepository.getMyOrderByCustomer(Long.valueOf(1));
        for(MyOrder myOrder : myOrderList){
            System.out.println(myOrder.getCode());
        }
    }

    @Test
    public void testJoin(){
        //根据查询结果，声明返回值对象，这里要查询用户的订单列表，所以声明返回对象为MyOrder
        Specification<MyOrder> spec = new Specification<MyOrder>() {
            //Root<X>  根查询，默认与声明相同
            @Override
            public Predicate toPredicate(Root<MyOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //声明并创建MyOrder的CriteriaQuery对象
                CriteriaQuery<MyOrder> q1 = cb.createQuery(MyOrder.class);
                //连接的时候，要以声明的根查询对象（这里是root，也可以自己创建）进行join
                //Join<Z,X>是Join生成的对象，这里的Z是被连接的对象，X是目标对象，
                //  连接的属性字段是被连接的对象在目标对象的属性，这里是我们在MyOrder内声明的customer
                //join的第二个参数是可选的，默认是JoinType.INNER(内连接 inner join)，也可以是JoinType.LEFT（左外连接 left join）
                Join<Customer, MyOrder> myOrderJoin = root.join("customer", JoinType.INNER);
                //用CriteriaQuery对象拼接查询条件，这里只增加了一个查询条件，cId=1
                q1.select(myOrderJoin).where(cb.equal(root.get("cId"), 1));
                //通过getRestriction获得Predicate对象
                Predicate p1 = q1.getRestriction();
                //返回对象
                return p1;
            }
        };
        //分页查询
        Pageable pageable = new PageRequest(0, 10, Sort.Direction.DESC, "id");
        //查询的分页结果
        Page<MyOrder> page = myOrderRepository.findAll(spec, pageable);
        System.out.println(page);
        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
        for (MyOrder c : page.getContent()) {
            System.out.println(c.toString());
        }
    }

    @Test
    public void testJoinMulParam(){
        Specification<MyOrder> spec = new Specification<MyOrder>() {
            @Override
            public Predicate toPredicate(Root<MyOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                CriteriaQuery<MyOrder> q1 = cb.createQuery(MyOrder.class);
                Join<Customer, MyOrder> myOrderJoin = root.join("customer");
                q1.select(myOrderJoin)
                        .where(
                                cb.equal(root.get("cId"), 1),//cId=1
                                cb.equal(root.get("customer").get("firstName"), "Jack")//对象customer的firstName=Jack
                        );
                Predicate p1 = q1.getRestriction();
                return p1;
            }
        };
        resultPrint(spec);
    }

    @Test
    public void testLeftJoin(){
        Specification<MyOrder> spec = new Specification<MyOrder>() {
            @Override
            public Predicate toPredicate(Root<MyOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                CriteriaQuery<MyOrder> q1 = cb.createQuery(MyOrder.class);
                Join<Customer, MyOrder> myOrderJoin = root.join("customer", JoinType.LEFT);
//                q1.select(myOrderJoin).where(cb.equal(root.get("cId"), 1));
                Predicate p1 = q1.getRestriction();
                return p1;
            }
        };
        resultPrint(spec);
    }

    @Test
    public void testIn(){
        Specification<MyOrder> spec = new Specification<MyOrder>() {
            @Override
            public Predicate toPredicate(Root<MyOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                CriteriaQuery<MyOrder> q1 = cb.createQuery(MyOrder.class);
                Join<Customer, MyOrder> myOrderJoin = root.join("customer");
                q1.select(myOrderJoin)
                        .where(
                                cb.equal(root.get("cId"), 1)
                                , root.get("id").in(1, 2, 4)
                        );

                Predicate p1 = q1.getRestriction();
                return p1;
            }
        };
        resultPrint(spec);
    }

    private void resultPrint(Specification<MyOrder> spec) {
        //分页查询
        Pageable pageable = new PageRequest(0, 10, Sort.Direction.DESC, "id");
        //查询的分页结果
        Page<MyOrder> page = myOrderRepository.findAll(spec, pageable);
        System.out.println(page);
        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
        for (MyOrder c : page.getContent()) {
            System.out.println(c.toString());
        }
    }

    @Test
    public void testSave(){
        MyOrder myOrder = new MyOrder();
        myOrder.setCode("005");
        myOrder.setTotal(new BigDecimal(5));
        Customer customer = new Customer("3_firstName", "3_lastName");
        myOrderRepository.saveAndFlush(myOrder);
    }
}