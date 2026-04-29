package com.office.library.user.member;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Service
public class UserMemberService {

    final static public int USER_ACCOUNT_ALREADY_EXIST = 0;
    final static public int USER_ACCOUNT_CREATE_SUCCESS = 1;
    final static public int USER_ACCOUNT_CREATE_FAIL = -1;

    @Autowired
    private UserMemberDao userMemberDao;

    public int createAccountConfirm(UserMemberVo userMemberVo) {
        System.out.println("[UserMemberService] createAccountConfirm()");

        boolean isMember = userMemberDao.isUserMember(userMemberVo.getU_m_id());
    
        if(!isMember){
            int result = userMemberDao.insertUserAccount(userMemberVo);

            if(result > 0){
                return USER_ACCOUNT_CREATE_SUCCESS;
            } else {
                return USER_ACCOUNT_CREATE_FAIL;
            }
        } else {
            return USER_ACCOUNT_ALREADY_EXIST;
        }
    }

    public UserMemberVo loginConfirm(UserMemberVo userMemberVo) {
        System.out.println("[UserMemberService] loginConfirm()");

        UserMemberVo loginedUserMemberVo = userMemberDao.selectUser(userMemberVo);

        if(loginedUserMemberVo != null){
            System.out.println("[UserMemberService] USER MEMBER LOGIN SUCCESS");
        }else{
            System.out.println("[UserMemberService] USER MEMBER LOGIN FAIL");
        }

        return loginedUserMemberVo;
        }


        public int modifyAccountConfirm(UserMemberVo userMemberVo) {
            System.out.println("[UserMemberService] modifyAccountConfirm()");

            return userMemberDao.updateUserAccount(userMemberVo);
        }

        public UserMemberVo getLoginedUserMemberVo(int u_m_no) {
            System.out.println("[UserMemberService] getLoginedUserMemberVo()");

            return userMemberDao.selectUser(u_m_no);
        }

        

}
