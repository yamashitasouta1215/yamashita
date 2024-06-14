package jp.co.sss.shop.controller.admin.artist;

import java.util.List;

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
import jp.co.sss.shop.bean.ArtistBean;
import jp.co.sss.shop.entity.Artist;
import jp.co.sss.shop.form.ArtistForm;
import jp.co.sss.shop.repository.ArtistRepository;
import jp.co.sss.shop.service.BeanTools;
import jp.co.sss.shop.util.Constant;

/**
 * カテゴリ管理 登録機能のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class AdminArtistRegistController {

	/**
	 * カテゴリ情報　リポジトリ
	 */
	@Autowired
	ArtistRepository artistRepository;

	/**
	 * セッション
	 */
	@Autowired
	HttpSession session;

	/**
	 * Entity、Form、Bean間のデータコピーサービス
	 */
	@Autowired
	BeanTools beanTools;

	/**
	 * 入力画面　表示処理(POST)
	 * 
	 * @return "admin/artist/regist_input" 入力画面　表示
	 */
	@RequestMapping(path = "/admin/artist/regist/input", method = RequestMethod.POST)
	public String registInput() {

		//セッションスコープより入力情報を取り出す
		ArtistForm artistForm = (ArtistForm) session.getAttribute("artistForm");

		if (artistForm == null) {
			//空の入力フォーム情報をセッションに保持 登録ボタンからの遷移
			session.setAttribute("artistForm", new ArtistForm());
		}
		//登録入力画面　表示処理
		return "redirect:/admin/artist/regist/input";

	}

	/**
	 * 入力画面　表示処理(GET)
	 * 
	 * @param model Viewとの値受渡し
	 * @return "admin/artist/regist_input" 登録入力画面　表示
	 */
	@RequestMapping(path = "/admin/artist/regist/input", method = RequestMethod.GET)
	public String registInput(Model model) {
		//セッションから入力フォーム情報取得
		ArtistForm artistForm = (ArtistForm) session.getAttribute("artistForm");
		if (artistForm == null) {
			// セッション情報がない場合、エラー
			return "redirect:/syserror";
		}
		//セッションから入力チェックエラー情報取得
		BindingResult result = (BindingResult) session.getAttribute("result");
		if (result != null) {
			//セッションにエラー情報がある場合、エラー情報をリクエストスコープに設定
			model.addAttribute("org.springframework.validation.BindingResult.artistForm", result);
			//セッションからエラー情報を削除
			session.removeAttribute("result");
		}
		// 入力フォーム情報を画面表示設定
		model.addAttribute("artistForm", artistForm);

		//登録入力画面　表示
		return "admin/artist/regist_input";

	}

	/**
	 * 入力情報確認処理
	 *
	 * @param form 入力フォーム
	 * @param result 入力値チェックの結果
	 * @return 
	 * 	入力値エラーあり："redirect:/admin/artist/regist/input" 登録入力画面　表示処理
	 * 	入力値エラーなし："redirect:/admin/artist/regist/check" 登録確認画面　表示処理
	 */
	@RequestMapping(path = "/admin/artist/regist/check", method = RequestMethod.POST)
	public String registInputCheck(@Valid @ModelAttribute ArtistForm form, BindingResult result) {

		// 入力されたカテゴリ情報をセッションに保持
		session.setAttribute("artistForm", form);

		if (result.hasErrors()) {
			// 入力値にエラーがあった場合、エラー情報をセッションに保持
			session.setAttribute("result", result);

			//登録入力画面　表示処理へリダイレクト
			return "redirect:/admin/artist/regist/input";
		}

		//登録確認画面　表示処理へリダイレクト
		return "redirect:/admin/artist/regist/check";
	}

	/**
	 * 確認画面　表示処理
	 *
	 * @param model Viewとの値受渡し
	 * @return "admin/artist/regist_check" 確認画面表示
	 */
	@RequestMapping(path = "/admin/artist/regist/check", method = RequestMethod.GET)
	public String registCheck(Model model) {
		// セッションから入力フォーム情報取得
		ArtistForm artistForm = (ArtistForm) session.getAttribute("artistForm");
		if (artistForm == null) {
			// セッション情報がない場合、エラー
			return "redirect:/syserror";
		}
		// 入力フォーム情報を画面表示設定
		model.addAttribute("artistForm", artistForm);
		// 確認画面　表示
		return "admin/artist/regist_check";

	}

	/**
	 * 情報登録処理
	 *
	 * @return "redirect:/admin/artist/regist/complete" 登録完了画面　表示処理
	 */
	@RequestMapping(path = "/admin/artist/regist/complete", method = RequestMethod.POST)
	public String registComplete() {

		//セッションから入力フォーム情報取得
		ArtistForm artistForm = (ArtistForm) session.getAttribute("artistForm");
		if (artistForm == null) {
			// セッション情報がない場合、エラー
			return "redirect:/syserror";
		}
		// 入力フォーム情報を登録用エンティティに設定
		Artist artist = new Artist();
		BeanUtils.copyProperties(artistForm, artist, "id");
		// カテゴリ情報の削除フラグを初期化
		artist.setDeleteFlag(Constant.NOT_DELETED);
		// カテゴリ情報をDBに保存
		artistRepository.save(artist);

		//セッション情報の削除
		session.removeAttribute("artistForm");

		// カテゴリ情報を全件検索
		List<Artist> artistList = artistRepository
				.findByDeleteFlagOrderByInsertDateDescIdDesc(Constant.NOT_DELETED);

		// エンティティ内の検索結果をJavaBeansにコピー
		List<ArtistBean> artistBeanList = beanTools.copyEntityListToArtistBeanList(artistList);

		// セッションスコープ中に保存されたカテゴリ一覧の情報を更新
		session.setAttribute("artist", artistBeanList);

		// 登録完了画面　表示処理
		//二重送信対策のためリダイレクトを行う
		return "redirect:/admin/artist/regist/complete";
	}

	/**
	 * 登録完了画面　表示処理
	 *
	 * @return "admin/artist/regist_complete" 登録完了画面　表示
	 */
	@RequestMapping(path = "/admin/artist/regist/complete", method = RequestMethod.GET)
	public String registCompleteFinish() {

		return "admin/artist/regist_complete";
	}

}
