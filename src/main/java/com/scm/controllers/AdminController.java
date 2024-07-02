package com.scm.controllers;

import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.ContactService;
import com.scm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private ContactService contactService;

//    @Autowired
//    private LoggedInUsers loggedInUsers;
    @ModelAttribute
    public void addCommanData(Model model, Authentication authentication) {
        if (authentication == null) {
            return;
        }
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        model.addAttribute("user",user);
    }
    @GetMapping("/index")
    public String adminDashboard(Model model) {
        model.addAttribute("title", "Admin-Dashboard| Samrt-Contact-Manager");

        long countUser = this.userService.countAllUser();
        model.addAttribute("numberOfUser",countUser);
        long countContact = this.contactService.countContacts();
        model.addAttribute("numberOfContact",countContact);
        return "admin/admin_dashboard";
    }

    @GetMapping("/show-users")
    public String showUsers(Model model){
        List<User> users = this.userService.getAllUsers();
        model.addAttribute("users",users);
        return "admin/show-users";
    }


}
