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
		return "client/basket/list";
	}
	
	//商品のかごへの追加
	@RequestMapping(path ="/client/basket/add", method = RequestMethod.POST)
	public String addBasket(Integer id, Model model) {
		//セッションスコープ内にリスト要素がない場合、リストを作る
		@SuppressWarnings("unchecked")
		List<BasketBean> basketItemList = (List<BasketBean>) session.getAttribute("basketBeans");
		if (basketItemList == null) {
			basketItemList = new ArrayList<>();
		}
		
		//ストックがいくつあるのか確認
		Item itemStock =itemRepository.getReferenceById(id);
		int itemStockNum = itemStock.getStock();
		
		if (0 < itemStockNum) {
			//在庫がある場合
			
			boolean existItemBasket = false;
			BasketBean itemAddToBasket = null;
			
			//かご内に追加したい商品が存在する場合、その数を増やす
			for (BasketBean itemInBasket : basketItemList) {
				if (itemInBasket.getId() == id) {
					itemAddToBasket = itemInBasket;
					int newOrderNum = itemAddToBasket.getOrderNum() + 1;
					itemAddToBasket.setOrderNum(newOrderNum);
					//注文数が在庫を上回るとき
					if (newOrderNum > itemStockNum) {
						model.addAttribute("itemNameListLessThan", itemStock.getName());
					}
					existItemBasket = true;
				}
			}
			
			//かご内に追加したい商品が存在しない場合
			if (!existItemBasket) {
				Item item = itemRepository.getReferenceById(id);
				itemAddToBasket = new BasketBean(item.getId(), item.getName(), item.getStock(),1);
				//買い物かごリストに追加
				basketItemList.add(itemAddToBasket);
			}
			
			
			//セッションスコープにリスト情報を追加
			session.setAttribute("basketBeans", basketItemList);
			
			
		} else {
			//在庫がない場合
			model.addAttribute("itemNameListZero", itemStock.getName());
		}
		
		return "client/basket/list";
	}
	
	//かご内の全件削除
	@RequestMapping(path ="/client/basket/allDelete", method = RequestMethod.POST)
	public String allDelete() {
		session.removeAttribute("basketBeans");
		return "client/basket/list";
	}
	
	@RequestMapping(path ="/client/basket/delete", method = RequestMethod.POST)
	public String delete(Integer id) {
		
		@SuppressWarnings("unchecked")
		List<BasketBean> basketItemList = (List<BasketBean>) session.getAttribute("basketBeans");
		
		BasketBean itemAddToBasket = null;
		
		for (BasketBean itemInBasket : basketItemList) {
			int i = 0;
			i = i + 1;
			
			if (itemInBasket.getId() == id) {
				itemAddToBasket = itemInBasket;
				int newOrderNum = itemAddToBasket.getOrderNum() - 1;
				itemAddToBasket.setOrderNum(newOrderNum);
				
				//注文数が0になった場合
				if (newOrderNum == 0) {
					i = i - 1;
					basketItemList.remove(i);
				}
			}
		}
		
		//セッションスコープにリスト情報を追加
		session.setAttribute("basketBeans", basketItemList);
		
		return "client/basket/list";
	}
}
