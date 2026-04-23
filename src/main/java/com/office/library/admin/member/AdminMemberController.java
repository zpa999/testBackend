package com.office.library.admin.member;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin/member")
public class AdminMemberController {
	
	//@RequestMapping(value="/createAccountForm", method=RequestMethod.GET)
	@GetMapping("/createAccountForm")
	public String createAccountForm() {
		System.out.println("[AdminMemberController] createAccountForm()");
		String nextPage = "admin/member/create_account_form";
		return nextPage;
	}
	
	
	@Autowired
	AdminMemberService adminMemberService;
	
	//@RequestMapping(value="/createAccountConfirm", method=RequestMethod.POST)
	@PostMapping("/createAccountConfirm")
	public String createAccountConfirm(AdminMemberVo adminMemberVo) {
			System.out.println("[AdminMemberController] createAccountConfirm()");
			
			String nextPage = "admin/member/create_account_ok";
			
			int result = adminMemberService.createAccountConfirm(adminMemberVo);
			
			if(result <= 0) {
				nextPage = "admin/member/create_account_ng";
			}
			
		return nextPage;
	}

	@GetMapping("/loginForm")
	public String loginForm() {
		System.out.println("[AdminMemberController] loginForm()");
		
		String nextPage = "admin/member/login_form";
		
		return nextPage;
	}


	@PostMapping("/loginConfirm")
	public String loginConfirm(AdminMemberVo adminMemberVo, HttpSession session) {
		System.out.println("[AdminMemberController] loginConfirm()");
		
		String nextPage = "admin/member/login_ok";
		
		AdminMemberVo loginedAdminMemberVo = adminMemberService.loginConfirm(adminMemberVo);
		
		if(loginedAdminMemberVo == null) {
			nextPage = "admin/member/login_ng";
		}else{
			session.setAttribute("loginedAdminMemberVo", loginedAdminMemberVo);
			session.setMaxInactiveInterval(60*30);
		}
		
		return nextPage;
	}

	//@RequestMapping(value="/logoutConfirm", method=RequestMethod.GET)
	@GetMapping("/logoutConfirm")
	public String logoutConfirm(HttpSession session) {
		System.out.println("[AdminMemberController] logoutConfirm()");
		
		String nextPage = "redirect:/admin";// 로그아웃 후 admin 요청 발생 리다이렉트
		
		session.invalidate();
		
		return nextPage;
	}


	
}//class end