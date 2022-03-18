package cn.linter.oasys.service;

import cn.linter.oasys.entity.Role;
import cn.linter.oasys.entity.User;
import cn.linter.oasys.mapper.RoleMapper;
import cn.linter.oasys.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final RoleMapper roleMapper;

    private final BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserMapper userMapper, RoleMapper roleMapper, BCryptPasswordEncoder encoder) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.encoder = encoder;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("找不到用户名为: " + username + "的用户");
        }
        return user;
    }

    @Override
    public User getUserById(int id) {
        return userMapper.selectUserById(id);
    }

    @Override
    public PageInfo<User> getUsers(int pageNumber, int pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        return new PageInfo<>(userMapper.selectUsers());
    }

    @Override
    public int updateUser(User user) {
        User u = userMapper.selectUserByUsername(user.getUsername());
        if (u != null && u.getId() != user.getId()) {
            return -1;
        }
        if (user.getPassword() != null) {
            user.setPassword(encoder.encode(user.getPassword()));
        }
        return userMapper.updateUser(user);
    }

    @Override
    public int addUser(User user) {
        if (userMapper.selectUserByUsername(user.getUsername()) != null) {
            return -1;
        }
        Role role = new Role();
        role.setId(1);
        user.setRole(role);
        user.setAvatar("/img/avatar/default.jpg");

        user.setPassword(encoder.encode(user.getPassword()));
        int result = userMapper.insertUser(user);
        return result;
    }

    @Override
    public void deleteUser(Integer[] ids) {
        userMapper.deleteUser(ids);
    }

    @Override
    public int updatePassword(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userMapper.updatePassword(user);
    }

    @Override
    public User findUser(User user1) {
        return userMapper.findUser(user1);
    }
}
