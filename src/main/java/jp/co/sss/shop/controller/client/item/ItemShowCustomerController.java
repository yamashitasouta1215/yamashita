package jp.co.sss.shop.controller.client.item;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;

@Controller
public class ItemShowCustomerController {
	
	@Autowired
	ItemRepository repository;
	OrderItemRepository repository1;
	
	@RequestMapping("/client/item/list/1")
	public String showItemListByOrderById(Model model) {
		model.addAttribute("items",repository.findTop10ByOrderByInsertDateDesc());
		return "client/item/list";
	}
	
	@RequestMapping("/client/item/list/2")
	public String showItemListByOrderByQuantity(Model model,Integer quantity) {
		model.addAttribute("items",repository1.findByOrderByQuantity());
		return "client/item/list";
	}
	
}
