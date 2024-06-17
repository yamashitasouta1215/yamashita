package jp.co.sss.shop.controller.client.item;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.service.BeanTools;
import jp.co.sss.shop.util.Constant;

@Controller
public class ItemShowCustomerController {
	
	@Autowired
	ItemRepository repository;
	
	@Autowired
	BeanTools beanTools;
	
//	//追加機能　CD検索
	@RequestMapping(path="/searchCD" ,method = { RequestMethod.GET, RequestMethod.POST })
	public String cd(String name,Model model,Pageable pageable,HttpSession session) {
		
		
		if(name==null) {
			name=(String) session.getAttribute("cd");
		}else {
			session.setAttribute("cd",name);
		}
		Item item=new Item();
		item.setName(name);
		Page<Item>pageList = repository.findByNameContaining(name,Constant.NOT_DELETED,pageable);
		List<Item>itemList = pageList.getContent();
		model.addAttribute("pages",pageList);
		model.addAttribute("items",itemList);
		model.addAttribute("pageNum",3);
		
		return "client/item/list";
	}
	

	
	@RequestMapping(path="/searchPrice",method = { RequestMethod.GET, RequestMethod.POST })
	public String month(Model model,Integer price,HttpSession session,Pageable pageable) {
	
		if(price==null) {
			price=(int) session.getAttribute("pri");
		}else {
			session.setAttribute("pri",price);
		}
		Item item=new Item();
		item.setPrice(price);
		
		if(price==1) {
			Page<Item>pageList = repository.findByPriceLessThanOrderByPrice(1000,Constant.NOT_DELETED,pageable);
			List<Item>itemList = pageList.getContent();
			model.addAttribute("pages",pageList);
			model.addAttribute("items",itemList);
			model.addAttribute("pageNum",4);
			
		}else if(price==2) {
			Page<Item>pageList = repository.findByPriceBetweenOrderByPrice(1001,2000,Constant.NOT_DELETED,pageable);
			List<Item>itemList = pageList.getContent();
			model.addAttribute("pages",pageList);
			model.addAttribute("items",itemList);
			model.addAttribute("pageNum",5);
			
		}else if(price==3) {
			Page<Item>pageList = repository.findByPriceBetweenOrderByPrice(2001,3000,Constant.NOT_DELETED,pageable);
			List<Item>itemList = pageList.getContent();
			model.addAttribute("pages",pageList);
			model.addAttribute("items",itemList);
			model.addAttribute("pageNum",6);
		}else if(price==4) {
			Page<Item>pageList = repository.findByPriceBetweenOrderByPrice(3001,4000,Constant.NOT_DELETED,pageable);
			List<Item>itemList = pageList.getContent();
			model.addAttribute("pages",pageList);
			model.addAttribute("items",itemList);
			model.addAttribute("pageNum",7);
		}else if(price==5) {
			Page<Item>pageList = repository.findByPriceBetweenOrderByPrice(4001,5000,Constant.NOT_DELETED,pageable);
			List<Item>itemList = pageList.getContent();
			model.addAttribute("pages",pageList);
			model.addAttribute("items",itemList);
			model.addAttribute("pageNum",8);
		}else if(price==6) {
			Page<Item>pageList = repository.findByPriceGreaterThanOrderByPrice(5001,Constant.NOT_DELETED,pageable);
			List<Item>itemList = pageList.getContent();
			model.addAttribute("pages",pageList);
			model.addAttribute("items",itemList);
			model.addAttribute("pageNum",9);
		}else {
			Page<Item>pageList = repository.findByDeleteFlagOrderByReleaseDateDesc(Constant.NOT_DELETED,pageable);
			List<Item>itemList = pageList.getContent();
			model.addAttribute("pages",pageList);
			model.addAttribute("items",itemList);
			model.addAttribute("pageNum",10);	
		}
			
		return "client/item/list";
	
	}

	

}

