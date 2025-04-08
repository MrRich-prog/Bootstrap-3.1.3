package springBootSecurity.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import springBootSecurity.models.Role;
import springBootSecurity.service.UserService;
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
public class AdminPage {

    private final UserService userService;

    @Autowired
    public AdminPage(UserService userService) {
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
    public String updateUser(@RequestParam(value = "role") String roleName, @ModelAttribute @Valid User user, ModelMap model/*, BindingResult bindingResult*/) {
//        if (bindingResult.hasErrors()) {
//            return "UpdateUser"; //должно быть модальное окно
//        }
        user.setRoles(userService.getRoles(roleName));
        userService.updateUser(user);

//        if (!userService.updateUser(user)){
//            model.addAttribute("userFormError", "Username already exists");
//            return "UpdateUser";  //должно быть модальное окно
//        }
        return "redirect:/admin/refresh";
    }

    @PostMapping(value = "/removeUserById")
    public String removeUserById(@RequestParam Long id, ModelMap model) {
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
//      не используется
//    @PostMapping(value = "/cleanUserTable")
//    public String cleanUserTable(ModelMap model) {
//        userService.cleanUsersTable();
//        List<User> users = userService.getAllUsers();
//        model.addAttribute("users", users);
//        return "Table";
//    }
}
