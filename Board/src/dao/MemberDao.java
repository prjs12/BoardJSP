package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import jdbc.JdbcUtil;
import model.Member;

public class MemberDao {
	
	public Member SelectById(Connection conn , String id) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "Select * from member where memberid=?";
		Member m = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				m = new Member(rs.getString("memberid"), 
						rs.getString("name"), 
						rs.getString("password"), 
						toDate(rs.getTimestamp("regdate")));
			}
			
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
		return m;
		
	}

	private Date toDate(Timestamp date) {
		// TODO Auto-generated method stub
		return date == null? null:new Date(date.getTime());
	}
	
	public void insert(Connection conn, Member mem) {
		PreparedStatement pstmt = null;
		String sql = "insert into member(memberid,name,password,regdate) values(?,?,?,?)";
		
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, mem.getId());
			pstmt.setString(2, mem.getName());
			pstmt.setString(3, mem.getPassword());
			pstmt.setTimestamp(4, new Timestamp(mem.getRegDate().getTime()));
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void update(Connection conn , Member member) {
		try {
			String sql = "update member set name=?,password=? where memberid=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getName());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getId());
			pstmt.executeUpdate();
		} catch(SQLException e) {
			
		}
	}
	
	
}
