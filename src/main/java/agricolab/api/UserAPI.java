package agricolab.api;


import agricolab.model.Mailing;
import agricolab.model.User;
import agricolab.security.JwtUtil;
import agricolab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/user")
@RestController
public class UserAPI {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    @Autowired
    public UserAPI(UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public boolean postUser(@RequestBody User u) {
        return userService.addUser(u);
    }

    @DeleteMapping("del/{email}")
    public void deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        System.out.println("Successful delete from user " + email);
    }

    // GET METHODS
    @GetMapping("/{email}")
    public User getUser(@PathVariable String email) {
        return userService.getUser(email);
    }

    @GetMapping("/address/{email}")
    public Mailing getMailingByUser(@PathVariable String email) throws ExecutionException, InterruptedException {
        return userService.getMailingByUser(email);

    }

    @PostMapping("/address/{email}")
    public boolean createMailing(@PathVariable String email, @RequestBody Mailing mailing) {
        return userService.createMailing(email, mailing);
    }

    @GetMapping
    public ArrayList<User> getAllUsers() {
        return userService.getAllUsers();
    }

}
