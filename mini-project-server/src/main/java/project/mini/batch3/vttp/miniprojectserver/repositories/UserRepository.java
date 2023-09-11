package project.mini.batch3.vttp.miniprojectserver.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import project.mini.batch3.vttp.miniprojectserver.models.User;

@Repository
public class UserRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String queryUserIdSql = "select * from app_user where user_id = ?;";
    private final String queryUsernameSql = "select * from app_user where username = ?;";
    private final String insertUserSql = "insert into app_user (user_id, username, enc_password) values (?, ?, ?);";

    public Optional<List<User>> getUserById(Integer id) {
        return Optional.ofNullable(jdbcTemplate.query(queryUserIdSql, new BeanPropertyRowMapper<User>(User.class), id));
    }

    public Optional<List<User>> getUserByUsername(String username) {
        return Optional.ofNullable(jdbcTemplate.query(queryUsernameSql, new BeanPropertyRowMapper<User>(User.class), username));
    }

    public Integer insertNewUser(User user) {
        return jdbcTemplate.update(insertUserSql, user.getId(), user.getUsername(), user.getPassword());
    }
}
