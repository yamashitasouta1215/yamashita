package jp.co.sss.shop.controller.client.item;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.entity.Artist;
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
	@GetMapping("/searchCD")
	public String cd(String name,Model model,Pageable pageable,HttpSession session) {
		
		if(name==null) {
			name=(String) session.getAttribute("cd");
		}else {
			session.setAttribute("cd",name);
		}
		Item item=new Item();
		item.setName(name);
		Page<Item>pageList = repository.findByNameContaining(name,pageable);
		List<Item>itemList = pageList.getContent();
		model.addAttribute("pages",pageList);
		model.addAttribute("items",itemList);
		model.addAttribute("pageNum",3);
		
		return "client/item/list";
	}
	
//	@PostMapping("/searchMonth")
//	public String month(Model model,Integer releaseDate) {
//		
//		List<Item>item=repository.findByReleaseDateContaining(releaseDate);
//		model.addAttribute("item",item);
//		
//		return "client/item/list";
//	}
	
	@PostMapping("/searchPrice")
	public String month(Model model,Integer price) {
		
		if(price==1) {
		List<Item>item=repository.findByPriceLessThanOrderByPrice(1000);
		model.addAttribute("items",item);
		}else if(price==2) {
			List<Item>item=repository.findByPriceBetweenOrderByPrice(1001,2000);
			model.addAttribute("items",item);
		}else if(price==3) {
			List<Item>item=repository.findByPriceBetweenOrderByPrice(2001,3000);
			model.addAttribute("items",item);
		}else if(price==4) {
			List<Item>item=repository.findByPriceBetweenOrderByPrice(3001,4000);
			model.addAttribute("items",item);
		}else if(price==5) {
			List<Item>item=repository.findByPriceBetweenOrderByPrice(4001,5000);
			model.addAttribute("items",item);
		}else if(price==6) {
		List<Item>item=repository.findByPriceGreaterThanOrderByPrice(5001);
		model.addAttribute("items",item);
		}else {
			List<Item>item=repository.findAll();
			model.addAttribute("items",item);
			
		}
			
		return "client/item/list";
	}
	
	
	@PostMapping("/searchArtist")
	public String artist(Model model,String name) {
//		
//		if(name!=null) {
//		Artist artist =new Artist();
//		artist.setName(name);
//		List<Artist>artists=repositorya.findByNameContaining(name);
//		int artistId = 0;
		
//		int artistId=((Artist) artist).getId();
//		int artistId=artist.getId();	
//		System.out.print(artistId);
//		List<Item> items=repository.findByArtistId(artistId);
//		model.addAttribute("items",items);
////		}
//		model.addAttribute("items",repository.findAll());
		
		
		if(name!=null) {
			Artist artist=new Artist();
		artist=repositorya.findByNameContaining(name);
		
//		int artistId=artist.getId();	
//		System.out.print(artistId);
		List<Item> items=repository.findByArtistId(artist.getId());
		model.addAttribute("items",items);
		}
		return "client/item/list";
	}
	
}