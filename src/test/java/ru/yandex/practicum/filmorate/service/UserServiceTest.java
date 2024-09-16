package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.repository.mapper.UserRowMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {UserService.class, UserRepository.class, UserRowMapper.class})
public class UserServiceTest {
    private User user1;
    private final UserService service;

    @BeforeEach
    public void testInit() {
        // Create a valid User instance
        user1 = new User();
        user1.setEmail("user@yandex.ru");
        user1.setLogin("User1");
        user1.setName("user1");
        user1.setBirthday(LocalDate.of(2000, Month.JANUARY, 1));
    }

    @Test
    public void testAddUserReturnsUserViewWithAssignedId() {
        User addedUser1 = service.addUser(user1);
        assertEquals(user1, addedUser1);
        assertNotNull(user1.getId());
    }

    @Test
    public void testUserWithBlankNameIsAddedWithNameEqualToLogin() {
        User blankNameUser = new User();
        blankNameUser.setEmail("kesha@yandex.ru");
        blankNameUser.setLogin("Kesha");
        blankNameUser.setName(" ");
        blankNameUser.setBirthday(LocalDate.of(2000, Month.JANUARY, 1));

        User user = service.addUser(blankNameUser);

        assertFalse(user.getName().isBlank());
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void testGetUserByInvalidIdThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> service.getUserById(-1));
    }

    @Test
    public void testAddFriendLeadsToUsersBecomesFriendsWithEachOther() {
        User user2 = new User();
        user2.setEmail("user@yandex.ru");
        user2.setLogin("User2");
        user2.setName("user2");
        user2.setBirthday(LocalDate.of(2000, Month.JANUARY, 1));

        service.addUser(user1);
        service.addUser(user2);
        service.addFriend(user1.getId(), user2.getId());

        assertTrue(service.getAllUserFriends(user1.getId()).contains(user2));
        assertTrue(service.getAllUserFriends(user2.getId()).contains(user1));
    }

    @Test
    public void testGetCommonFriendsReturnSetOfOneFriendIfThreeUsersAreFriends() {
        User user2 = new User();
        user2.setEmail("user@yandex.ru");
        user2.setLogin("User2");
        user2.setName("user2");
        user2.setBirthday(LocalDate.of(2000, Month.JANUARY, 1));

        User user3 = new User();
        user3.setEmail("user@yandex.ru");
        user3.setLogin("User2");
        user3.setName("user2");
        user3.setBirthday(LocalDate.of(2000, Month.JANUARY, 1));

        service.addUser(user1);
        service.addUser(user2);
        service.addUser(user3);

        service.addFriend(user1.getId(), user2.getId());
        service.addFriend(user1.getId(), user3.getId());
        service.addFriend(user2.getId(),user3.getId());

        Set<User> expectedCommonFriends = Set.of(user3);
        assertEquals(expectedCommonFriends, service.getCommonFriends(user1.getId(), user2.getId()));
    }
}
