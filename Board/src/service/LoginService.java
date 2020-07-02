package service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import connection.ConnectionProvider;
import dao.MemberDao;
import model.Member;

public class LoginService {
	private MemberDao memberDao = new MemberDao();
	public User login(String id, String password) {
		try(Connection conn = ConnectionProvider.getConnection()){
			Member member = memberDao.SelectById(conn, id);
			if(member == null) {
				throw new LoginFailException();
			}
			if(!member.matchPassword(password)) {
				throw new LoginFailException();
			}
			return new User(member.getId(),member.getName());
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
