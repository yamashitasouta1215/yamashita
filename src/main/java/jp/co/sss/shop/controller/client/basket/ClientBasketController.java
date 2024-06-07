package jp.co.sss.shop.controller.client.basket;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;

@Controller
public class ClientBasketController {
	@Autowired
	HttpSession session;
	
	@Autowired
	ItemRepository itemRepository;
	
	//買い物かごタブをクリックした際のメソッド
	//かご内の一覧表示
	@RequestMapping(path ="client/basket/list", method = RequestMethod.GET)
	public String ShowBasket() {
		
		@SuppressWarnings("unchecked")
		//セッションスコープに中身があるかの確認
		List<BasketBean> basketItemList = (List<BasketBean>) session.getAttribute("basketItems");
		if (basketItemList == null) {
			basketItemList = new ArrayList<>();
			session.setAttribute("basketItems", basketItemList);
		}
		
		return "client/basket/list";
	}
	
	//商品のかごへの追加
	@RequestMapping(path ="/client/basket/add", method = RequestMethod.POST)
	public String addBasket(Integer id, Model model) {
		//セッションスコープ内にリスト要素がない場合、リストを作る
		@SuppressWarnings("unchecked")
		List<BasketBean> basketItemList = (List<BasketBean>) session.getAttribute("basketItems");
		if (basketItemList == null) {
			basketItemList = new ArrayList<>();
		}
		
		boolean existItemBasket = false;
		BasketBean itemAddToBasket = null;
		
		//かご内に追加したい商品が存在する場合、その数を増やす
		for (BasketBean itemInBasket : basketItemList) {
			if (itemInBasket.getId() == id) {
				itemAddToBasket = itemInBasket;
				int newOrderNum = itemAddToBasket.getOrderNum() + 1;
				itemAddToBasket.setOrderNum(newOrderNum);
				existItemBasket = true;
			}
		}
		
		//かご内に追加したい商品が存在しない場合
		if (!existItemBasket) {
			Item item = itemRepository.getReferenceById(id);
			itemAddToBasket = new BasketBean();
			itemAddToBasket.setId(item.getId());
			itemAddToBasket.setName(item.getName());
			itemAddToBasket.setStock(item.getStock());
			int i = 1;
			int orderNum = i++;
			itemAddToBasket.setOrderNum(orderNum);
			//買い物かごリストに追加
			basketItemList.add(itemAddToBasket);
		}
		
		return "forward:/client/basket/list";
	}
}
