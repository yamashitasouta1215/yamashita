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
		
		Page<Item>pageList = itemRepository.findAllByQuery(pageable);
		List<Item>itemList = pageList.getContent();
		model.addAttribute("pages",pageList);
		model.addAttribute("items",itemList);
		model.addAttribute("pageNum",2);

		return "index";
	}
	
	@RequestMapping(path = "/client/item/list/{sortType}", method = { RequestMethod.GET, RequestMethod.POST } )
	
	public String List(@PathVariable Integer sortType,@RequestParam(name="categoryId",defaultValue="0",required = false)Integer categoryId,Model model,Pageable pageable,HttpSession session) {
		Category category = new Category();
		category.setId(categoryId);
		if(sortType==1) {
			if(categoryId==0){
				
				Page<Item>pageList = itemRepository.findByOrderByReleaseDateDesc(pageable);
				List<Item>itemList = pageList.getContent();
				model.addAttribute("pages",pageList);
				model.addAttribute("items",itemList);
				model.addAttribute("pageNum",1);
				
//			}else {
//				
//				if(categoryId==1) {
//					Page<Item>pageList = itemRepository.findByCategoryOrderByReleaseDateDesc(category,pageable);
//					List<Item>itemList = pageList.getContent();
//					model.addAttribute("pages",pageList);
//					model.addAttribute("items",itemList);
//					model.addAttribute("pageNum",11);
//				}if(categoryId==2) {
//					Page<Item>pageList = itemRepository.findByCategoryOrderByReleaseDateDesc(category,pageable);
//					List<Item>itemList = pageList.getContent();
//					model.addAttribute("pages",pageList);
//					model.addAttribute("items",itemList);
//					model.addAttribute("pageNum",12);
//				}if(categoryId==3) {
//					Page<Item>pageList = itemRepository.findByCategoryOrderByReleaseDateDesc(category,pageable);
//					List<Item>itemList = pageList.getContent();
//					model.addAttribute("pages",pageList);
//					model.addAttribute("items",itemList);
//					model.addAttribute("pageNum",13);
//				}if(categoryId==4) {
//					Page<Item>pageList = itemRepository.findByCategoryOrderByReleaseDateDesc(category,pageable);
//					List<Item>itemList = pageList.getContent();
//					model.addAttribute("pages",pageList);
//					model.addAttribute("items",itemList);
//					model.addAttribute("pageNum",14);
//				}if(categoryId==5) {
//					Page<Item>pageList = itemRepository.findByCategoryOrderByReleaseDateDesc(category,pageable);
//					List<Item>itemList = pageList.getContent();
//					model.addAttribute("pages",pageList);
//					model.addAttribute("items",itemList);
//					model.addAttribute("pageNum",15);
//				}if(categoryId==6) {
//					Page<Item>pageList = itemRepository.findByCategoryOrderByReleaseDateDesc(category,pageable);
//					List<Item>itemList = pageList.getContent();
//					model.addAttribute("pages",pageList);
//					model.addAttribute("items",itemList);
//					model.addAttribute("pageNum",16);
//				}
			}
		}else {
			
			if(categoryId==0) {
				
				
				Page<Item>pageList = itemRepository.findAllByQuery(pageable);
				List<Item>itemList = pageList.getContent();
				model.addAttribute("pages",pageList);
				model.addAttribute("items",itemList);
				model.addAttribute("pageNum",3);
				
			}else {
				
				if(categoryId==1) {
					Page<Item>pageList = itemRepository.findCategoryByQuery(categoryId,pageable);
					List<Item>itemList = pageList.getContent();
					model.addAttribute("pages",pageList);
					model.addAttribute("items",itemList);
					model.addAttribute("pageNum",21);
				}if(categoryId==2) {
					Page<Item>pageList = itemRepository.findCategoryByQuery(categoryId,pageable);
					List<Item>itemList = pageList.getContent();
					model.addAttribute("pages",pageList);
					model.addAttribute("items",itemList);
					model.addAttribute("pageNum",22);
				}if(categoryId==3) {
					Page<Item>pageList = itemRepository.findCategoryByQuery(categoryId,pageable);
					List<Item>itemList = pageList.getContent();
					model.addAttribute("pages",pageList);
					model.addAttribute("items",itemList);
					model.addAttribute("pageNum",23);
				}if(categoryId==4) {
					Page<Item>pageList = itemRepository.findCategoryByQuery(categoryId,pageable);
					List<Item>itemList = pageList.getContent();
					model.addAttribute("pages",pageList);
					model.addAttribute("items",itemList);
					model.addAttribute("pageNum",24);
				}if(categoryId==5) {
					Page<Item>pageList = itemRepository.findCategoryByQuery(categoryId,pageable);
					List<Item>itemList = pageList.getContent();
					model.addAttribute("pages",pageList);
					model.addAttribute("items",itemList);
					model.addAttribute("pageNum",25);
				}if(categoryId==6) {
					Page<Item>pageList = itemRepository.findCategoryByQuery(categoryId,pageable);
					List<Item>itemList = pageList.getContent();
					model.addAttribute("pages",pageList);
					model.addAttribute("items",itemList);
					model.addAttribute("pageNum",26);
				}
				
			}
		}
		return "client/item/list";
	}
	
	@GetMapping("/categorypaging/{id}")
	public String cd(@PathVariable Integer id,Model model,Pageable pageable,HttpSession session) {
		
		if(id==null) {
			id=(Integer) session.getAttribute("category");
		}else {
			session.setAttribute("category",id);
		}
		Category category=new Category();
		//Item item=new Item();
		category.setId(id);
		Page<Item>pageList = itemRepository.findCategoryByQuery(id,pageable);
		List<Item>itemList = pageList.getContent();
		model.addAttribute("pages",pageList);
		model.addAttribute("items",itemList);
		model.addAttribute("pageNum",120);
		
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
