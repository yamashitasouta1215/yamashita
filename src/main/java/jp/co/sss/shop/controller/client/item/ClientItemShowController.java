package jp.co.sss.shop.controller.client.item;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.service.BeanTools;
import jp.co.sss.shop.util.Constant;
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
		//表示できるアイテムがあれば売れ筋順で表示する
		model.addAttribute("items",itemRepository.findAllByQueryAndDeleteFlag(Constant.NOT_DELETED));
		//表示できるアイテムが無ければ新着順で表示する
		List<Item>items=itemRepository.findAllByQueryAndDeleteFlag(Constant.NOT_DELETED);
		if(items.isEmpty()) {
			
		model.addAttribute("items",itemRepository.findByDeleteFlagOrderByReleaseDateDesc(Constant.NOT_DELETED));
		}
		return "index";
	}
	
	@RequestMapping(path = "/client/item/list/{sortType}" , method = { RequestMethod.GET, RequestMethod.POST })
	
	public String List(@PathVariable Integer sortType,@RequestParam(name="categoryId",defaultValue="0",required = false)Integer categoryId,Model model,Pageable pageable) {
		Category category = new Category();
		category.setId(categoryId);
		if(sortType==1) {
			if(categoryId==0){
				Page<Item>pageList = itemRepository.findByDeleteFlagOrderByReleaseDateDesc(Constant.NOT_DELETED,pageable);
				List<Item>itemList = pageList.getContent();
				model.addAttribute("pages",pageList);
				model.addAttribute("items",itemList);
				model.addAttribute("pageNum",1);
			}else {
				model.addAttribute("items",itemRepository.findByCategoryOrderByReleaseDateDesc(category,Constant.NOT_DELETED));
			}
		}else {
			if(categoryId==0) {
				
				Page<Item>pageList = itemRepository.findAllByQueryAndDeleteFlag(Constant.NOT_DELETED,pageable);
				List<Item>itemList = pageList.getContent();
				model.addAttribute("items",itemList);
				List<Item>items=itemRepository.findAllByQueryAndDeleteFlag(Constant.NOT_DELETED);
				if(items.isEmpty()) {
					
				model.addAttribute("items",itemRepository.findByDeleteFlagOrderByReleaseDateDesc(Constant.NOT_DELETED,pageable));
				}
				model.addAttribute("pages",pageList);
				model.addAttribute("pageNum",2);
			}else {
				model.addAttribute("items",itemRepository.findCategoryByQuery(categoryId,Constant.NOT_DELETED));
			}
		}
		return "client/item/list";
	}
	
	
	@RequestMapping("/client/item/detail/{id}")
	public String detail(@PathVariable Integer id,Model model,HttpSession session,String categoryName) {
		
		Item item=itemRepository.getReferenceById(id);
//		ItemBean bean=new ItemBean();
//		BeanUtils.copyProperties(item, bean);
		
		model.addAttribute("items", item);
		
		return "client/item/detail";
	}
	

	
}









