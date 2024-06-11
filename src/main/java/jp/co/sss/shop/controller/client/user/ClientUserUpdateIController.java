package jp.co.sss.shop.controller.client.user;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.util.Constant;

@Controller
public class ClientUserUpdateIController {
	
	/**
	 * 会員情報　リポジトリ
	 */
	@Autowired
	UserRepository userRepository;

	/**
	 * セッション
	 */
	@Autowired
	HttpSession session;

	
	//変更ボタン押下時　確認画面ー戻るボタン押下時処理　処理１
	@RequestMapping(path = "/client/user/update/input/{id}", method = RequestMethod.POST)
	public String updateInputInit(@PathVariable Integer id) {

		//セッションスコープより入力情報を取り出す
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		if (userForm == null) {

			// セッション情報がない場合、詳細画面からの遷移と判断し初期値を準備
			// 変更対象の情報取得
			User user = userRepository.findByIdAndDeleteFlag(id, Constant.NOT_DELETED);

			if (user == null) {
				// 対象が無い場合、エラー
				return "redirect:/syserror" ;
			}

			// 初期表示用フォーム情報の生成
			userForm = new UserForm();
			
			//変更対象の情報をuserFormにコピー
			BeanUtils.copyProperties(user, userForm);

			//変更入力フォームをセッションに保持
			session.setAttribute("userForm", userForm);

		}

		//変更入力画面　表示処理
		return "redirect:/client/user/update/input";

	}

	/**
	 * 入力画面　表示処理
	 *
	 * @param model Viewとの値受渡し
	 * @return "admin/user/update_input" 変更入力画面 表示
	 * 処理２
	 */
	@RequestMapping(path = "/client/user/update/input", method = RequestMethod.GET)
	public String updateInput(Model model) {

		//セッションから入力フォーム取得
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		if (userForm == null) {
			// セッション情報がない場合、エラー
			return "redirect:/syserror";
		}
		// 入力フォーム情報を画面表示設定
		model.addAttribute("userForm", userForm);

		BindingResult result = (BindingResult) session.getAttribute("result");
		if (result != null) {
			//セッションにエラー情報がある場合、エラー情報を画面表示設定
			model.addAttribute("org.springframework.validation.BindingResult.userForm", result);
			session.removeAttribute("result");
		}

		//変更入力画面　表示
		return "client/user/update_input";

	}

	
	//会員登録の情報を入力後の入力チェック　処理３
		@RequestMapping(path = "/client/user/update/check" , method = RequestMethod.POST)
		public String userCheck (@Valid @ModelAttribute UserForm form,BindingResult result){
			
			//直前のセッション情報を取得
			UserForm lastUserForm = (UserForm) session.getAttribute("userForm");
				if (lastUserForm == null) {
					// セッション情報が無い場合、エラー
					return "redirect:/syserror";
				}
				
//			if (form.getAuthority() == null) {
//					
//				//権限情報がない場合、セッション情報から値をセット
//				form.setAuthority(lastUserForm.getAuthority());
//			}	
				
			// 入力フォーム情報をセッションに保持
			session.setAttribute("userForm", form);

			if (result.hasErrors()) {
				
				// 入力値にエラーがあった場合、エラー情報をセッションに保持
				session.setAttribute("result", result);
				
				// 入力フォーム情報をセッションに保持
				session.setAttribute("userForm", form);
				
				// 登録入力画面　表示処理
				return "redirect:/client/user/update/input";
			}


			// 入力フォーム情報をセッションに保持
			session.setAttribute("userForm", form);
			
			//入力エラーがない場合
			return "client/user/update_check";
		}

			
		//登録確認画面　処理４
		@RequestMapping(path = "/client/user/update/check" , method = RequestMethod.GET)
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
			return "client/user/update_check";
			}
		
		
		//変更ボタン押下時　処理５
		@RequestMapping(path = "/client/user/update/complete", method = RequestMethod.POST)
		public String registComplete(@PathVariable Integer id,UserForm form,Model model) {

			//セッション保持情報から入力値再取得
//			UserForm userForm = (UserForm) session.getAttribute("userForm");
//			if (userForm == null) {
//			
//				// セッション情報がない場合、エラー
//				return "redirect:/syserror";
//			}
//			User user2=new User();
//			user2.setId(id);
			// 会員情報を生成
			User user = userRepository.getReferenceById(id);
			
//			user.setAuthority(Constant.DELETED)
			
			// 入力フォーム情報をエンティティに設定
			BeanUtils.copyProperties(form, user,"id");

//			authority = 2;
//			
//			user.setAuthority(authority);
//			user.getAuthority();
			
			// DB登録
			user=userRepository.save(user);

			UserBean userBean = new UserBean();
			BeanUtils.copyProperties(user, userBean);
			
			model.addAttribute("userinput", userBean);
			
			//登録完了画面　表示処理
			//二重送信防止のためリダイレクトを行う
			return "redirect:/client/user/update/complete";
		}

			
		//登録完了画面表示処理　処理７
		@RequestMapping(path = "/client/user/update/complete", method = RequestMethod.GET)
		public String registCompleteFinish() {

				return "client/user/update_complete";
			}

}
