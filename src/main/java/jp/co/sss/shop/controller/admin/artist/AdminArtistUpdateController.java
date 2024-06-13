package jp.co.sss.shop.controller.admin.artist;

import java.sql.Date;
import java.util.List;

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
import jp.co.sss.shop.bean.ArtistBean;
import jp.co.sss.shop.entity.Artist;
import jp.co.sss.shop.form.ArtistForm;
import jp.co.sss.shop.repository.ArtistRepository;
import jp.co.sss.shop.service.BeanTools;
import jp.co.sss.shop.util.Constant;

/**
 * カテゴリ管理 変更機能のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class AdminArtistUpdateController {

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
	 * 入力画面　初期表示処理(POST)
	 * 
	 * @param id 変更対象ID
	 * @return "redirect:/admin/artist/update/input" 入力録画面　表示処理
	 */
	@RequestMapping(path = "/admin/artist/update/input/{id}", method = RequestMethod.POST)
	public String updateInput(@PathVariable Integer id) {

		//セッションスコープより入力情報を取り出す
		ArtistForm artistForm = (ArtistForm) session.getAttribute("artistForm");

		if (artistForm == null) {
			// セッション情報がない場合、詳細画面からの遷移と判断し初期値を準備

			// 変更対象の情報を取得
			Artist artist = artistRepository.findByIdAndDeleteFlag(id, Constant.NOT_DELETED);

			if (artist == null) {
				// 対象が無い場合、エラー
				return "redirect:/syserror";
			}

			// 初期表示用フォーム情報の生成しIDで取得した値を入力フォーム情報としてセット
			artistForm = new ArtistForm();
			BeanUtils.copyProperties(artist, artistForm);

			//変更入力フォームをセッションに保持
			session.setAttribute("artistForm", artistForm);
		}

		//変更入力画面　表示処理
		return "redirect:/admin/artist/update/input";

	}

	/**
	 * 入力画面　表示処理
	 *
	 * @param model Viewとの値受渡し
	 * @return "admin/artist/update_input" 更新入力画面表示
	 */
	@RequestMapping(path = "/admin/artist/update/input", method = RequestMethod.GET)
	public String updateInput(Model model) {

		//セッションから入力フォーム取得
		ArtistForm artistForm = (ArtistForm) session.getAttribute("artistForm");
		if (artistForm == null) {
			// セッション情報がない場合、エラー
			return "redirect:/syserror";
		}

		BindingResult result = (BindingResult) session.getAttribute("result");
		if (result != null) {
			//セッションにエラー情報がある場合、エラー情報をリクエストスコープに設定
			model.addAttribute("org.springframework.validation.BindingResult.artistForm", result);
			// セッションのエラー情報を削除
			session.removeAttribute("result");
		}

		// 入力フォーム情報を画面表示設定
		model.addAttribute("artistForm", artistForm);

		//変更入力画面　表示
		return "admin/artist/update_input";

	}

	/**
	 * 変更確認 処理
	 *
	 * @param form 入力フォーム
	 * @param result 入力チェック結果
	 * @return 
	 *   入力値エラーあり："redirect:/admin/artist/update/input" 変更入力画面へ 
	 *   入力値エラーなし："redirect:/admin/artist/update/check" 変更確認画面へ
	 */
	@RequestMapping(path = "/admin/artist/update/check", method = RequestMethod.POST)
	public String updateInputCheck(@Valid @ModelAttribute ArtistForm form, BindingResult result) {

		// 入力されたカテゴリ情報をセッションに保持
		session.setAttribute("artistForm", form);

		if (result.hasErrors()) {
			// 入力値にエラーがあった場合、エラー情報をセッションに保持
			session.setAttribute("result", result);

			//変更入力画面　表示処理
			return "redirect:/admin/artist/update/input";
		}

		//変更確認画面　表示処理
		return "redirect:/admin/artist/update/check";
	}

	/**
	 * 確認画面　表示処理
	 *
	 * @param model Viewとの値受渡し
	 * @return "admin/artist/update_check" 確認画面 表示
	 */
	@RequestMapping(path = "/admin/artist/update/check", method = RequestMethod.GET)
	public String updateCheck(Model model) {
		//セッションから入力フォーム情報取得
		ArtistForm artistForm = (ArtistForm) session.getAttribute("artistForm");
		if (artistForm == null) {
			// セッション情報がない場合、エラー
			return "redirect:/syserror";
		}
		//入力フォーム情報をスコープへ設定
		model.addAttribute("artistForm", artistForm);

		// 変更確認画面　表示
		return "admin/artist/update_check";

	}

	/**
	 * 変更登録、完了画面表示処理
	 *
	 * @return "redirect:/admin/artist/update/complete" 変更完了画面　表示へ
	 */
	@RequestMapping(path = "/admin/artist/update/complete", method = RequestMethod.POST)
	public String updateComplete() {

		//セッション保持情報から入力値再取得
		ArtistForm artistForm = (ArtistForm) session.getAttribute("artistForm");
		if (artistForm == null) {
			// セッション情報がない場合、エラー
			return "redirect:/syserror";
		}

		// 変更対象の情報取得
		Artist artist = artistRepository.findByIdAndDeleteFlag(artistForm.getId(), Constant.NOT_DELETED);
		if (artist == null) {
			// 対象が無い場合、エラー
			return "redirect:/syserror";
		}

		// 入力値以外の情報を一時退避
		Integer deleteFlag = artist.getDeleteFlag();
		Date insertDate = artist.getInsertDate();

		// 入力フォーム情報を変更用エンティティにコピー
		BeanUtils.copyProperties(artistForm, artist);

		// 入力値以外の情報を設定
		artist.setDeleteFlag(deleteFlag);
		artist.setInsertDate(insertDate);

		// カテゴリ情報を保存
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

		// 変更完了画面　表示処理
		//二重送信対策のためリダイレクトを行う
		return "redirect:/admin/artist/update/complete";

	}

	/**
	 * 変更完了画面　表示
	 * 
	 * @return "admin/artist/update_complete"
	 */
	@RequestMapping(path = "/admin/artist/update/complete", method = RequestMethod.GET)
	public String updateCompleteFinish() {

		return "admin/artist/update_complete";
	}

}
