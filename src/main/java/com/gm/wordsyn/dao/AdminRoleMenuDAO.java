package com.gm.wordsyn.dao;

import com.gm.wordsyn.entity.AdminRoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AdminRoleMenuDAO extends JpaRepository<AdminRoleMenu, Integer> {
    List<AdminRoleMenu> findAllByRid(int rid);

    void deleteAllByRid(int rid);
}
