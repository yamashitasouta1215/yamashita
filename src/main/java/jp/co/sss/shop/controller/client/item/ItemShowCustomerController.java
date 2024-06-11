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
	
<<<<<<< HEAD
	
	@RequestMapping("/client/item/list/1")
	public String showItemListByOrderById(Model model,Integer categoryId) {
	//	session.setAttribute("categories",repositorycategory.findAll());
		model.addAttribute("items",repository.findTop10ByOrderByReleaseDateDesc());
		Category category = new Category();
		category.setId(categoryId);
		List<Item>items = repository.findByCategory(category);
		model.addAttribute("items",items);
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
	
=======
>>>>>>> branch 'master' of https://yamashitasouta1215@github.com/yamashitasouta1215/yamashita.git

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
	