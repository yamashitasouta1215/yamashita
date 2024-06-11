package jp.co.sss.shop.controller.client.user;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.util.Constant;

@Controller
public class ClientUserShowController {
	
	/**
	 * 会員情報　リポジトリ
	 */
	@Autowired
	UserRepository userRepository;

	/**
	 * セッション情報
	 */
	@Autowired
	HttpSession session;
	
	
	//会員詳細表示
	@RequestMapping(path = "/client/user/detail/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	 public String showUser(@PathVariable int id, Model model) {
		
		// 表示対象の情報を取得
		User user = userRepository.findByIdAndDeleteFlag(id, Constant.NOT_DELETED);
			if (user == null) {
				// 対象が無い場合、エラー
					return "redirect:/syserror";
				}


		// Userエンティティの各フィールドの値をUserBeanにコピー
		UserBean userBean = new UserBean();
		BeanUtils.copyProperties(user, userBean);

		// 会員情報をViewに渡す
		model.addAttribute("userBean", userBean);

		//会員登録・変更・削除用のセッションスコープを初期化
		session.removeAttribute("userForm");

		// 詳細画面　表示
		return "client/user/detail";
	}
}


