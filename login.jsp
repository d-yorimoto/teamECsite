<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="./css/table.css">
<title>ログイン</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	<div id="main">
		<h1>ログイン画面</h1>
		<s:if
			test="userIdErrorMessageList != null && userIdErrorMessageList.size() > 0">
			<div class="error">
				<div class="error_message">
					<s:iterator value="userIdErrorMessageList">
						<s:property />
						<br>
					</s:iterator>
				</div>
			</div>
		</s:if>
		<s:if
			test="passwordErrorMessageList != null && passwordErrorMessageList.size() > 0">
			<div class="error">
				<div class="error_message">
					<s:iterator value="passwordErrorMessageList">
						<s:property />
						<br>
					</s:iterator>
				</div>
			</div>
		</s:if>
		<s:if test="errorMessage != null && !errorMessage.isEmpty()">
			<div class="error">
				<div class="error_message">
					<s:property value="errorMessage" />
				</div>
			</div>
		</s:if>
		<s:form action="LoginAction">
			<table class="table">
				<tr>
					<th scope="row"><label>ユーザーID</label></th>
					<s:if test="#session.saveUserId==true">
						<td><s:textfield name="userId" class="txt"
								placeholder="ユーザーID" value="%{#session.loginUserId}"
								autocomplete="off" /></td>
					</s:if>
					<s:else>
						<td><s:textfield name="userId" class="txt"
								placeholder="ユーザーID" value="%{userId}" autocomplete="off" /></td>
					</s:else>
				</tr>
				<tr>
					<th scope="row"><s:label value="パスワード" /></th>
					<td><s:password name="password" class="txt"
							placeholder="パスワード" autocomplete="off" /></td>
				</tr>
			</table>
			<div class="check_box">
				<s:if test="(#session.saveUserId==true && #session.loginUserId != null && !#session.loginUserId.isEmpty()) || saveUserId == true">
					<s:checkbox checked="checked" name="saveUserId" />
				</s:if>
				<s:else>
					<s:checkbox name="saveUserId" />
				</s:else>
				<s:label value="ユーザーID保存" />
				<br>
			</div>
			<div class="submit_btn_box2">
				<s:submit value="ログイン" class="submit_btn" />
			</div>
		</s:form>
		<div class="submit_btn_box2">
			<s:form action="CreateUserAction">
				<s:submit value="新規ユーザー登録" class="submit_btn2" />
			</s:form>
		</div>
		<div class="submit_btn_box2">
			<s:form action="ResetPasswordAction">
				<s:submit value="パスワード再設定" class="submit_btn2" />
			</s:form>
		</div>
	</div>
</body>
</html>