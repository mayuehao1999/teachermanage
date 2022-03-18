package cn.linter.oasys.controller;

import cn.linter.oasys.entity.Response;
import cn.linter.oasys.entity.User;
import cn.linter.oasys.service.UserService;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/auth")
    public Response<User> getAuthentication(@AuthenticationPrincipal User user) {
        user.setPassword(null);
        return Response.success("获取成功！", user);
    }

    @GetMapping("/user/{id}")
    public Response<User> getUser(@PathVariable("id") int id) {
        User user = userService.getUserById(id);
        if (user.getId() == 0) {
            return Response.error("此用户不存在！");
        }
        return Response.success("获取成功！", user);
    }

    @PreAuthorize("hasRole('校长')")
    @GetMapping("/getUsers")
    public Response<PageInfo<User>> getUsers(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                                             @RequestParam(value = "pageSize", defaultValue = "8") int pageSize) {

        System.out.println("执行了...");
        System.out.println("跳转到这个页数"+pageNumber);

        PageInfo<User> pageInfo = userService.getUsers(pageNumber, pageSize);
        return Response.success("获取成功！", pageInfo);
    }

    @PreAuthorize("hasRole('校长')")
    @PostMapping("/updateUser")
    public Response<?> updateUser(@RequestBody User user) {

        int result = userService.updateUser(user);
        if (result == -1) {
            return Response.error("用户名已存在！");
        }
        return Response.success("更新成功！");
    }

    @PreAuthorize("hasRole('校长')")
    @PostMapping("/addUser")
    public Response<?> addUser(@RequestBody User user) {

        int result = userService.addUser(user);
        if (result == -1) {
            return Response.error("用户名已存在！");
        }
        return Response.success("添加成功！");
    }

    @PreAuthorize("hasRole('校长')")
    @PostMapping("/deleteUser")
    public Response<?> deleteUser(@RequestBody Integer[] ids) {
        userService.deleteUser(ids);
        return Response.success("删除成功！");
    }

    /**
     * 修改密码
      */
    @PostMapping("/updatePassword")
    public Response<?> updatePassword(@RequestBody User user) {

        System.out.println("执行了...");
        int result = userService.updatePassword(user);
        return Response.success("修改密码成功！");
    }

    /**
     * 查询个人信息
     */
    @PostMapping("/findUser")
    public Response<?> findUser(@RequestBody User user1) {

        System.out.println("一致性了");
        System.out.println(user1.getUsername());

        User user=userService.findUser(user1);

        return Response.success("获取成功！", user);
    }





}
