package com.gm.wordsyn.dao;

import com.gm.wordsyn.entity.AdminUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface AdminUserRoleDAO extends JpaRepository<AdminUserRole, Integer> {
    List<AdminUserRole> findAllByUid(int uid);

    void deleteAllByUid(int uid);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "DELETE FROM `wordsyn`.`admin_user_role` WHERE (`uid` = ?1)")
    public void deleteDataByUid(int uid);
}
