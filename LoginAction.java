package com.internousdev.rosso.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.rosso.dao.CartInfoDAO;
import com.internousdev.rosso.dao.UserInfoDAO;
import com.internousdev.rosso.dto.CartInfoDTO;
import com.internousdev.rosso.util.InputChecker;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport implements SessionAware {

	private int totalPrice;
	private String userId;
	private String password;
	private boolean saveUserId;
	private String errorMessage;
	private List<String> userIdErrorMessageList;
	private List<String> passwordErrorMessageList;
	private List<CartInfoDTO> cartList;
	private List<CartInfoDTO> cartInfoForTmpUser;
	private Map<String ,Object> session;
	private CartInfoDAO cartInfoDAO = new CartInfoDAO();

	public String execute() throws SQLException {

		UserInfoDAO userInfoDAO = new UserInfoDAO();
		String result = ERROR;
		session.remove("saveUserId");
		InputChecker inputChecker = new InputChecker();

		//ユーザー情報登録機能で保持したユーザーIDを取得
		if(session.containsKey("createUserFlg") && Integer.parseInt(session.get("createUserFlg").toString()) == 1) {
			userId = session.get("userIdForCreateUser").toString();
			session.remove("userIdForCreateUser");
			session.remove("createUserFlg");
			result = SUCCESS;
		} else {

			//入力チェック(未入力、桁数、文字種)
			userIdErrorMessageList = inputChecker.doCheck("ユーザーID", userId, 1, 8, true, false, false, true, false, false);
			passwordErrorMessageList = inputChecker.doCheck("パスワード", password, 1, 16, true, false, false, true, false, false);

			if(userIdErrorMessageList.size() > 0 || passwordErrorMessageList.size() > 0) {
				return result;
			}

			//認証処理(DBにユーザーIDとパスワードが一致するユーザーが存在しているかを確認)
			if(userInfoDAO.isCheckUserInfo(userId,password)) {
				result = SUCCESS;
			} else {
				errorMessage = "ユーザーIDまたはパスワードが異なります。";
				return result;
			}
		}

		String tmpUserId = session.get("tmpUserId").toString();

		if(!session.containsKey("tmpUserId") && !session.containsKey("userId")) {
			return "sessionTimeout";
		}

		List<CartInfoDTO> cartInfoForTmpUser = new ArrayList<>();
		cartInfoForTmpUser = cartInfoDAO.getCartInfo(tmpUserId);

		if(cartInfoForTmpUser != null && cartInfoForTmpUser.size() > 0) {
			boolean cartResult = changeCartInfo(cartInfoForTmpUser,tmpUserId);

			if(!cartResult) {
				return "DBError";
			}
		}

		session.put("userId", userId);
		session.put("logined", 1);
		session.remove("tmpUserId");

		if(saveUserId == true) {
			session.put("saveUserId", true);
		}

		//カートフラグがある場合は、カート情報を取得しカート画面へ遷移
		if(session.containsKey("cartFlg") && Integer.parseInt(session.get("cartFlg").toString()) == 1) {
			session.remove("cartFlg");
			cartList = cartInfoDAO.getCartInfo(userId);
			totalPrice = cartInfoDAO.getTotalPrice(userId);
			result = "cart";
		}
		return result;
	}

	//カート情報の紐づけ
	private boolean changeCartInfo(List<CartInfoDTO> cartInfoForTmpUser,String tmpUserId) throws SQLException {
		int count = 0;
		boolean check = false;
		for(CartInfoDTO dto:cartInfoForTmpUser) {

			if(cartInfoDAO.isCartInfoCheckId(userId,dto.getProductId())) {
				count = cartInfoDAO.cartInfoUpdateAdd(userId,dto.getProductId(),dto.getProductCount());
				cartInfoDAO.deleteCartInfo(tmpUserId,String.valueOf(dto.getProductId()));
			} else {
				count = cartInfoDAO.cartInfoUpdateUserId(userId,tmpUserId,dto.getProductId());
			}

			if(count == 1) {
				check = true;
			}

		}

		return check;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<String> getUserIdErrorMessageList() {
		return userIdErrorMessageList;
	}

	public void setUserIdErrorMessageList(List<String> userIdErrorMessageList) {
		this.userIdErrorMessageList = userIdErrorMessageList;
	}

	public List<String> getPasswordErrorMessageList() {
		return passwordErrorMessageList;
	}

	public void setPasswordErrorMessageList(List<String> passwordErrorMessageList) {
		this.passwordErrorMessageList = passwordErrorMessageList;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public List<CartInfoDTO> getCartList() {
		return cartList;
	}

	public void setCartList(List<CartInfoDTO> cartList) {
		this.cartList = cartList;
	}

	public List<CartInfoDTO> getCartInfoForTmpUser() {
		return cartInfoForTmpUser;
	}

	public void setCartInfoForTmpUser(List<CartInfoDTO> cartInfoForTmpUser) {
		this.cartInfoForTmpUser = cartInfoForTmpUser;
	}

	public boolean isSaveUserId() {
		return saveUserId;
	}

	public void setSaveUserId(boolean saveUserId) {
		this.saveUserId = saveUserId;
	}

}
