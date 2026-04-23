package com.office.library.admin.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AdminMemberService {
	final static public int ADMIN_ACCOUNT_ALREADY_EXIST = 0;
	final static public int ADMIN_ACCOUNT_CREATE_SUCCESS = 1;
	final static public int ADMIN_ACCOUNT_CREATE_FAIL = -1;
	
	
	@Autowired
	AdminMemberDao adminMemberDao;
	
	public int createAccountConfirm(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberService] createAccountConfirm()");
		
		boolean isMember = adminMemberDao.isAdminMember(adminMemberVo.getA_m_id());
		
		if(!isMember) {
			int result = adminMemberDao.insertAdminAccount(adminMemberVo);
			
			if(result > 0) {
				return ADMIN_ACCOUNT_CREATE_SUCCESS;
			}else {
				return ADMIN_ACCOUNT_CREATE_FAIL;
			}
		}else {
			return ADMIN_ACCOUNT_ALREADY_EXIST;
		}
		
	}


	public AdminMemberVo loginConfirm(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberService] loginConfirm()");
		
		AdminMemberVo logAdminMemberVo = adminMemberDao.selectAdmin(adminMemberVo);
		
		if(logAdminMemberVo != null){
			System.out.println("[AdminMemberService] ADMIN MEMBER LOGIN SUCCESS");
		}else {
			System.out.println("[AdminMemberService] ADMIN MEMBER LOGIN FAIL");	
		}
		return logAdminMemberVo;
	}

	public List<AdminMemberVo> listupAdmin() {
		System.out.println("[AdminMemberService] listupAdmin()");
		
		return adminMemberDao.selectAdmins();
	}


	public void setAdminApproval(int a_m_mo){
		System.out.println("[AdminMemberService] setAdminApproval()");
		
		int result = adminMemberDao.updateAdminAccount(a_m_mo);
	}

	public int modifyAccountConfirm(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberService] modifyAccountConfirm()");

		return adminMemberDao.updateAdminAccount(adminMemberVo);
	}
		
	public AdminMemberVo getLoginedAdminMemberVo(int a_m_no) {
		System.out.println("[AdminMemberService] getLoginedAdminMemberVo()");

		return adminMemberDao.selectAdmin(a_m_no);
	}	
	
}
