package com.demo.udema.controller;

import com.demo.udema.entity.ConfirmationToken;
import com.demo.udema.entity.User;
import com.demo.udema.repositoryDAO.ConfirmationTokenRepository;
import com.demo.udema.repositoryDAO.UserRepository;
import com.demo.udema.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private UserValidator userValidator;

//    _____________nauji @Autowired_________________
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private EmailSenderService emailSenderService;


    @GetMapping("/registration")
    public String registration(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView registration (@ModelAttribute("userForm") User userForm, BindingResult bindingResult,    ModelAndView modelAndView, User user, RedirectAttributes redirectAttributes) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
//            __________________
            modelAndView.addObject("message","This email already exists!");
            modelAndView.setViewName("error");
//            ___________________
            return modelAndView;
        }
        else {
//            userService.save(userForm);
//        _______________________kodo gabalas____________________
//            issaugo user:
            userRepository.save(user);
//            sukuria confirmationTokenObjekta
            ConfirmationToken confirmationToken = new ConfirmationToken(user);
//  issaugo confirmationToken
            confirmationTokenRepository.save(confirmationToken);
// siuncia email
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("dovdau27@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
                    + "http://localhost:8084/confirm-account?token=" + confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);
            modelAndView.addObject("email", user.getEmail());

            redirectAttributes.addFlashAttribute("message", "A verification email has been sent to your email");


            modelAndView.setViewName("redirect:/login");

        }

        return modelAndView;
    }


//        _____________________kodo gabalo pabaiga_______________________

//        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());
//        return "redirect:/adminPage";
//    }

//    ________________________kodo gabalas 2__________________________________

@RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token")String confirmationToken, RedirectAttributes redirectAttributes)
{
    ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

    if(token != null)
    {
        User user = userRepository.findByEmail(token.getUser().getEmail());
        user.setEnabled(true);
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("message", "Congratulations! Your account has been activated and email is verified! Please, login to continue. ");
        modelAndView.setViewName("redirect:/login");
    }
    else
    {
        modelAndView.addObject("message","The link is invalid or broken!");
        modelAndView.setViewName("error");
    }

    return modelAndView;
}

//    ________________________kodo gabalao 2 pabaiga____________________________

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");
        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");
        return "login";
    }

    @GetMapping("/userPage")
    public String viewDetails(@AuthenticationPrincipal UserDetails loggerUser, Model model) {
        String username = loggerUser.getUsername();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "admin-page/user-profile";
    }

    @PostMapping("/userPage/update")
    public String updateDetails(@ModelAttribute("user") User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        userValidator.validate(user, bindingResult);
        if (user.getOldPassword().equals("") && user.getNewPassword().equals("") && user.getPasswordNewConfirm().equals("")) {
            if (!(user.getOldEmail().equals("") || user.getNewEmail().equals("") || user.getEmailConfirm().equals(""))) {
                if (bindingResult.hasFieldErrors("name") || bindingResult.hasFieldErrors("surname") || bindingResult.hasFieldErrors("newEmail") || bindingResult.hasFieldErrors("emailConfirm") || bindingResult.hasFieldErrors("oldEmail")) {
                    redirectAttributes.addFlashAttribute("error", "An error occurred while saving the changes (name, surname, email)");
                    return "redirect:/userPage";
                }
                redirectAttributes.addFlashAttribute("message", "Name and Email saved successfully");
                user.setEmail(user.getNewEmail()); // Using, because in front error
                userService.saveNoPassword(user);
                return "redirect:/userPage";
            } else {
                if (bindingResult.hasFieldErrors("name") || bindingResult.hasFieldErrors("surname")) {
                    redirectAttributes.addFlashAttribute("error", "An error occurred while saving the changes (name, surname)");
                    return "redirect:/userPage";
                }
                redirectAttributes.addFlashAttribute("message", "Name saved successfully");
                userService.saveNoPassword(user);
                return "redirect:/userPage";
            }
        } else if (user.getOldEmail().equals("") || user.getNewEmail().equals("") || user.getEmailConfirm().equals("")) {
            if (!(user.getOldPassword().equals("") || user.getNewPassword().equals("") || user.getPasswordNewConfirm().equals(""))) {
                if (bindingResult.hasFieldErrors("name") || bindingResult.hasFieldErrors("surname") || bindingResult.hasFieldErrors("newPassword") || bindingResult.hasFieldErrors("passwordNewConfirm") || bindingResult.hasFieldErrors("oldPassword")) {
                    redirectAttributes.addFlashAttribute("error", "An error occurred while saving the changes (name, surname, password)");
                    return "redirect:/userPage";
                }
                user.setPassword(user.getNewPassword());
                userService.save(user);
                redirectAttributes.addFlashAttribute("message", "Name and Password saved successfully");
                return "redirect:/userPage";
            } else {
                if (bindingResult.hasFieldErrors("name") || bindingResult.hasFieldErrors("surname")) {
                    redirectAttributes.addFlashAttribute("error", "An error occurred while saving the changes (name, surname)");
                    return "redirect:/userPage";
                }
                userService.saveNoPassword(user);
                redirectAttributes.addFlashAttribute("message", "Name saved successfully");
                return "redirect:/userPage";
            }
        } else {
            if (bindingResult.hasFieldErrors("name") || bindingResult.hasFieldErrors("surname") || bindingResult.hasFieldErrors("newPassword") || bindingResult.hasFieldErrors("passwordNewConfirm") || bindingResult.hasFieldErrors("oldPassword") || bindingResult.hasFieldErrors("newEmail") || bindingResult.hasFieldErrors("emailConfirm") || bindingResult.hasFieldErrors("oldEmail")) {
                redirectAttributes.addFlashAttribute("error", "An error occurred while saving the changes (ALL)");
                return "redirect:/userPage";
            }
            user.setEmail(user.getNewEmail()); // Using, because in front error
            user.setPassword(user.getNewPassword());
            userService.save(user);
            redirectAttributes.addFlashAttribute("message", "Saved successfully");
            return "redirect:/userPage";
        }
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/adminPage")
    public String adminPage() {
        return "admin-page/index";
    }
}



