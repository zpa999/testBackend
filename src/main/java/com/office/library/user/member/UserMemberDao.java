package com.office.library.user.member;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.office.library.admin.member.AdminMemberVo;

@Component
public class UserMemberDao {

    @Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public boolean isUserMember(String u_m_id) {
		System.out.println("[UserMemberDao] isUserMember()");
		
		String sql =  "SELECT COUNT(*) FROM tbl_user_member WHERE u_m_id = ?";
		
		int result = jdbcTemplate.queryForObject(sql, Integer.class, u_m_id);
		
		return result > 0 ? true : false;
		
	}

	public int insertUserAccount(UserMemberVo userMemberVo) {
		System.out.println("[UserMemberDao] insertUserAccount()");
		
		String sql = "INSERT INTO tbl_user_member(u_m_id, "
											   + "u_m_pw, "
											   + "u_m_name, "
											   + "u_m_gender, "
											   + "u_m_mail, "
											   + "u_m_phone, "
											   + "u_m_reg_date, "
											   + "u_m_mod_date) VALUES(?, ?, ?, ?, ?, ?, NOW(), NOW())";
		
		int result = -1;
		
		try {
			
			result = jdbcTemplate.update(sql, 
											 userMemberVo.getU_m_id(), 
											 passwordEncoder.encode(userMemberVo.getU_m_pw()), 
											 userMemberVo.getU_m_name(), 
											 userMemberVo.getU_m_gender(), 
											 userMemberVo.getU_m_mail(), 
											 userMemberVo.getU_m_phone());
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return result;
		
	}

	public UserMemberVo selectUser(UserMemberVo userMemberVo) {
		System.out.println("[UserMemberDao] selectUser()");
		
		String sql = "SELECT * FROM tbl_user_member WHERE u_m_id = ?";

		List<UserMemberVo> userMemberVos = new ArrayList<UserMemberVo>();

		try {
			userMemberVos = jdbcTemplate.query(sql, new RowMapper<UserMemberVo>() {
				@Override
				public UserMemberVo mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
					UserMemberVo userMemberVo = new UserMemberVo();
					userMemberVo.setU_m_no(rs.getInt("u_m_no"));
					userMemberVo.setU_m_id(rs.getString("u_m_id"));
					userMemberVo.setU_m_pw(rs.getString("u_m_pw"));
					userMemberVo.setU_m_name(rs.getString("u_m_name"));
					userMemberVo.setU_m_gender(rs.getString("u_m_gender"));
					userMemberVo.setU_m_mail(rs.getString("u_m_mail"));
					userMemberVo.setU_m_phone(rs.getString("u_m_phone"));
					userMemberVo.setU_m_reg_date(rs.getString("u_m_reg_date"));
					userMemberVo.setU_m_mod_date(rs.getString("u_m_mod_date"));
					return userMemberVo;
				}
			}, userMemberVo.getU_m_id());


			// 암호화된 문자열을 비교하는 메서드
			if (!passwordEncoder.matches(userMemberVo.getU_m_pw(),
					userMemberVos.get(0).getU_m_pw())) {
						userMemberVos.clear();	
			} 
		} catch (Exception e) {
			e.printStackTrace();
			// 예외 발생 시 빈 리스트를 반환하여 로그인 실패로 처리합니다.
		}

		return userMemberVos.size() > 0 ? userMemberVos.get(0) : null;
	}

}
