package jp.co.sss.shop.controller.client.basket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	@SuppressWarnings("unchecked")
	@RequestMapping(path ="/client/basket/list", method = RequestMethod.GET)
	public String ShowBasket(Model model) {
		
		
		ArrayList <BasketBean> newBaskets = new ArrayList<>();
		
		Item items = new Item();
		/*
		 * セッションスコープより買い物かご情報取得
		 */
		 ArrayList<BasketBean> basketBean = new ArrayList<>();
		 
		 basketBean =(ArrayList<BasketBean>) session.getAttribute("basketBeans");
		
		 
		 if( basketBean != null) {
			 
//		 警告メッセージ表示用
			 List<String> ListZero = new ArrayList<String>();
			 List<String> ListLessThan = new ArrayList<String>();

//		 計算用変数
			 int total =0;
			 int Allprice = 0;
		 
		 /*
		  * 買い物かごの種類分だけ
		  */
		
			 for(int i=0;i < basketBean.size() ;i++) {
		
				 BasketBean basket = basketBean.get(i);
			
			/*
			 * かごの商品情報をItemエンティィへ格納
			 */
				 items=itemRepository.getReferenceById(basket.getId());
		
				 int orderNum = basket.getOrderNum();
			
				 int stock = items.getStock();
				 int price = items.getPrice();
				 
			String orderitemName = items.getName();
			
			/*
			 * 在庫数との照らし合わせ
			 */
					if(stock == 0) {
				
					/*
					 * 在庫0のため商品削除
					 */
							ListZero.add(orderitemName);
						
							items = null;
						
					} else if(orderNum > stock){
						
						/*
						 * 在庫数と注文数を合わせる
						 */
							ListLessThan.add(orderitemName);
						
						
							orderNum = stock;
							Allprice = price * stock;
						
					
							newBaskets.add(basket);
					
						
					} else {
							
						/*
						 * 必要処理なし
						 */
							Allprice = price * orderNum;
						
							newBaskets.add(basket);
						
						
					}		
				
				/*
				 * 買い物かご情報セット
				 */
				basket.setPriceSum(Allprice);
				basket.setOrderNum(orderNum);
				basket.setStock(stock);	
				
				
				total += Allprice;
				
		/*
		 * for文終わりの}		
		 */
			 }
			 
		
		if(ListZero.size() !=0 || ListLessThan.size() !=0) {
			
		model.addAttribute("itemNameListZero",ListZero);
		model.addAttribute("itemNameListLessThan",ListLessThan);
		
		session.setAttribute("priceSum", total);
		session.setAttribute("basketBeans", newBaskets);

			
			/*
			 * 買い物かごから完全消去されたときremoveする。
			 */
			if(newBaskets.size() == 0) {
				session.removeAttribute("basketBeans");
			}
		}
	 }
				
	
			
			return "/client/basket/list";	 
		
	}
			
		
//			List<BasketBean> basketItemList = (List<BasketBean>) session.getAttribute("basketBeans");
//			int id = (int) session.getAttribute("id");
//			//ストックがいくつあるのか確認
//			Item itemStock =itemRepository.getReferenceById(id);
//			int itemStockNum = itemStock.getStock();
//			
//			for (BasketBean itemInBasket : basketItemList) {
//				if (itemInBasket.getId() == id) {
//				int newOrderNum = itemInBasket.getOrderNum();
//				
//					//注文数が在庫を上回るとき
//					if (newOrderNum > itemStockNum) {
//						session.setAttribute("itemNameListLessThan", itemStock.getName());
//					} 
//				}
//			}
//			
//			session.removeAttribute("id");
//		}
		
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
		

	
	
	//商品のかごへの追加
	@RequestMapping(path ="/client/basket/add", method = RequestMethod.POST)
	public String addBasket(Integer id, Model model, RedirectAttributes re) {
		//セッションスコープ内にリスト要素がない場合、リストを作る
		@SuppressWarnings("unchecked")
		List<BasketBean> basketItemList = (List<BasketBean>) session.getAttribute("basketBeans");
		if (basketItemList == null) {
			basketItemList = new ArrayList<>();
		}
		
		
		//反転している要素をもとに戻す
		Collections.reverse(basketItemList);
		
		//ストックがいくつあるのか確認
		Item itemStock =itemRepository.getReferenceById(id);
		int itemStockNum = itemStock.getStock();
		
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
						
						//小計の計算
						int price = itemAddToBasket.getPrice();
						int priceSum = itemAddToBasket.getPriceSum();
						
						
						//注文数が在庫を上回るとき
						if (newOrderNum > itemStockNum) {
//							session.setAttribute("id", id);
//							session.setAttribute("i", 0);
							re.addFlashAttribute("itemNameListLessThan", itemAddToBasket.getName());
						} else {
							itemAddToBasket.setOrderNum(newOrderNum);
							itemAddToBasket.setPriceSum(priceSum + price);
						}
						existItemBasket = true;
					}
				}
//			}
			
			//かご内に追加したい商品が存在しない場合
			if (!existItemBasket) {
				
				Item item = itemRepository.getReferenceById(id);
				itemAddToBasket = new BasketBean(item.getId(), item.getName(), item.getStock(),1, item.getPrice());
				//買い物かごリストに追加
				basketItemList.add(itemAddToBasket);
			}
			
			//要素を反転する
			Collections.reverse(basketItemList);
			
			//セッションスコープにリスト情報を追加
			session.setAttribute("basketBeans", basketItemList);
			
			//合計金額を出す
			int priceSum = 0;
			for (BasketBean itemInBasket : basketItemList) {
				int orderNum = itemInBasket.getOrderNum();
				int price = itemInBasket.getPrice();
				priceSum = priceSum + (orderNum * price);
			}
			session.setAttribute("priceSum", priceSum);
//		} else {
			//在庫がない場合
//			model.addAttribute("itemNameListZero", itemStock.getName());
//		}
		
//		session.removeAttribute("id");
			
		return "redirect:/client/basket/list";
	}
	
	//かご内の全件削除
	@RequestMapping(path ="/client/basket/allDelete", method = RequestMethod.POST)
	public String allDelete() {
		session.removeAttribute("basketBeans");
		session.removeAttribute("priceSum");
		return "redirect:/client/basket/list";
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
		int priceSum = (int) session.getAttribute("priceSum");
		
		for (BasketBean itemInBasket : basketItemList) {
		
			if (itemInBasket.getId() == id) {
				itemAddToBasket = itemInBasket;
				int newOrderNum = itemAddToBasket.getOrderNum() - 1;
				itemAddToBasket.setOrderNum(newOrderNum);
				
				//小計計算
				int price = itemAddToBasket.getPrice();
				int priceS = itemAddToBasket.getPriceSum();
				itemAddToBasket.setPriceSum(priceS - price);
				
				//合計金額から減らした金額を引く
				priceSum = priceSum - itemAddToBasket.getPrice();
				session.setAttribute("priceSum", priceSum);
				
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
			session.removeAttribute("priceSum");
			session.removeAttribute("basketBeans");
		} else {
			//要素を反転する
			Collections.reverse(basketItemList);
		
			//セッションスコープにリスト情報を追加
			session.setAttribute("basketBeans", basketItemList);
		}
		
		
		return "redirect:/client/basket/list";
	}
	
	//削除ボタンが選択された場合　商品の削除
	@RequestMapping(path ="/client/basket/oneDelete", method = RequestMethod.POST)
	public String oneDelete(Integer id) {
		@SuppressWarnings("unchecked")
		List<BasketBean> basketItemList = (List<BasketBean>) session.getAttribute("basketBeans");
		
		//反転している要素を元に戻す
		Collections.reverse(basketItemList);
		
		int i = 0;
		int priceSum = (int) session.getAttribute("priceSum");
		
		for (BasketBean itemInBasket : basketItemList) {
		
			if (itemInBasket.getId() == id) {
				
				//削除したリストの小計を計算する
				BasketBean bean = basketItemList.get(i);
				int price = bean.getPrice();
				int order = bean.getOrderNum();
				priceSum = priceSum - (price * order);
				
				session.setAttribute("priceSum", priceSum);
				
				basketItemList.remove(i);
				break;
			}
			
			i = i + 1;
		}
		
		//要素が入っているか確認
		boolean boo =  basketItemList.isEmpty();
		
		if (boo == true) {
			session.removeAttribute("priceSum");
			session.removeAttribute("basketBeans");
		} else {
			//要素を反転する
			Collections.reverse(basketItemList);
		
			//セッションスコープにリスト情報を追加
			session.setAttribute("basketBeans", basketItemList);
		}
		return "redirect:/client/basket/list";
	}
	

}
