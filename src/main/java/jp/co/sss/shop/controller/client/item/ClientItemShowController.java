package jp.co.sss.shop.controller.client.item;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
	public String index(Model model,Pageable pageable) {
		
		Page<Item>pageList = itemRepository.findAllByQueryAndDeleteFlag(Constant.NOT_DELETED,pageable);
		List<Item>itemList = pageList.getContent();
		model.addAttribute("pages",pageList);
		model.addAttribute("items",itemList);
		return "index";
	}
	
	@RequestMapping(path = "/client/item/list/{sortType}" , method = { RequestMethod.GET, RequestMethod.POST })
	
	public String List(@PathVariable Integer sortType,@RequestParam(name="categoryId",defaultValue="0",required = false)Integer categoryId,Model model,Pageable pageable) {
		Category category = new Category();
		category.setId(categoryId);
		if(sortType==1) {
			if(categoryId==0){
				Page<Item>pageList = itemRepository.findByOrderByReleaseDateDesc(pageable);
				List<Item>itemList = pageList.getContent();
				model.addAttribute("pages",pageList);
				model.addAttribute("items",itemList);
				model.addAttribute("pageNum",1);
			}else {
				model.addAttribute("items",itemRepository.findByCategoryOrderByReleaseDateDesc(category));
			}
		}else {
			if(categoryId==0) {
				
				Page<Item>pageList = itemRepository.findAllByQueryAndDeleteFlag(Constant.NOT_DELETED,pageable);
				List<Item>itemList = pageList.getContent();
				model.addAttribute("pages",pageList);
				model.addAttribute("items",itemList);
				model.addAttribute("pageNum",2);
			}else {
				model.addAttribute("items",itemRepository.findCategoryByQuery(categoryId));
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
	
	//追加機能　CD検索
		@GetMapping("/searchCD")
		public String cd(String name,Model model,Pageable pageable,HttpSession session) {
			
			
			if(name==null) {
				name=(String) session.getAttribute("cd");
			}else {
				session.setAttribute("cd",name);
			}
			Item item=new Item();
			item.setName(name);
			Page<Item>pageList = itemRepository.findByNameContaining(name,pageable);
			List<Item>itemList = pageList.getContent();
			model.addAttribute("pages",pageList);
			model.addAttribute("items",itemList);
			model.addAttribute("pageNum",3);
			
			return "client/item/list";
		}
		
		@GetMapping("/searchPrice")
		public String month(Model model,Integer price,HttpSession session,Pageable pageable) {
		
			if(price==null) {
				price=(int) session.getAttribute("pri");
			}else {
				session.setAttribute("pri",price);
			}
			Item item=new Item();
			item.setPrice(price);
			
			if(price==1) {
				Page<Item>pageList = itemRepository.findByPriceLessThanOrderByPrice(1000,pageable);
				List<Item>itemList = pageList.getContent();
				model.addAttribute("pages",pageList);
				model.addAttribute("items",itemList);
				model.addAttribute("pageNum",4);
				
			}else if(price==2) {
				Page<Item>pageList = itemRepository.findByPriceBetweenOrderByPrice(1001,2000,pageable);
				List<Item>itemList = pageList.getContent();
				model.addAttribute("pages",pageList);
				model.addAttribute("items",itemList);
				model.addAttribute("pageNum",5);
				
			}else if(price==3) {
				Page<Item>pageList = itemRepository.findByPriceBetweenOrderByPrice(2001,3000,pageable);
				List<Item>itemList = pageList.getContent();
				model.addAttribute("pages",pageList);
				model.addAttribute("items",itemList);
				model.addAttribute("pageNum",6);
			}else if(price==4) {
				Page<Item>pageList = itemRepository.findByPriceBetweenOrderByPrice(3001,4000,pageable);
				List<Item>itemList = pageList.getContent();
				model.addAttribute("pages",pageList);
				model.addAttribute("items",itemList);
				model.addAttribute("pageNum",7);
			}else if(price==5) {
				Page<Item>pageList = itemRepository.findByPriceBetweenOrderByPrice(4001,5000,pageable);
				List<Item>itemList = pageList.getContent();
				model.addAttribute("pages",pageList);
				model.addAttribute("items",itemList);
				model.addAttribute("pageNum",8);
			}else if(price==6) {
				Page<Item>pageList = itemRepository.findByPriceGreaterThanOrderByPrice(5001,pageable);
				List<Item>itemList = pageList.getContent();
				model.addAttribute("pages",pageList);
				model.addAttribute("items",itemList);
				model.addAttribute("pageNum",9);
			}else {
				Page<Item>pageList = itemRepository.findAll(pageable);
				List<Item>itemList = pageList.getContent();
				model.addAttribute("pages",pageList);
				model.addAttribute("items",itemList);
				model.addAttribute("pageNum",10);
				
			}
				
			return "client/item/list";
		}
		
	
}