package article.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import article.model.Article;
import article.model.Writer;
import jdbc.JdbcUtil;

public class ArticleDao {
	public Article insert(Connection conn , Article article) throws SQLException {
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "insert into article(article_no,writer_id,writer_name,title,regdate,moddate,read_cnt) values(article_seq.nextval,?,?,?,?,?,0)"; 
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, article.getWriter().getId());
			pstmt.setString(2, article.getWriter().getName());
			pstmt.setString(3, article.getTitle());
			pstmt.setTimestamp(4, toTimestamp(article.getRegDate()));
			pstmt.setTimestamp(5, toTimestamp(article.getModifiedDate()));
			int insertedCount = pstmt.executeUpdate();
			pstmt.close();
//			System.out.println(insertedCount);
			if(insertedCount>0) {
				
				stmt = conn.createStatement();
				rs = stmt.executeQuery("select article_seq.currval from dual");
				Integer newNum = 0;
				if(rs.next()) {
					newNum = rs.getInt(1);
				}
				pstmt = conn.prepareStatement("select * from article where article_no = ?");
				pstmt.setInt(1, newNum);
				rs = pstmt.executeQuery(); // 지금 들어간 article_no의 숫자를 가져올수있게
				if(rs.next()) {
					return new Article(newNum,article.getWriter(),article.getTitle(),article.getRegDate(),article.getModifiedDate(),0);	
				}
			}
			return null;	
		}finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
			JdbcUtil.close(pstmt);
		}
	}
	private Timestamp toTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}
	
	public int selectCount(Connection conn) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt=conn.createStatement();
			rs=stmt.executeQuery("select count(*) from article");
			if(rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		}finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
		
		}
	}
	
	public List<Article> select(Connection conn,int startRow, int size) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select * from article order by article_no desc"; // 게시판 번호순정렬
			pstmt=conn.prepareStatement(sql);
//			pstmt.setInt(1, startRow);
//			pstmt.setInt(2, size);
			
			rs=pstmt.executeQuery();
			List<Article> result = new ArrayList<Article>();
			while(rs.next()) {
				result.add(converArticle(rs));
			}
			return result;
		}finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
	private Article converArticle(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return new Article(rs.getInt("article_no"),new Writer(rs.getString("writer_id"),rs.getString("writer_name")),
				rs.getString("title"),toDate(rs.getTimestamp("regdate")),toDate(rs.getTimestamp("moddate")),rs.getInt("read_cnt"));
	}
	private Date toDate(Timestamp timestamp) {
		// TODO Auto-generated method stub
		return new Date(timestamp.getTime());
	}
	
	public Article selectById(Connection conn , int no) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql= "select * from article where article_no=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			rs=pstmt.executeQuery();
			Article article = null;
			if(rs.next()) {
				article = converArticle(rs);
			}
			return article;
		}finally {
			JdbcUtil.close(pstmt);
			JdbcUtil.close(rs);
		}
	}
	
	public void increaseReadCount(Connection conn , int no) throws SQLException {
		String sql = "update article set read_cnt = read_cnt +1 where article_no=?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, no);
			pstmt.executeUpdate();
		}
	}
	
	public int update(Connection conn , int no , String title) {
		try {
			String sql = "update article set title=?, moddate= sysdate where article_no =?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setInt(2, no);
			return pstmt.executeUpdate();
		}catch(SQLException e) {
			return 0;
		}
	
	
	}
}
