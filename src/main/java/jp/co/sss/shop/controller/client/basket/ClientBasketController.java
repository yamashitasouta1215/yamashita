package jp.co.sss.shop.controller.client.basket;

import java.util.ArrayList;
import java.util.Collections;
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
	@RequestMapping(path ="/client/basket/list", method = RequestMethod.GET)
	public String ShowBasket() {
		
//		@SuppressWarnings("unchecked")
//		List<BasketBean> basketItemList = (List<BasketBean>) session.getAttribute("basketBeans");
//		if (basketItemList == null) {
//			basketItemList = new ArrayList<>();
//		}
//		BasketBean itemAddToBasket = null;
//		Item item = itemRepository.getReferenceById(1);
//		itemAddToBasket = new BasketBean(item.getId(), item.getName(), item.getStock(),2);
//		//買い物かごリストに追加
//		basketItemList.add(itemAddToBasket);
//		
//		Item item2 = itemRepository.getReferenceById(4);
//		itemAddToBasket = new BasketBean(item2.getId(), item2.getName(), item2.getStock(),5);
//		//買い物かごリストに追加
//		basketItemList.add(itemAddToBasket);
//		session.setAttribute("basketBeans", basketItemList);
		
		return "client/basket/list";
	}
	
	//商品のかごへの追加
	@RequestMapping(path ="/client/basket/add", method = RequestMethod.POST)
	public String addBasket( Model model) {
		//セッションスコープ内にリスト要素がない場合、リストを作る
		@SuppressWarnings("unchecked")
		List<BasketBean> basketItemList = (List<BasketBean>) session.getAttribute("basketBeans");
		if (basketItemList == null) {
			basketItemList = new ArrayList<>();
		}
		
		int id = (int) session.getAttribute("id");
		
		//反転している要素をもとに戻す
		Collections.reverse(basketItemList);
		
		//ストックがいくつあるのか確認
//		Item itemStock =itemRepository.getReferenceById(id);
//		int itemStockNum = itemStock.getStock();
		
//		if (0 < itemStockNum) {
			//在庫がある場合
			
			boolean existItemBasket = false;
			BasketBean itemAddToBasket = null;
			
//			boolean boo =  basketItemList.isEmpty();
			
//			if (boo == false) {
				//かご内に追加したい商品が存在する場合、その数を増やす
				for (BasketBean itemInBasket : basketItemList) {
					if (itemInBasket.getId() == id) {
						itemAddToBasket = itemInBasket;
						int newOrderNum = itemAddToBasket.getOrderNum() + 1;
						itemAddToBasket.setOrderNum(newOrderNum);
						//注文数が在庫を上回るとき
//						if (newOrderNum > itemStockNum) {
//							model.addAttribute("itemNameListLessThan", itemStock.getName());
//						}
						existItemBasket = true;
					}
				}
//			}
			
			//かご内に追加したい商品が存在しない場合
			if (!existItemBasket) {
				System.out.println("aaaaa");
				Item item = itemRepository.getReferenceById(id);
				itemAddToBasket = new BasketBean(item.getId(), item.getName(), item.getStock(),1);
				//買い物かごリストに追加
				basketItemList.add(itemAddToBasket);
			}
			
			//要素を反転する
			Collections.reverse(basketItemList);
			
			//セッションスコープにリスト情報を追加
			session.setAttribute("basketBeans", basketItemList);
			
			
//		} else {
			//在庫がない場合
//			model.addAttribute("itemNameListZero", itemStock.getName());
//		}
		
		session.removeAttribute("id");
			
		return "client/basket/list";
	}
	
	//かご内の全件削除
	@RequestMapping(path ="/client/basket/allDelete", method = RequestMethod.POST)
	public String allDelete() {
		session.removeAttribute("basketBeans");
		return "client/basket/list";
	}
	
	//選択したアイテムの削除
	@RequestMapping(path ="/client/basket/delete", method = RequestMethod.POST)
	public String delete(Integer id) {
		
		@SuppressWarnings("unchecked")
		List<BasketBean> basketItemList = (List<BasketBean>) session.getAttribute("basketBeans");
		
		//反転している要素を元に戻す
		Collections.reverse(basketItemList);
		
		BasketBean itemAddToBasket = null;
		
		int i = 0;
		
		for (BasketBean itemInBasket : basketItemList) {
		
			if (itemInBasket.getId() == id) {
				itemAddToBasket = itemInBasket;
				int newOrderNum = itemAddToBasket.getOrderNum() - 1;
				itemAddToBasket.setOrderNum(newOrderNum);
				
				//注文数が0になった場合
				if (itemAddToBasket.getOrderNum() == 0) {
					basketItemList.remove(i);
					break;
				}
			}
			
			i = i + 1;
		}
		
		//要素が入っているか確認
		boolean boo =  basketItemList.isEmpty();
		
		if (boo == true) {
			session.removeAttribute("basketBeans");
		} else {
			//要素を反転する
			Collections.reverse(basketItemList);
		
			//セッションスコープにリスト情報を追加
			session.setAttribute("basketBeans", basketItemList);
		}
		
		
		return "client/basket/list";
	}
}
