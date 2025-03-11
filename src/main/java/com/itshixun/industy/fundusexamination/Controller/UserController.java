package com.itshixun.industy.fundusexamination.Controller;


import com.itshixun.industy.fundusexamination.Service.UserService;
import com.itshixun.industy.fundusexamination.Utils.JwtUtil;
import com.itshixun.industy.fundusexamination.Utils.ResponseMessage;
import com.itshixun.industy.fundusexamination.Utils.ThreadLocalUtil;
import com.itshixun.industy.fundusexamination.pojo.User;
import com.itshixun.industy.fundusexamination.pojo.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController //接口方法返回对象，转换成json文本
@RequestMapping("/user")
public class
UserController {
    @Autowired
    private UserService userService;
    //注册
    @PostMapping("/register")
    public ResponseMessage<User> addUser(@Validated @RequestBody UserDto user) {
        User userNew = userService.add(user);
        return ResponseMessage.success(userNew);
    }

    //登录
    @PostMapping("/login")
    public ResponseMessage<User> login(@RequestBody UserDto user) {
        User userNew = userService.findByUserName(user);
        //判断该用户是否存在
        if(userNew == null) {
            System.out.println("用户不存在");
            return ResponseMessage.success(userNew);
        }
        //判断密码是否正确
        if (userNew.getPasswordHash().equals(user.getPasswordHash())) {
            //JWT 生成token
            Map<String,Object> claims = new HashMap<>();
            claims.put("userId", userNew.getUserId());
            claims.put("userName", userNew.getUserName());
            String token = JwtUtil.genToken(claims);

            return ResponseMessage.success(token);
        }
        System.out.println("登陆出现未知错误");
        return ResponseMessage.success(userNew);
    }
    //查询
    @GetMapping("/{userId}")
    public ResponseMessage<User> getUser(@PathVariable Integer userId) {
        User userNew = userService.getUser(userId);
        return ResponseMessage.success(userNew);
    }

    //修改
    @PutMapping
    public ResponseMessage<User> updateUser(@Validated @RequestBody UserDto user) {
        User userNew = userService.update(user);
        return ResponseMessage.success(userNew);
    }

    //删除用户
    @DeleteMapping("/{userId}")
    public ResponseMessage<User> deleteUser(@PathVariable Integer userId) {

        userService.delete(userId);
        return ResponseMessage.success();
    }

    //更新头像
    @PatchMapping
    public ResponseMessage updateAvatar(@RequestParam String avatarUrl) {
//        Map<String,Object> map = ThreadLocalUtil.get();
//        Integer userId = (Integer) map.get("userId");
//        User user = userService.getUser(userId);
//        user.setAvatarUrl(avatarurl);
        userService.updateAvatar(avatarUrl);
        return ResponseMessage.success("头像上传成功！");
    }
}

