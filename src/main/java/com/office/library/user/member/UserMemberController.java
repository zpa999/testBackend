package com.office.library.user.member;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
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

    @GetMapping("/loginForm")
    public String loginForm() {
        System.out.println("[UserMemberController] loginForm()");

        String nextPage = "user/member/login_form";

        return nextPage;
    }

    @PostMapping("loginConfirm")
    public String loginConfirm(UserMemberVo userMemberVo, HttpSession session) {
        System.out.println("[UserMemberController] loginConfirm()");

        String nextPage = "user/member/login_ok";

        UserMemberVo loginedUserMemberVo =
                userMemberService.loginConfirm(userMemberVo);

        if (loginedUserMemberVo == null) {
            nextPage = "user/member/login_ng";

        } else {
            session.setAttribute("loginedUserMemberVo", loginedUserMemberVo);
            session.setMaxInactiveInterval(60 * 30);
        }


        return nextPage;
    }

    @GetMapping("/modifyAccountForm")
    public String modifyAccountForm(HttpSession session) {
        System.out.println("[UserMemberController] modifyAccountForm()");

        String nextPage = "user/member/modify_account_form";

        UserMemberVo loginedUserMemberVo =
                (UserMemberVo) session.getAttribute("loginedUserMemberVo");

        if (loginedUserMemberVo == null){
            return "redirect:/user/member/loginForm";
        }

        return nextPage;
    }

    @PostMapping("/modifyAccountConfirm")
    public String modifyAccountConfirm(UserMemberVo userMemberVo, HttpSession session) {
        System.out.println("[UserMemberController] modifyAccountConfirm()");

        String nextPage = "user/member/modify_account_ok";

        int result = userMemberService.modifyAccountConfirm(userMemberVo);

        if(result > 0){
            UserMemberVo loginedUserMemberVo =
            userMemberService.getLoginedUserMemberVo(userMemberVo.getU_m_no());
            
            session.setAttribute("loginedUserMemberVo", loginedUserMemberVo);
            session.setMaxInactiveInterval(60*30);
        } else {
            nextPage = "user/member/modify_account_ng";
        }

        return nextPage;
    }

    @GetMapping("/logoutConfirm")
    public String logout(HttpSession session) {
        System.out.println("[UserMemberController] logoutConfirm()");

        String nextPage = "redirect:/";

        session.invalidate();

        return nextPage;
    }

}
