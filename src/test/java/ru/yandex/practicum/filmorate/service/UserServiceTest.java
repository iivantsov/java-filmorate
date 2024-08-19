package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private User user1;
    private UserService service;

    @BeforeEach
    public void testInit() {
        UserStorage storage = new InMemoryUserStorage();
        service = new UserService(storage);

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
    public void testAddFriendLeadsToUsersBecomesFriendsWithEachOther() {
        User user2 = new User();
        user2.setEmail("user@yandex.ru");
        user2.setLogin("User2");
        user2.setName("user2");
        user2.setBirthday(LocalDate.of(2000, Month.JANUARY, 1));

        service.addUser(user1);
        service.addUser(user2);
        service.addFriend(user1.getId(), user2.getId());

        assertTrue(user1.getFriends().contains(user2.getId()));
        assertTrue(user2.getFriends().contains(user1.getId()));
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
