package com.office.library.admin.member;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class AdminMemberDao {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	public boolean isAdminMember(String a_m_id) {
		System.out.println("[AdminMemberDao] isAdminMember()");
		
		String sql = "SELECT COUNT(*) FROM tbl_admin_member WHERE a_m_id = ?";
		
		int result = jdbcTemplate.queryForObject(sql, Integer.class, a_m_id);
		
		if(result > 0) {
			return true;
		}else {
			return false;
		}
	}
	
	public int insertAdminAccount(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberDao] insertAdminAccount()");

		List<String> columns = new ArrayList<>();
		List<Object> values = new ArrayList<>();

		// 1. 컬럼과 값 리스트 초기화 및 super admin 여부 체크
		if (adminMemberVo.getA_m_id().equals("super admin")) {
			columns.add("a_m_approval");
			values.add("1");
		}

		columns.add("a_m_id");
		values.add(adminMemberVo.getA_m_id());

		columns.add("a_m_pw");
		values.add(passwordEncoder.encode(adminMemberVo.getA_m_pw()));

		columns.add("a_m_name");
		values.add(adminMemberVo.getA_m_name());

		columns.add("a_m_gender");
		values.add(adminMemberVo.getA_m_gender());

		columns.add("a_m_part");
		values.add(adminMemberVo.getA_m_part());

		columns.add("a_m_position");
		values.add(adminMemberVo.getA_m_position());

		columns.add("a_m_mail");
		values.add(adminMemberVo.getA_m_mail());

		columns.add("a_m_phone");
		values.add(adminMemberVo.getA_m_phone());

		columns.add("a_m_reg_date");
		columns.add("a_m_mod_date");

// 2. SQL 구문 생성
String columnList = String.join(", ", columns);
// VALUES 절의 ? 개수는 사용자 데이터로 채워지는 컬럼(총 9개) 개수와 일치해야 합니다.
String sql = String.format("INSERT INTO tbl_admin_member (%s) VALUES (%s, NOW(), NOW())", 
        columnList, 
        String.join(", ", java.util.Collections.nCopies(columns.size() - 2, "?")));

		System.out.println(sql);

		// 3. 파라미터 배열 변환 및 업데이트 실행
		Object[] params = values.toArray();
		
		try {
			// 1. jdbcTemplate.update 사용은 매우 안전한 방식입니다. (PreparedStatement 활용)
			int result = jdbcTemplate.update(sql, params);
			return result;
		} catch (DuplicateKeyException e) {
			// 2. 데이터 중복 예외 처리
			throw new DuplicateUserException("이미 존재하는 회원 ID 또는 이메일입니다. 중복을 확인해주세요.", e);
		} catch (DataAccessException e) {
			// 3. 데이터 관련 일반 예외 처리 (컬럼명 오타, 데이터 타입 불일치 등)
			String errorMessage = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
			// 상세 에러는 시스템 로그에 기록하고, 사용자에게는 일반적인 실패 메시지를 전달합니다.
			System.err.println("[ERROR] 데이터베이스 삽입 실패: " + errorMessage);
			throw new AccountRegistrationException("회원가입 정보가 유효하지 않거나 DB 구조에 문제가 발생했습니다. 관리자에게 문의해주세요.", e);
		} catch (Exception e) {
			// 4. 예측하지 못한 모든 예외 처리
			System.err.println("[ERROR] 알 수 없는 DB 연결 또는 쿼리 오류 발생: " + e.getMessage());
			throw new RuntimeException("회원가입 처리 중 시스템 오류가 발생했습니다. 재시도하거나 관리자에게 문의해주세요.", e);
		}
	}

	
	public AdminMemberVo selectAdmin(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberDao] selectAdmin()");
		
		String sql = "SELECT * FROM tbl_admin_member WHERE a_m_id = ? AND a_m_approval > 0";
		
		List<AdminMemberVo> adminMemberVos = new ArrayList<AdminMemberVo>();

		try {
			adminMemberVos = jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() {
				@Override
				public AdminMemberVo mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
					AdminMemberVo adminMemberVo = new AdminMemberVo();
					adminMemberVo.setA_m_no(rs.getInt("a_m_no"));
					adminMemberVo.setA_m_approval(rs.getInt("a_m_approval"));
					adminMemberVo.setA_m_id(rs.getString("a_m_id"));
					adminMemberVo.setA_m_pw(rs.getString("a_m_pw"));
					adminMemberVo.setA_m_name(rs.getString("a_m_name"));
					adminMemberVo.setA_m_gender(rs.getString("a_m_gender"));
					adminMemberVo.setA_m_part(rs.getString("a_m_part"));
					adminMemberVo.setA_m_position(rs.getString("a_m_position"));
					adminMemberVo.setA_m_mail(rs.getString("a_m_mail"));
					adminMemberVo.setA_m_phone(rs.getString("a_m_phone"));
					adminMemberVo.setA_m_reg_date(rs.getString("a_m_reg_date"));
					adminMemberVo.setA_m_mod_date(rs.getString("a_m_mod_date"));
					return adminMemberVo;
				}
			}, adminMemberVo.getA_m_id());

			// 암호화된 문자열을 비교하는 메서드
			if (!passwordEncoder.matches(adminMemberVo.getA_m_pw(),
					adminMemberVos.get(0).getA_m_pw())) {
						adminMemberVos.clear();
						
			} 
		} catch (Exception e) {
			e.printStackTrace();
			// 예외 발생 시 빈 리스트를 반환하여 로그인 실패로 처리합니다.
		}
		return adminMemberVos.size() > 0 ? adminMemberVos.get(0) : null;
	}

	public List<AdminMemberVo> selectAdmins() {
		System.out.println("[AdminMemberDao] selectAdmins()");
		
		String sql = "SELECT * FROM tbl_admin_member";
		
		List<AdminMemberVo> adminMemberVos = new ArrayList<AdminMemberVo>();

		try {
			adminMemberVos = jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() {
				@Override
				public AdminMemberVo mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
					AdminMemberVo adminMemberVo = new AdminMemberVo();
					adminMemberVo.setA_m_no(rs.getInt("a_m_no"));
					adminMemberVo.setA_m_approval(rs.getInt("a_m_approval"));
					adminMemberVo.setA_m_id(rs.getString("a_m_id"));
					adminMemberVo.setA_m_pw(rs.getString("a_m_pw"));
					adminMemberVo.setA_m_name(rs.getString("a_m_name"));
					adminMemberVo.setA_m_gender(rs.getString("a_m_gender"));
					adminMemberVo.setA_m_part(rs.getString("a_m_part"));
					adminMemberVo.setA_m_position(rs.getString("a_m_position"));
					adminMemberVo.setA_m_mail(rs.getString("a_m_mail"));
					adminMemberVo.setA_m_phone(rs.getString("a_m_phone"));
					adminMemberVo.setA_m_reg_date(rs.getString("a_m_reg_date"));
					adminMemberVo.setA_m_mod_date(rs.getString("a_m_mod_date"));
					return adminMemberVo;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return adminMemberVos;
	}

	public int updateAdminAccount(int a_m_mo) {
		System.out.println("[AdminMemberDao] updateAdminAccount()");
		
		String sql = "UPDATE tbl_admin_member SET a_m_approval = 1 WHERE a_m_no = ?";
		
		int result = -1;

		try {
			result = jdbcTemplate.update(sql, a_m_mo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public int updateAdminAccount(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberDao] updateAdminAccount()");
		
		String sql = "UPDATE tbl_admin_member SET a_m_name = ?, a_m_gender = ?, a_m_part = ?, a_m_position = ?, a_m_mail = ?, a_m_phone = ?, a_m_mod_date = NOW() WHERE a_m_no = ?";
		int result = -1;

		try {
			result = jdbcTemplate.update(sql, adminMemberVo.getA_m_name(),
			adminMemberVo.getA_m_gender(), adminMemberVo.getA_m_part(),
			adminMemberVo.getA_m_position(), adminMemberVo.getA_m_mail(),
			adminMemberVo.getA_m_phone(), adminMemberVo.getA_m_no());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public AdminMemberVo selectAdmin(int a_m_no) {
		System.out.println("[AdminMemberDao] selectAdmin()");
		
		String sql = "SELECT * FROM tbl_admin_member WHERE a_m_no = ?";
		
		List<AdminMemberVo> adminMemberVos = new ArrayList<AdminMemberVo>();

		try {
			adminMemberVos = jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() {
				@Override
				public AdminMemberVo mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
					AdminMemberVo adminMemberVo = new AdminMemberVo();
					adminMemberVo.setA_m_no(rs.getInt("a_m_no"));
					adminMemberVo.setA_m_approval(rs.getInt("a_m_approval"));
					adminMemberVo.setA_m_id(rs.getString("a_m_id"));
					adminMemberVo.setA_m_pw(rs.getString("a_m_pw"));
					adminMemberVo.setA_m_name(rs.getString("a_m_name"));
					adminMemberVo.setA_m_gender(rs.getString("a_m_gender"));
					adminMemberVo.setA_m_part(rs.getString("a_m_part"));
					adminMemberVo.setA_m_position(rs.getString("a_m_position"));
					adminMemberVo.setA_m_mail(rs.getString("a_m_mail"));
					adminMemberVo.setA_m_phone(rs.getString("a_m_phone"));
					adminMemberVo.setA_m_reg_date(rs.getString("a_m_reg_date"));
					adminMemberVo.setA_m_mod_date(rs.getString("a_m_mod_date"));
					return adminMemberVo;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return adminMemberVos.size() > 0 ? adminMemberVos.get(0) : null;
	}
	

	
}