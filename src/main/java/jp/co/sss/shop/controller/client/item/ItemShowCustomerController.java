package jp.co.sss.shop.controller.client.item;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

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
	

}