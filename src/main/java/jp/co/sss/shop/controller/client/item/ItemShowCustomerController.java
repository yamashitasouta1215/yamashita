package jp.co.sss.shop.controller.client.item;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.repository.ItemRepository;

@Controller
public class ItemShowCustomerController {
	
	@Autowired
	ItemRepository repository;
	
	@RequestMapping("/client/item/list/1")
	public String showItemListByOrderById(Model model) {
		model.addAttribute("items",repository.findAllByOrderById());
		return "client/item/list";
	}
	

}
