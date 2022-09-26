package com.demo.udema.controller;

import com.demo.udema.entity.User;
import com.demo.udema.repositoryDAO.UserRepository;
import com.demo.udema.repositoryDAO.VerificationTokenRepository;
import com.demo.udema.entity.VerificationToken;
import com.demo.udema.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

public class UserAccountController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView displayRegistration(ModelAndView modelAndView, User user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName("register");

        return modelAndView;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerUser(ModelAndView modelAndView, User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if(existingUser != null) {
            modelAndView.addObject("message", "This user already exist!");
            modelAndView.setViewName("error");
        }
        else {
            userRepository.save(user);

            VerificationToken verificationToken = new VerificationToken(user);

            verificationTokenRepository.save(verificationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail()); //? example (user.getEmailId())
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("dovile.d.git@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
                    +"http://localhost:8082/confirm-account?token="+verificationToken.getVerificationToken());

            emailSenderService.sendEmail(mailMessage);

            modelAndView.addObject("email", user.getEmail()); // example ("emailId", user.getEmailId())

            modelAndView.setViewName("successfullRegistration");

        }

        return modelAndView;
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount (ModelAndView modelAndView, @RequestParam("token")String verificationToken) {
        VerificationToken token = verificationTokenRepository.findVerificationTokenBy(verificationToken);

        if (token != null) {
            User user = userRepository.findByUsername(token.getUser().getEmail()); // example userRepository.findByEmailIdIgnoreCase(token.getUser().getEmailId())
            user.setEnabled(true);
            userRepository.save(user);
            modelAndView.setViewName("accountVerified");
        }
        else {
            modelAndView.addObject("message", "The link is invalid or broken!");
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }

    // getters and setters

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public VerificationTokenRepository getVerificationTokenRepository() {
        return verificationTokenRepository;
    }

    public void setVerificationTokenRepository(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public EmailSenderService getEmailSenderService() {
        return emailSenderService;
    }

    public void setEmailSenderService(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }
}
