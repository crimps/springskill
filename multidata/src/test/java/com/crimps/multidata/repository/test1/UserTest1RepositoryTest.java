package com.crimps.multidata.repository.test1;

import com.crimps.multidata.MultidataApplication;
import com.crimps.multidata.domain.primary.User;
import com.crimps.multidata.repository.test2.UserTest2Repository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MultidataApplication.class})
public class UserTest1RepositoryTest {

    @Autowired
    UserTest1Repository userTest1Repository;

    @Autowired
    UserTest2Repository userTest2Repository;

    @Test
    public void findByUserName() {
        User user1 = userTest1Repository.findByUserName("1");
        com.crimps.multidata.domain.secondary.User user2 = userTest2Repository.findByUserName("2");
        System.out.println(user1.getId());
        System.out.println(user2.getId());
    }

    @Test
    public void findByUserNameOrEmail() {
    }
}