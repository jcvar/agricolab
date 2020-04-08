package agricolab.app.api;


import agricolab.app.model.User;
import agricolab.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


@RequestMapping("/api/v1/user")
@RestController
public class UserAPI {

    private final UserService userService;

    @Autowired
    public UserAPI(UserService userService) {
        this.userService = userService;
    }

    @GetMapping ("/{id}")
    public User getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    @PostMapping
    public void postUser(@RequestBody User u) {
        userService.addUser(u);
        System.out.println("Successful");
    }
    @GetMapping
    public ArrayList<User> getAllUsers(){
        return userService.getAllUsers();
    }
    @PutMapping
    public void putUser() {
    }

    @DeleteMapping
    public void deleteUser() {
    }
}
