package springBootSecurity.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import springBootSecurity.models.Role;
import springBootSecurity.services.UserService;
import springBootSecurity.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void userPrincipal(Principal principal, ModelMap model) {
        User user = userService.getUserByUsername(principal.getName());
        String rolesString = user.getRoles().stream().map(Role::getName).sorted().collect(Collectors.joining(", "));

        model.addAttribute("profile", user.getUsername());
        model.addAttribute("role_profile", rolesString);

        List<User> users = userService.getAllUsers();
        users.forEach(user1 -> user1.setRolesName(user1.getRoles()
                .stream().map(Role::getName).sorted().collect(Collectors.joining(","))));

        model.addAttribute("userNew", new User());
        model.addAttribute("users", users);

    }

    @PostMapping(value = "/saveUser")
    public String saveUser(@RequestParam(value = "role") String roleName, @ModelAttribute @Valid User user, ModelMap model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "AddUser"; // Вернуть обратно на форму с ошибками
        }
        user.setRoles(userService.getRoles(roleName));
        if (!userService.saveUser(user)){
            model.addAttribute("userFormError", "Username already exists");
            return "AddUser";
        }
        return "redirect:/admin/refresh";
    }

    @PostMapping(value = "/updateUser")
    public String updateUser(@RequestParam(value = "role") String roleName,
                             @RequestParam(value = "usernameOld") String usernameOld,
                             @RequestParam(value = "passwordOld") String passwordOld,
                             @ModelAttribute @Valid User user) {

        user.setRoles(userService.getRoles(roleName));

        if (!userService.updateUser(user, usernameOld, passwordOld)){
            System.out.println("Username already exists");
            return "Table";  //должно быть модальное окно ?
        }
        return "redirect:/admin/refresh";
    }

    @PostMapping(value = "/removeUserById")
    public String removeUserById(@RequestParam Long id) {
        userService.removeUserById(id);

        return "redirect:/admin/refresh";
    }

    @GetMapping(value = "/AddUserPage")
    public String index(ModelMap model) {

        model.addAttribute("user", new User());
        return "AddUser";
    }

    @GetMapping(value = "/refresh")
    public String refresh(ModelMap model) {
        model.addAttribute("users", userService.getAllUsers());
        return "Table";
    }
}
