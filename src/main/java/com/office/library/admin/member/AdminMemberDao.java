package com.office.library.admin.member;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class AdminMemberDao {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
	
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
		values.add(adminMemberVo.getA_m_pw());

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
	
}