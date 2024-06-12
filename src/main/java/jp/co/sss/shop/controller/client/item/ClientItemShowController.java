package jp.co.sss.shop.controller.client.item;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.service.BeanTools;


/**
 * 商品管理 一覧表示機能(一般会員用)のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class ClientItemShowController {
	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;
	
	
	/**
	 * Entity、Form、Bean間のデータコピーサービス
	 */
	@Autowired
	BeanTools beanTools;
	
	/**
	 * トップ画面 表示処理
	 *
	 * @param model    Viewとの値受渡し
	 * @return "index" トップ画面
	 */
	@RequestMapping(path = "/" , method = { RequestMethod.GET, RequestMethod.POST })
	public String index(Model model) {
		model.addAttribute("items",itemRepository.findTop10AllByQuery());
		return "index";
	}
	
	@RequestMapping(path = "/client/item/list/{sortType}" , method = { RequestMethod.GET, RequestMethod.POST })
	
	public String List(@PathVariable Integer sortType,@RequestParam(name="categoryId",defaultValue="0",required = false)Integer categoryId,Model model) {
		Category category = new Category();
		category.setId(categoryId);
		if(sortType==1) {
			if(categoryId==0){
				model.addAttribute("items",itemRepository.findTop10ByOrderByReleaseDateDesc());
			}else {
				model.addAttribute("items",itemRepository.findTop10ByCategoryOrderByInsertDateDesc(category));
			}
		}else {
			if(categoryId==0) {
				model.addAttribute("items",itemRepository.findTop10AllByQuery());
			}else {
				model.addAttribute("items",itemRepository.findTop10CategoryByQuery(categoryId));
			}
		}
		return "client/item/list";
	}
	
	
	@RequestMapping("/client/item/detail/{id}")
	public String detail(@PathVariable Integer id,Model model,HttpSession session) {
		
		Item item=itemRepository.getReferenceById(id);
		ItemBean bean=new ItemBean();
		BeanUtils.copyProperties(item, bean);
		model.addAttribute("items", bean);
		
		return "client/item/detail";
	}
}
