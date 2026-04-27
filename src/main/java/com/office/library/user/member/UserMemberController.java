package com.office.library.user.member;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user/member")
public class UserMemberController {

    @Autowired
    private UserMemberService userMemberService;

    @GetMapping("createAccountForm")
    public String createAccountForm() {
        System.out.println("[UserMemberController] createAccountForm()");

        String nextPage = "user/member/create_account_form";

        return nextPage;
    }


    @PostMapping("/createAccountConfirm")
    public String createAccount(UserMemberVo userMemberVo) {
        System.out.println("[UserMemberController] createAccountConfirm()");

        String nextPage = "user/member/create_account_ok";

        int result = userMemberService.createAccountConfirm(userMemberVo);

        if (result <= 0) {
            nextPage = "user/member/create_account_ng";
        }

        return nextPage;
    }

}
