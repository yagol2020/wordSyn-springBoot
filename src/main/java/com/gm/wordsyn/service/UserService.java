package com.gm.wordsyn.service;

import com.gm.wordsyn.dao.AdminUserRoleDAO;
import com.gm.wordsyn.dao.UserDAO;
import com.gm.wordsyn.entity.AdminRole;
import com.gm.wordsyn.entity.AdminUserRole;
import com.gm.wordsyn.entity.User;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;


@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    AdminUserRoleService adminUserRoleService;
    @Autowired
    AdminUserRoleDAO adminUserRoleDAO;


    public List<User> list() {
        List<User> users = userDAO.list();
        List<AdminRole> roles;
        for (User user : users) {
            roles = adminRoleService.listRolesByUser(user.getUsername());
            user.setRoles(roles);
        }
        return users;
    }

    public boolean isExist(String username) {
        User user = userDAO.findByUsername(username);
        return null != user;
    }

    public int findRoleIdFromUserName(String username) {
        return userDAO.roleIdFromUserName(username);
    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public User get(String username, String password) {
        return userDAO.getByUsernameAndPassword(username, password);
    }

    public void addOrUpdate(User user) {
        userDAO.save(user);
    }

    public int register(User user) {
        String username = user.getUsername();
        String name = user.getName();
        String phone = user.getPhone();
        String email = user.getEmail();
        String password = user.getPassword();

        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        phone = HtmlUtils.htmlEscape(phone);
        user.setPhone(phone);
        email = HtmlUtils.htmlEscape(email);
        user.setEmail(email);
        user.setEnabled(true);

        if (username.equals("") || password.equals("")) {
            return 0;
        }

        boolean exist = isExist(username);

        if (exist) {
            return 2;
        }

        // 默认生成 16 位盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String encodedPassword = new SimpleHash("md5", password, salt, times).toString();

        user.setSalt(salt);
        user.setPassword(encodedPassword);
        User userInDB = userDAO.save(user);
        AdminUserRole ur = new AdminUserRole();
        ur.setUid(userInDB.getId());
        ur.setRid(3);                       //默认为普通用户
        adminUserRoleDAO.save(ur);
        return 1;
    }

    public boolean updateUserStatus(User user) {
        User userInDB = userDAO.findByUsername(user.getUsername());
        userInDB.setEnabled(user.isEnabled());
        try {
            userDAO.save(userInDB);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean resetPassword(User user) {
        User userInDB = userDAO.findByUsername(user.getUsername());
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        userInDB.setSalt(salt);
        String encodedPassword = new SimpleHash("md5", "123", salt, times).toString();
        userInDB.setPassword(encodedPassword);
        try {
            userDAO.save(userInDB);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean editUser(User user) {
        User userInDB = userDAO.findByUsername(user.getUsername());
        userInDB.setName(user.getName());
        userInDB.setPhone(user.getPhone());
        userInDB.setEmail(user.getEmail());
        try {
            userDAO.save(userInDB);
            adminUserRoleService.saveRoleChanges(userInDB.getId(), user.getRoles());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean editUser(String username, String phone, String email) {
        User userInDB = userDAO.findByUsername(username);
        userInDB.setPhone(phone);
        userInDB.setEmail(email);
        try {
            userDAO.save(userInDB);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public void deleteDataByUserId(int id) {
        userDAO.deleteDataByUserId(id);
    }
}
