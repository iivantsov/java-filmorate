package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.DatabaseException;

import lombok.AllArgsConstructor;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public abstract class EntityRepository<T> {
    protected final JdbcTemplate jdbcT;
    protected final RowMapper<T> rowMapper;

    protected Optional<T> getSingleEntity(String sql, Object... params) {
        try {
            T result = jdbcT.queryForObject(sql, rowMapper, params);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    protected List<T> getMultipleEntity(String sql, Object... params) {
        return jdbcT.query(sql, rowMapper, params);
    }

    public boolean deleteEntity(String sql, long id) {
        int rowsDeleted = jdbcT.update(sql, id);
        return rowsDeleted > 0;
    }

    protected int insertEntity(String sql, Object... params) {
        PreparedStatementCreator psCreator = connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int paramIndex = 0; paramIndex < params.length; paramIndex++) {
                ps.setObject(paramIndex + 1, params[paramIndex]);
            }
            return ps;
        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcT.update(psCreator, keyHolder);
        Integer id = keyHolder.getKeyAs(Integer.class);

        if (id != null) {
            return id;
        } else {
            throw new DatabaseException("insertion error");
        }
    }

    protected void updateEntity(String sql, Object... params) {
        int rowsUpdated = jdbcT.update(sql, params);
        if (rowsUpdated == 0) {
            throw new DatabaseException("updating error");
        }
    }
}
