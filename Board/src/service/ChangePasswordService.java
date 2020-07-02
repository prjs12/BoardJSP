package service;

import java.sql.Connection;
import java.sql.SQLException;

import connection.ConnectionProvider;
import dao.MemberDao;
import jdbc.JdbcUtil;
import model.Member;

public class ChangePasswordService {
	private MemberDao memberDao = new MemberDao();
	public void changePassword(String userId,String curPwd,String newPwd) {
		Connection conn=null;
		try {
			conn=ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			Member member = memberDao.SelectById(conn, userId);
			if(member == null) {
				throw new MemberNotFoundException();
			}
			if(!member.matchPassword(curPwd)) {
				throw new InvalidPasswordException();
			}
			member.changePassword(newPwd);
			memberDao.update(conn, member);
			conn.commit();
		}catch(SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		}finally {
			JdbcUtil.close(conn);
		}
	}
}
