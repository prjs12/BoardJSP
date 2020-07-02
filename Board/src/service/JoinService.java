package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import connection.ConnectionProvider;
import dao.MemberDao;
import jdbc.JdbcUtil;
import model.Member;

public class JoinService {
	private MemberDao memberDao = new MemberDao();
	
	public void join(JoinRequest joinReq) {
		Connection conn=null;
		
		
		try {
			conn=ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			Member member = memberDao.SelectById(conn, joinReq.getId());
			if(member!=null) {
				JdbcUtil.rollback(conn);
				throw new DuplicateIdException();
			}
			
			memberDao.insert(conn, new Member(joinReq.getId(),joinReq.getName(),joinReq.getPassword(),new Date()));
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		}finally {
			JdbcUtil.close(conn);
		}
		
	}
}
