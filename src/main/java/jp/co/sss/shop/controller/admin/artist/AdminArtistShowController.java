package jp.co.sss.shop.controller.admin.artist;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.bean.ArtistBean;
import jp.co.sss.shop.entity.Artist;
import jp.co.sss.shop.repository.ArtistRepository;
import jp.co.sss.shop.util.Constant;
/**
 * カテゴリ管理 一覧表示機能のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class AdminArtistShowController {
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
	 * 一覧表示処理
	 *
	 * @param model Viewとの値受渡し
	 * @param pageable ページング制御
	 * @return "admin/artist/list" カテゴリ情報 一覧画面へ
	 */
	@RequestMapping(path = "/admin/artist/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String showArtistList(Model model, Pageable pageable) {
		// 商品情報の登録数の取得と新規追加可否チェック
		Long artistCount = artistRepository.count();
		Boolean registrable = true;
		if (artistCount == Constant.CATEGORIES_MAX_COUNT) {
			//カテゴリ情報の登録数が最大値の場合、新規追加不可
			registrable = false;
		}
		// 表示画面でページングが必要なため、ページ情報付きのカテゴリ情報を取得する検索を行う
		Page<Artist> artistPage = artistRepository
				.findByDeleteFlagOrderByInsertDateDescIdDescPage(Constant.NOT_DELETED, pageable);
		// カテゴリ情報をViewへ渡す
		//ページ情報付きの検索結果からgetContent()メソッドを使用してレコード情報のみを取り出す
		List<Artist> artistList = artistPage.getContent();
		model.addAttribute("pages", artistPage);
		model.addAttribute("artist", artistList);
		model.addAttribute("registrable", registrable);
		//カテゴリ登録・変更・削除用のセッションスコープを初期化
		session.removeAttribute("artistForm");
		return "admin/artist/list";
	}
	/**
	 * 詳細画面表示 処理
	 *
	 * @param id ID
	 * @param model Viewとの値受渡し
	 * @return "admin/artist/detail" 詳細画面 表示
	 */
	@RequestMapping(path = "/admin/artist/detail/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	public String showArtist(@PathVariable Integer id, Model model) {
		// 表示対象の情報取得
		Artist artist = artistRepository.findByIdAndDeleteFlag(id, Constant.NOT_DELETED);
		if (artist == null) {
			// 対象が無い場合、エラー
			return "redirect:/syserror";
		}
		ArtistBean artistBean = new ArtistBean();
		// Artistエンティティの各フィールドの値をArtistBeanにコピー
		BeanUtils.copyProperties(artist, artistBean);
		// カテゴリ情報をViewへ渡す
		model.addAttribute("artist", artistBean);
		//カテゴリ登録・変更・削除用のセッションスコープを初期化
		session.removeAttribute("artistForm");
		return "admin/artist/detail";
	}
}