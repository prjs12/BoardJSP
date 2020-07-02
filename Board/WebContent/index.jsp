<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="u" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원제 게시판 예제</title>
</head>
<%
	String c = application.getInitParameter("configFile");
%>


<body>
	<u:isLogin>
		CT:${authUser.name }님, 안녕하세요
		<a href="logout.do">[로그아웃 하기]</a>
		<a href="changePwd.do">[암호 변경하기]</a>
		<a href="article/list.do">[게시판 이동]</a>
	</u:isLogin>
	<u:notLogin>
		<a href="join.do">[회원 가입 하기]</a>
		<a href="login.do">[로그인 하기]</a><br>
		<%=c %>
		<form action="login.do" method="POST">
			<input type="submit" value="index에서 POST로 바로 전송">
		</form>
	</u:notLogin>
</body>
</html>