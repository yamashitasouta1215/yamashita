package jp.co.sss.shop.controller.client.user;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;

//新規登録のコントローラー

@Controller
public class ClientUserRegistController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	HttpSession session;

	//新規登録リンク　クリック時処理 処理１
	@RequestMapping(path = "/client/user/regist/input/init", method = RequestMethod. GET)
	public String userInputInit () {
		
		session.invalidate();
		UserForm userForm = new UserForm();
		session.setAttribute("userForm", userForm);
		
		return "redirect:/client/user/regist/input";
	}
	
	
	//登録画面表示処理　処理３
	@RequestMapping(path = "/client/user/regist/input", method = RequestMethod.GET)
	public String userInput (Model model) {
		
		//セッションから入力フォーム情報取得
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		
		// 入力フォーム情報をスコープに設定UserForm userForm = (UserForm) session.getAttribute("userForm");
		if (userForm == null) {
			
			// セッション情報がない場合、エラー
			return "redirect:/syserror";
		}

		BindingResult result = (BindingResult) session.getAttribute("result");
		if (result != null) {
			
			//セッションにエラー情報がある場合、エラー情報をスコープに設定
			model.addAttribute("org.springframework.validation.BindingResult.userForm", result);
			
			// セッションにエラー情報を削除
			session.removeAttribute("result");
		}

		// 入力フォーム情報をスコープに設定
		model.addAttribute("userForm", userForm);
		
		//登録画面表示
		return "client/user/regist_input";
	}
	
	
	//会員登録の情報を入力後の入力チェック　処理４
	@RequestMapping(path = "/client/user/regist/check" , method = RequestMethod.POST)
	public String userCheck (@Valid @ModelAttribute UserForm form,BindingResult result){
		
		//直前のセッション情報を取得
		UserForm lastUserForm = (UserForm) session.getAttribute("userForm");
			if (lastUserForm == null) {
				// セッション情報が無い場合、エラー
				return "redirect:/syserror";
			}
			
//		if (form.getAuthority() == null) {
//				
//			//権限情報がない場合、セッション情報から値をセット
//			form.setAuthority(lastUserForm.getAuthority());
//		}	
			
		// 入力フォーム情報をセッションに保持
		session.setAttribute("userForm", form);

		if (result.hasErrors()) {
			
			// 入力値にエラーがあった場合、エラー情報をセッションに保持
			session.setAttribute("result", result);
			
			// 入力フォーム情報をセッションに保持
			session.setAttribute("userForm", form);
			
			// 登録入力画面　表示処理
			return "redirect:/client/user/regist/input";
		}


		// 入力フォーム情報をセッションに保持
		session.setAttribute("userForm", form);
		
		//入力エラーがない場合
		return "redirect:/client/user/regist/check";
	}

		
	//登録確認画面　処理５
	@RequestMapping(path = "/client/user/regist/check" , method = RequestMethod.GET)
	public String registCheck (Model model) {
		
		//セッションから入力フォーム情報取得
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		if (userForm == null) {
		
			// セッション情報がない場合、エラー
			return "redirect:/syserror";
		 }
		
		//入力フォーム情報をスコープへ設定
		model.addAttribute("userForm", userForm);

		//登録確認画面　表示処理
		return "client/user/regist_check";
		}
	
		
	//登録ボタン押下時　処理６
	@RequestMapping(path = "/client/user/regist/complete", method = RequestMethod.POST)
	public String registComplete(Integer authority, Model model) {

		//セッション保持情報から入力値再取得
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		if (userForm == null) {
		
			// セッション情報がない場合、エラー
			return "redirect:/syserror";
		}

		// 会員情報を生成
		User user = new User();
		
//		user.setAuthority(Constant.DELETED)
		
		// 入力フォーム情報をエンティティに設定
		BeanUtils.copyProperties(userForm, user, "id");

		authority = 2;
		
		user.setAuthority(authority);
		user.getAuthority();
		
		// DB登録
		userRepository.save(user);

		UserBean userBean = new UserBean();
		BeanUtils.copyProperties(user, userBean);
		session.setAttribute("user",userBean);
		session.removeAttribute("userForm");
		
		//登録完了画面　表示処理
		//二重送信防止のためリダイレクトを行う
		return "redirect:/client/user/regist/complete";
	}

		
	//登録完了画面表示処理　処理７
	@RequestMapping(path = "/client/user/regist/complete", method = RequestMethod.GET)
	public String registCompleteFinish(HttpSession session) {
	


			return "client/user/regist_complete";
		}
}

