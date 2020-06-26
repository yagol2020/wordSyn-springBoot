package com.gm.wordsyn.dao;

import com.gm.wordsyn.entity.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRoleDAO extends JpaRepository<AdminRole, Integer> {
    AdminRole findById(int id);
}
