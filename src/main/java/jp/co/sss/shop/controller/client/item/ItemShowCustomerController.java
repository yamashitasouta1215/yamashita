package jp.co.sss.shop.controller.client.item;


import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.CategoryRepository;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;

@Controller
public class ItemShowCustomerController {
	
	@Autowired
	ItemRepository repository;
	OrderItemRepository repositoryoi;
	CategoryRepository repositorycategory;
	
	@RequestMapping("/client/item/list/1")
	public String showItemListByOrderById(Model model) {
	//	session.setAttribute("categories",repositorycategory.findAll());
		model.addAttribute("items",repository.findTop10ByOrderByReleaseDateDesc());
		return "client/item/list";
	}
	
	@RequestMapping("/client/item/list/2")
	public String showItemListByOrderByQuantity(Model model,Integer quantity) {
		model.addAttribute("items",repositoryoi.findByOrderByQuantity());
		return "client/item/list";
	}
	
	@GetMapping("/searchCategory")
	public String searchByCategoryId(Integer categoryId,Model model) {
		
		Category category = new Category();
		category.setId(categoryId);
		List<Item>items = repository.findByCategory(category);
		model.addAttribute("items",items);
		return "client/item/list";
	}
	
	@RequestMapping("/client/item/detail/{id}")
	public String detail(@PathVariable Integer id,Model model) {
		
		Item item=repository.getReferenceById(id);
		ItemBean bean=new ItemBean();
		BeanUtils.copyProperties(item, bean);
		model.addAttribute("items", bean);
		
		
		return "client/item/detail";
	}
}
