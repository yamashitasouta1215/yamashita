package jp.co.sss.shop.controller.admin.artist;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.bean.ArtistBean;
import jp.co.sss.shop.entity.Artist;
import jp.co.sss.shop.form.ArtistForm;
import jp.co.sss.shop.repository.ArtistRepository;
import jp.co.sss.shop.service.BeanTools;
import jp.co.sss.shop.util.Constant;

/**
 * カテゴリ管理 削除機能のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class AdminArtistDeleteController {

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
	 * 削除確認画面 受付処理
	 *
	 * @param id 削除対象ID
	 * @return "redirect:/admin/artist/delete/check" 削除確認画面　表示
	 */
	@RequestMapping(path = "/admin/artist/delete/check/{id}", method = RequestMethod.POST)
	public String deleteCheck(@PathVariable Integer id) {

		// 削除対象の情報取得
		Artist artist = artistRepository.findByIdAndDeleteFlag(id, Constant.NOT_DELETED);
		if (artist == null) {
			// 対象が無い場合、エラー
			return "redirect:/syserror";
		}

		// 取得情報から表示フォーム情報を生成
		ArtistForm artistForm = new ArtistForm();
		//取得情報をフォームにコピー
		BeanUtils.copyProperties(artist, artistForm);

		// 表示フォーム情報をセッションに保持
		session.setAttribute("artistForm", artistForm);

		// 削除確認画面　表示処理
		return "redirect:/admin/artist/delete/check";
	}

	/**
	 * 確認画面　表示処理
	 *
	 * @param model Viewとの値受渡し
	 * @return "admin/artist/delete_check" 削除確認画面　表示
	 */
	@RequestMapping(path = "/admin/artist/delete/check", method = RequestMethod.GET)
	public String deleteCheckView(Model model) {

		//セッションから入力フォーム取得
		ArtistForm artistForm = (ArtistForm) session.getAttribute("artistForm");
		if (artistForm == null) {
			// セッション情報がない場合、エラー
			return "redirect:/syserror";
		}

		// 入力フォーム情報を画面表示設定
		model.addAttribute("artistForm", artistForm);

		// 削除確認画面　表示
		return "admin/artist/delete_check";
	}

	/**
	 * 削除処理、完了画面　表示処理
	 *
	 * @return "redirect:/admin/artist/delete/complete" 削除完了画面　表示処理
	 */
	@RequestMapping(path = "/admin/artist/delete/complete", method = RequestMethod.POST)
	public String deletetComplete() {

		// セッションから削除対象フォーム情報を取得
		ArtistForm artistForm = (ArtistForm) session.getAttribute("artistForm");
		if (artistForm == null) {
			// セッション情報がない場合、エラー
			return "redirect:/syserror";
		}

		// 削除対象の情報取得
		Artist artist = artistRepository.findByIdAndDeleteFlag(artistForm.getId(), Constant.NOT_DELETED);

		if (artist == null) {
			// 対象が無い場合、エラー
			return "redirect:/syserror";
		}

		// カテゴリ削除フラグを立てる情報を取得
		artist.setDeleteFlag(Constant.DELETED);

		// カテゴリ情報を保存
		artistRepository.save(artist);

		// カテゴリ情報を全件検索
		List<Artist> artistList = artistRepository
				.findByDeleteFlagOrderByInsertDateDescIdDesc(Constant.NOT_DELETED);

		// エンティティ内の検索結果をJavaBeansにコピー
		List<ArtistBean> artistBeanList = beanTools.copyEntityListToArtistBeanList(artistList);

		// セッションスコープ中に保存されたカテゴリ一覧の情報を更新
		session.setAttribute("artist", artistBeanList);

		// セッションの削除対象情報を削除
		session.removeAttribute("artistForm");

		// 削除完了画面　表示処理
		//二重送信対策のためリダイレクトを行う
		return "redirect:/admin/artist/delete/complete";
	}

	/**
	 * 削除完了画面 表示
	 *
	 * @return "admin/artist/delete_complete" 削除完了画面　表示
	 */
	@RequestMapping(path = "/admin/artist/delete/complete", method = RequestMethod.GET)
	public String deletetCompleteRedirect() {

		return "admin/artist/delete_complete";
	}

}
