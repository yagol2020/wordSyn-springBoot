package com.gm.wordsyn.dao;

import com.gm.wordsyn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface UserDAO extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    User getByUsernameAndPassword(String username, String password);

    @Query(value = "select new User(u.id,u.username,u.name,u.phone,u.email,u.enabled) from User u")
    List<User> list();

    @Query(nativeQuery =true,value="SELECT rid FROM wordsyn.admin_user_role join wordsyn.user where wordsyn" +
            ".admin_user_role.uid=wordsyn.user.id and wordsyn.user.username=?1")
    int roleIdFromUserName(String username);

    @Query(nativeQuery =true,value = "SELECT id FROM wordsyn.user where username=?1")
    int findUserIdByUsername(String username);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "DELETE FROM `wordsyn`.`user` WHERE (`id` = ?1)")
    void deleteDataByUserId(int id);
}
