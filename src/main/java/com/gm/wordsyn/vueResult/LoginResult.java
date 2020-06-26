package com.gm.wordsyn.vueResult;

import lombok.Data;
import org.apache.shiro.authc.UsernamePasswordToken;

@Data
public class LoginResult {
    UsernamePasswordToken usernamePasswordToken;
    int roleId;

    public LoginResult(UsernamePasswordToken usernamePasswordToken, int roleId) {
        this.usernamePasswordToken = usernamePasswordToken;
        this.roleId = roleId;
    }
}
