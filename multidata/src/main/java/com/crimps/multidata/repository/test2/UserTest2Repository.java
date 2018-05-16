package com.crimps.multidata.repository.test2;

import com.crimps.multidata.domain.secondary.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author itguang
 * @create 2017-12-07 16:47
 **/
public interface UserTest2Repository extends JpaRepository<User,Long>{
    User findByUserName(String userName);
    User findByUserNameOrEmail(String username, String email);
}
