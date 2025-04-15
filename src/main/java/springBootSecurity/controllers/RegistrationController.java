package springBootSecurity.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import springBootSecurity.services.RoleService.RoleService;
import springBootSecurity.services.UserService.UserService;
import springBootSecurity.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/formRegistration")
public class RegistrationController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public RegistrationController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String registration(ModelMap model) {
        model.addAttribute("user", new User());
        return "formRegistration";
    }

    @PostMapping("")
    public String registration(@ModelAttribute("user") @Valid User userForm, BindingResult bindingR, ModelMap model) {
        if (bindingR.hasErrors()) {
            return "formRegistration";
        }
        userForm.setRoles(roleService.getRoleById(2L));
        if (!userService.saveUser(userForm)){
            model.addAttribute("userFormError", "Username already exists");
            return "formRegistration";
        }
        return "redirect:/";
    }
}
