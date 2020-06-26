package com.gm.wordsyn.service;

import com.gm.wordsyn.dao.AdminMenuDAO;
import com.gm.wordsyn.entity.AdminMenu;
import com.gm.wordsyn.entity.AdminRoleMenu;
import com.gm.wordsyn.entity.AdminUserRole;
import com.gm.wordsyn.entity.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AdminMenuService {
    @Autowired
    AdminMenuDAO adminMenuDAO;
    @Autowired
    UserService userService;
    @Autowired
    AdminUserRoleService adminUserRoleService;
    @Autowired
    AdminRoleMenuService adminRoleMenuService;

    /**
     * 得到该父目录的所有子目录
     * @param parentId
     * @return
     */
    public List<AdminMenu> getAllByParentId(int parentId) {
        return adminMenuDAO.findAllByParentId(parentId);
    }

    /**
     * 得到该用户权限下的所有父目录
     * @return
     */
    public List<AdminMenu> getMenusByCurrentUser() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();     //从系统里得到用户名
        User user = userService.findByUsername(username);
        List<AdminUserRole> userRoleList = adminUserRoleService.listAllByUid(user.getId());
        List<AdminMenu> menus = new ArrayList<>();
        for (AdminUserRole userRole : userRoleList) {
            List<AdminRoleMenu> rms = adminRoleMenuService.findAllByRid(userRole.getRid());
            for (AdminRoleMenu rm : rms) {
                // 增加防止多角色状态下菜单重复的逻辑
                AdminMenu menu = adminMenuDAO.findById(rm.getMid());
                boolean isExist = false;
                for (AdminMenu m : menus) {
                    if (m.getId() == menu.getId()) {
                        isExist = true;
                    }
                }
                if (!isExist) {
                    menus.add(menu);
                }
            }
        }
        handleMenus(menus);
        return menus;
    }

    public List<AdminMenu> getMenusByRoleId(int rid) {
        List<AdminMenu> menus = new ArrayList<>();
        List<AdminRoleMenu> rms = adminRoleMenuService.findAllByRid(rid);
        for (AdminRoleMenu rm : rms) {
            menus.add(adminMenuDAO.findById(rm.getMid()));
        }
        handleMenus(menus);
        return menus;
    }

    /**
     * 为父菜单，添加其子菜单
     * @param menus
     */
    public void handleMenus(List<AdminMenu> menus) {
        for (AdminMenu menu : menus) {
            List<AdminMenu> children = getAllByParentId(menu.getId());
            menu.setChildren(children);
        }

        menus.removeIf(menu -> menu.getParentId() != 0);
    }
}
