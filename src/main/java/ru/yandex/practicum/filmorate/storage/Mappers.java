package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class Mappers {

    final RowMapper<User> userMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.builder()
                    .id(rs.getInt("user_id"))
                    .login(rs.getString("login"))
                    .name(rs.getString("name"))
                    .birthday(rs.getDate("birthday").toLocalDate())
                    .email(rs.getString("email"))
                    .build();
        }
    };

    final RowMapper<Friend> friendMapper = new RowMapper<Friend>() {
        @Override
        public Friend mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Friend.builder()
                    .friendId(rs.getInt("friend_id"))
                    .userId(rs.getInt("user_id"))
                    .build();
        }
    };

    final RowMapper<Film> filmMapper = new RowMapper<Film>() {
        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Film.builder()
                    .id(rs.getInt("film_id"))
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .releaseDate(rs.getDate("release_date").toLocalDate())
                    .duration(rs.getInt("duration"))
                    .build();
        }
    };

    final RowMapper<FilmGenre> filmGenresMapper = new RowMapper<FilmGenre>() {
        @Override
        public FilmGenre mapRow(ResultSet rs, int rowNum) throws SQLException {
            return FilmGenre.builder()
                    .filmId(rs.getInt("film_id"))
                    .genreId(rs.getInt("genre_id"))
                    .build();
        }
    };

    final RowMapper<Genre> genreMapper = new RowMapper<Genre>() {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Genre.builder()
                    .id(rs.getInt("genre_id"))
                    .name(rs.getString("name"))
                    .build();
        }
    };

    final RowMapper<FilmMpa> filmMpaMapper = new RowMapper<FilmMpa>() {
        @Override
        public FilmMpa mapRow(ResultSet rs, int rowNum) throws SQLException {
            return FilmMpa.builder()
                    .filmId(rs.getInt("film_id"))
                    .mpaId(rs.getInt("mpa_id"))
                    .build();
        }
    };

    final RowMapper<Mpa> mpaMapper = new RowMapper<Mpa>() {
        @Override
        public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Mpa.builder()
                    .id(rs.getInt("mpa_id"))
                    .name(rs.getString("name"))
                    .build();
        }
    };
}