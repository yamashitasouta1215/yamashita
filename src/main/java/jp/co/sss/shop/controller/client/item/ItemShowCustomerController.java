package jp.co.sss.shop.controller.client.item;


import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ArtistRepository;
import jp.co.sss.shop.repository.CategoryRepository;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;

@Controller
public class ItemShowCustomerController {
	
	@Autowired
	ItemRepository repository;
	OrderItemRepository repositoryoi;
	CategoryRepository repositorycategory;
	@Autowired
	ArtistRepository repositorya;
	

	//追加機能　CD検索
	@PostMapping("/searchCD")
	public String cd(String name,Model model) {
		
		Item item=new Item();
		item.setName(name);
		List<Item>items=repository.findByNameContaining(name);
		model.addAttribute("items",items);
		
		return "client/item/list";
	}

	

	@RequestMapping("/client/item/detail/{id}")
	public String detail(@PathVariable Integer id,Model model,HttpSession session) {
		
		Item item=repository.getReferenceById(id);
		ItemBean bean=new ItemBean();
		BeanUtils.copyProperties(item, bean);
		model.addAttribute("items", bean);
		session.setAttribute("id", bean.getId());

		return "client/item/detail";
	}
	
	
}
	