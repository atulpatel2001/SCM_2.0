package com.scm.controllers;

import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.ContactService;
import com.scm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
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
        model.addAttribute("title", "Admin-Dashboard| Smart-Contact-Manager");

        long countUser = this.userService.countAllUser();
        model.addAttribute("numberOfUser",countUser);
        long countContact = this.contactService.countContacts();
        model.addAttribute("numberOfContact",countContact);
        return "admin/index";
    }

    @GetMapping("/users")
    public String showUsers(Model model){
        //Pageable pageable = PageRequest.of(page, 5);
        List<User> users = this.userService.findAllUsers();
        model.addAttribute("users", users);
//        model.addAttribute("currentPage", pageResult.getNumber()); // Page number is zero-based, so we add 1
//        model.addAttribute("totalPages", pageResult.getTotalPages());
        return "user/users";
    }





}
