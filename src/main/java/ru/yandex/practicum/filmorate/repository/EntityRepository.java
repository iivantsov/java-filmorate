package ru.yandex.practicum.filmorate.repository;

import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import lombok.AllArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.Statement;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public abstract class EntityRepository<T> {
    protected final JdbcTemplate jdbcT;
    protected final RowMapper<T> rowMapper;

    protected Optional<T> getSingleEntity(String sql, Object... params) {
        T result = jdbcT.queryForObject(sql, rowMapper, params);
        return Optional.ofNullable(result);
    }

    protected List<T> getMultipleEntity(String sql, Object... params) {
        return jdbcT.query(sql, rowMapper, params);
    }

    protected Optional<Integer> insertEntity(String sql, Object... params) {
        PreparedStatementCreator psCreator = connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int paramIndex = 0; paramIndex < params.length; paramIndex++) {
                ps.setObject(paramIndex + 1, params[paramIndex]);
            }
            return ps;
        };
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcT.update(psCreator, keyHolder);

        return Optional.ofNullable(keyHolder.getKeyAs(Integer.class));
    }

    protected int updateEntity(String sql, Object... params) {
        return jdbcT.update(sql, params);
    }
}
