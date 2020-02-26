package com.internousdev.rosso.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class LogoutAction extends ActionSupport implements SessionAware {

	private Map<String,Object> session;
	private String userId;
	private String kariSave;
	private boolean saveUserId;

	public String execute() {

		String result = SUCCESS;

		if(session.containsKey("saveUserId")) {
			userId = session.get("userId").toString();
			kariSave = session.get("saveUserId").toString();
			saveUserId = "null".equals(kariSave)? false : Boolean.valueOf(kariSave);
		}

		session.clear();
		session.put("loginUserId",userId);
		session.put("saveUserId", saveUserId);

		return result;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getKariSave() {
		return kariSave;
	}

	public void setKariSave(String kariSave) {
		this.kariSave = kariSave;
	}

	public boolean isSaveUserId() {
		return saveUserId;
	}

	public void setSaveUserId(boolean saveUserId) {
		this.saveUserId = saveUserId;
	}

}
