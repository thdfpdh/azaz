<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
   <h2>로그인</h2>
   <form action="/ehr/anno/doSelectOne.do" method = "post">
    <label for="">아이디</label>
    <input type="text" name="userId" id = "userid">
    <label for="password">비밀번호</label>
    <input type="password" name="password" id="password">
    <input type="submit" value="로그인">
   
   </form>
   
   userId:${userId }<br/>
   password:${password }
</body>
</html>