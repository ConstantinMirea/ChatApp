package com.fvsware;

import com.fvsware.data.Message;
import com.fvsware.data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@RestController
@RequestMapping(path = "api")
public class RestEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestEndpoint.class);

    private List<User> allUsers = new ArrayList<>();
    private List<User> chatroomUsers = new ArrayList<>();
    private List<Message> msges = new ArrayList<>();

    public RestEndpoint() {
        User user = new User("qosteen", "qosteen");
        allUsers.add(user);
        user = new User("floreen", "floreen");
        allUsers.add(user);
        user = new User("mareen", "mareen");
        allUsers.add(user);
    }

    @GetMapping(path = "currentUser")
    public String getCurrentUser(@SessionAttribute(name = "currentUser", required = false) User currentUser) {
        if (currentUser != null) {
            return currentUser.getUsername();
        }
        return null;
    }

    @PostMapping(path = "login")
    public String login(String username, String password, HttpSession httpSession) {

        LOGGER.info("Attempted login {}, {}", username, password);
        for (User user : allUsers) {
            if (user.getUsername().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
                httpSession.setAttribute("currentUser", user);
                chatroomUsers.add(user);
                return "Login Successful";
            }
        }
        return "Wrong username or password";
    }

    @PostMapping(path = "logout")
    public void logout(HttpSession session) {
        session.setAttribute("currentUser", null);
    }

    @PostMapping(path = "send")
    public List<Message> msghandler(@SessionAttribute(name = "currentUser") User currentUser, @RequestBody String message) {
        System.out.println(message);
        Message message1 = new Message();
        message1.setUser(currentUser);
        message1.setContent(message);
        msges.add(message1);
        //Aici DB bro
        return msges;

    }

    @GetMapping(path = "messages")
    public List<Message> msghandler(@SessionAttribute(name = "currentUser") User currentUser) {
        return msges;
    }

    @GetMapping(path = "persons")
    public Set<String> personshandler(@SessionAttribute(name = "currentUser") User currentUser) {
        Set<String> usernames = new TreeSet<>();
        for (User user : chatroomUsers) {
            usernames.add(user.getUsername());
        }
        return usernames;
    }

}
