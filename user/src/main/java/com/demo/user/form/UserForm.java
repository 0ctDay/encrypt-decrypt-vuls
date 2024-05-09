package com.demo.user.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: rosh
 * @Date: 2021/10/25 23:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserForm {

    private String username;

    private String password;

}
