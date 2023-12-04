package com.csis3275;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.csis3275.models.UserModel;
import com.csis3275.services.PasswordUtils;
import com.csis3275.services.UserImpl;

@SpringBootTest
class UserTest {

    @Autowired
    private UserImpl userService;

    @Test
    void testSuccessfulSignIn() {
        String email = "test@example.com";
        String password = "password";
        UserModel user = new UserModel("Test User", email, password);

        userService.addUser(user);

        UserModel fetchedUser = userService.getUserByEmail(email);

        assertEquals("Test User", fetchedUser.getName());
        assertEquals(email, fetchedUser.getEmail());
        assertTrue(PasswordUtils.verifyPassword(password, fetchedUser.getPassword()));
        assertTrue(userService.authorizeUser(email, password));
        
        userService.deleteUser(email);
    }
    
    @Test
    void testUnsuccessfulSignIn() {
        String email = "test@example.com";
        String password = "password";
        String incorrectPassowrd = "qweqweqwe";
        UserModel user = new UserModel("Test User", email, password);

        userService.addUser(user);

        UserModel fetchedUser = userService.getUserByEmail(email);

        assertEquals("Test User", fetchedUser.getName());
        assertEquals(email, fetchedUser.getEmail());
        assertFalse(PasswordUtils.verifyPassword(incorrectPassowrd, fetchedUser.getPassword()));
        assertFalse(userService.authorizeUser(email, incorrectPassowrd));
        
        userService.deleteUser(email);
    }
    
    @Test
    void testCreatingAUserWithTheSameEmail() {
    	// Can't have 2 users with the same email
    	
        String email = "test2@example.com";
        UserModel user = new UserModel("Test User", email, "password");
        UserModel user2 = new UserModel("Test User", email, "password");
        userService.addUser(user);
        
        UserModel fetchedUser = userService.getUserByEmail(email);
        assertEquals(email, fetchedUser.getEmail());
        
        userService.addUser(user2);
        
        UserModel fetchedUser2 = userService.getUserByEmail(email);
        assertEquals(email, fetchedUser2.getEmail());
    }
    
    @Test
    void testDeletingBeforeCreatingNewUser() {
        String email = "test3@example.com";
        UserModel user = new UserModel("Test User", email, "password");
        userService.addUser(user);
        
        UserModel fetchedUser = userService.getUserByEmail(email);
        assertEquals(email, fetchedUser.getEmail());
        
        userService.deleteUser(email);
        assertEquals(fetchedUser.getEmail(), null);
        UserModel user2 = new UserModel("Test User", email, "password");
        userService.addUser(user2);
        fetchedUser = userService.getUserByEmail(email);
        assertEquals(email, fetchedUser.getEmail());
        
        userService.deleteUser(email);
    }
   
}
