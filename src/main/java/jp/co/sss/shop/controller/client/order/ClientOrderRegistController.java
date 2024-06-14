package jp.co.sss.shop.controller.client.order;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.bean.OrderItemBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.OrderForm;
import jp.co.sss.shop.repository.ArtistRepository;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.repository.UserRepository;

@Controller
public class ClientOrderRegistController {
	
//	private static final OrderItemBean List = null;

	@Autowired
	HttpSession session;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	OrderItemRepository orderitemRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ArtistRepository artistRepository;
	
	/*
	 * 住所入力処理→買い物かごへ
	 * 「戻る」ボタン押下時
	 */

	@RequestMapping(path="/client/basket/list",method=RequestMethod.POST)
	public String basketback() {
		
		return "client/basket/list";
	}

	/*
	 * 処理１
	 */
	@RequestMapping(path="/client/order/address/input",method=RequestMethod.POST)
	public String addressinput(OrderForm form) {
		
		/*
		 * 注文フォーム生成
		 * 初期値登録（ログインユーザ情報）
		 */
		UserBean user = (UserBean) session.getAttribute("user");
		User order = (User) userRepository.getReferenceById(user.getId());
		
		form.setAddress(order.getAddress());
		form.setId(order.getId());
		form.setName(order.getName());
		form.setPhoneNumber(order.getPhoneNumber());
		form.setPostalCode(order.getPostalCode());
		form.setPayMethod(1);
		
		
		session.setAttribute("orderForm", form);
		
		return "redirect:/client/order/address/input";
		
		
	}
	
	/*
	 * 処理２
	 */
	@RequestMapping(path="/client/order/address/input",method=RequestMethod.GET)
	public String addressinput(@ModelAttribute OrderForm form,Model model) {
		
		OrderForm orderform = (OrderForm) session.getAttribute("orderForm");
		model.addAttribute("orderForm",orderform);
		
		
//		・セッションスコープに入力エラー情報がある場合
//		- 取得したエラー情報をリクエストスコープに設定
//		- セッションスコープから、エラー情報を削除

		BindingResult result = (BindingResult) session.getAttribute("result");
		if(result != null) {
			model.addAttribute("org.springframework.validation.BindingResult.orderForm",result);
			session.removeAttribute("result");
		}

		return "client/order/address_input";
		
	}
	
	/*
	 * 	処理３
	 */
	@RequestMapping(path="/client/order/payment/input",method=RequestMethod.POST)
	public String paymentinput(@Valid @ModelAttribute OrderForm form,BindingResult result) {
		
		/*
		 * フォームに入力された情報をセッション（orderForm)に保存
		 */
		form.setAddress(form.getAddress());
		form.setPhoneNumber(form.getPhoneNumber());
		form.setPostalCode(form.getPostalCode());
		form.setName(form.getName());
		
		session.setAttribute("orderForm", form);
		

		if(result.hasErrors()) {
		session.setAttribute("result",result);
			
		return "redirect:/client/order/address/input";
		
		}		
		
		return "redirect:/client/order/payment/input";
			
	}
	
	
	/*
	 * 	処理４
	 */
	@RequestMapping(path="/client/order/payment/input",method=RequestMethod.GET)
	public String payment(Model model) {
		
		/*
		 * 支払情報の初期値にクレジットカードを設定
		 */
		OrderForm orderform = (OrderForm) session.getAttribute("orderForm");
		session.setAttribute("orderForm",orderform);
		model.addAttribute("payMethod",orderform.getPayMethod());
		
		return "client/order/payment_input";
		
	}
	
	/*
	 * 処理５
	 */
	@RequestMapping(path="/client/order/check",method=RequestMethod.POST)
	public String ordercheck(Integer payMethod) {
		
		/*
		 * 入力された支払情報をセッション(orderForm）に保存
		 */
		OrderForm orderform = (OrderForm) session.getAttribute("orderForm");
		orderform.setPayMethod(payMethod);
		session.setAttribute("orderForm", orderform);
		
		return "redirect:/client/order/check";
	}
	
	
	/*
	 * 処理６
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@RequestMapping(path="/client/order/check",method=RequestMethod.GET)
	public String check(OrderForm form,Model model) {

//		全額合計
		int total =0;
//		商品ごと小計
		int Allprice = 0;
		ArrayList <BasketBean> newBaskets = new ArrayList<>();
		
		OrderForm orderform = new OrderForm();
		orderform = (OrderForm) session.getAttribute("orderForm");
		
		
		Item items = new Item();
		/*
		 * セッションスコープより買い物かご情報取得
		 */
		 ArrayList<BasketBean> basketBean = new ArrayList<>();
		 
		 basketBean =(ArrayList<BasketBean>) session.getAttribute("basketBeans");
		
		 
		 List<OrderItemBean> orderitemBeanList = new ArrayList<OrderItemBean>();
//		 orderitemBeanList = null;
		 /*
		  * 買い物かごの種類分だけ
		  */
		for(int i=0;i < basketBean.size() ;i++) {
		
			BasketBean basket = basketBean.get(i);
	
			/*
			 * かごの商品情報をItemエンティィへ格納
			 */
			items=itemRepository.getReferenceById(basket.getId());
		
			String itemName = items.getName();
			
			int price = items.getPrice();
			int orderNum = basket.getOrderNum();
			int stock = items.getStock();
			
			OrderItemBean orderitemBean = new OrderItemBean();
			/*
			 * 在庫数との照らし合わせ
			 */
			
			
			
				if(stock == 0) {
				
					/*
					 * 在庫0のため商品削除
					 */
						model.addAttribute("itemNameListZero",itemName);
						items = null;
						
						} else if(orderNum > stock){
						
						/*
						 * 在庫数と注文数を合わせる
						 */
						model.addAttribute("itemNameListLessThan",itemName);
						orderNum = stock;
						basket.setOrderNum(stock);
						Allprice = price * orderNum;
						newBaskets.add(basket);
						
						
						} else {
						/*
						 * 必要処理なし
						 */
						newBaskets.add(basket);
						Allprice= price * orderNum;
						
				}

				
				
					if(items != null) {
						orderitemBean.setSubtotal(Allprice);
						orderitemBean.setOrderNum(orderNum);
						orderitemBean.setArtistName(items.getArtist().getName());

				
						BeanUtils.copyProperties(items, orderitemBean);
						orderitemBeanList.add(orderitemBean);
						
						
						/*
						 * 商品の合計金額を表示
						 */
						total = total + Allprice;
						model.addAttribute("total",total);
					}
					
		//for文終わりの ｝		
		}
		
		/*
		 * 注文確認画面表示用注文内容リクエストスコープ
		 */
		
		model.addAttribute("orderItemBeans",orderitemBeanList);	
		
		
		
		/*
		 * 商品が全く入っていない場合の処理
		 * 確定ボタン表示、非表示用
		 */
		if(newBaskets.size() == 0) {
			model.addAttribute("sizeNull",null);
		} else {
			model.addAttribute("sizeNull"," ");
		}
		
		/*
		 * 注文確認画面表示用ユーザー登録リクエストスコープ
		 * 
		 */
		model.addAttribute("orderForm",session.getAttribute("orderForm"));
			
		/*
		 * アーティスト画像表示用リクエストスコープ
		 */
				
		/*
		 * 在庫数反映後買い物かご情報
		 * basketBeansを更新。不足商品を買い物かごから除外する
		 */
		session.setAttribute("orderItemBeans",newBaskets);
		session.setAttribute("basketBeans", newBaskets);	
		
			
		
		return "client/order/check";
			
	}
	
	
	/*
	 * 「買い物かごへ戻る」ボタン押下時
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(path="/client/order/back",method=RequestMethod.POST)
	public String cartback() {
		
	
	session.removeAttribute("orderForm");
	
	
	List<OrderItem> orderItemList = new ArrayList<>();
	 orderItemList =(List<OrderItem>) session.getAttribute("basketBeans");
	 
	 
	if(orderItemList.size() == 0) {
		System.out.println(orderItemList);
		session.removeAttribute("basketBeans");
		
	}
	 
	 return "redirect:/client/basket/list";
	}
	
	
	/*
	 * 処理７
	 */
	@RequestMapping(path="/client/order/payment/back",method=RequestMethod.POST)
	public String paymentback(Model model,OrderForm form) {
		
		return "redirect:/client/order/address/input";
	}
	
	
	/*
	 * check→paymentリダイレクト処理
	 */
	@RequestMapping(path="/client/order/payment/back/2",method=RequestMethod.POST)
	public String checkback() {
		return "redirect:/client/order/payment/input";
	}
	
	
	
	/*
	 * 注文確定ボタン押下時在庫数チェック
	 * ストック数0または個数不足のときclient/order/checkへリダイレクト
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(path="/client/order/complete",method=RequestMethod.POST)
	public String FinalOrderCheck(Model model) {
		
		
	ArrayList<BasketBean> baskets = new ArrayList<>();
	 baskets =(ArrayList<BasketBean>) session.getAttribute("orderItemBeans");

	 Item items = new Item();

	 
	for(int i=0;i<baskets.size();i++) {
		
		BasketBean basket = baskets.get(i);
		
		items=itemRepository.getReferenceById(basket.getId());
		
		String itemName = items.getName();
		
		int orderNums = basket.getOrderNum();
		int stock = items.getStock();
		
		
		if(stock == 0) {
			
	
			model.addAttribute("itemNameListZero",itemName);
			
			if(i != baskets.size()-1) {
				continue;
			}
			return "redirect:/client/order/check";
			
			
		}else if(orderNums > stock) {
			model.addAttribute("itemNameListLessThan",itemName);
			
			if(i != baskets.size()-1) {
				continue;
			}
			
			return "redirect:/client/order/check";
		}
	}
	return "redirect:/client/order/completed";
}
	
	
	/*
	 * 処理８
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(path="/client/order/completed",method=RequestMethod.GET)
	public String OrderComplete(Model model) {
		
		/*
		 * ユーザーの注文情報をDBに登録する
		 */
		Order order = new Order();
		
		OrderForm orderform = (OrderForm) session.getAttribute("orderForm");
		BeanUtils.copyProperties(orderform,order,"id");
		
		
		/*
		 * 本日の日付をDBに登録する
		 */
		Date date= new Date(0);
		order.setInsertDate(date);
		
		/*
		 * 注文情報の取得、DBへ登録
		 */
		List<OrderItem> orderItemList = new ArrayList<>();
		 orderItemList =(List<OrderItem>) session.getAttribute("orderItemBeans");
		order.setOrderItemsList(orderItemList);
		
		
		/*
		 * ユーザ情報取得、DBへ登録
		 */
		UserBean user=(UserBean) session.getAttribute("user");
		User userd = (User) userRepository.getReferenceById(user.getId());
		order.setUser(userd);
		
		
		/*
		 * ユーザ情報をorderテーブルへ登録
		 */
		orderRepository.save(order);

		
		/*
		 * 注文商品DB登録
		 */
		
//		買い物かご情報を取得
		ArrayList<BasketBean> baskets = new ArrayList<>();
		 baskets =(ArrayList<BasketBean>) session.getAttribute("orderItemBeans");

		 
//		 注文情報表示、DB登録のため実体化（List型）
		 ArrayList<OrderItem> orderitem = new ArrayList<>();
		OrderItem item = new OrderItem();
		
		
//		注文詳細検索、Itemテーブルstock数反映 のための実体化
		Item items = new Item();
		
		
		/*
		 * 在庫数との照らし合わせ
		 * 在庫切れになった場合、確認画面へリダイレクトする
		 */

		for(int i=0;i<baskets.size();i++) {
			
			BasketBean basket = baskets.get(i);
			
			int stock = basket.getStock();

			items=itemRepository.getReferenceById(basket.getId());

			int id = items.getId();
			int orderNum = basket.getOrderNum();
			int orderPrice = items.getPrice();

			item.setId(id);
			item.setPrice(orderPrice);
			item.setQuantity(orderNum);
			item.setOrder(order);
			item.setItem(items);
			
			/*
			 * OrderItemテーブルへDB登録
			 */
			orderitem.add(item);
			BeanUtils.copyProperties(order, orderitem,"id");
			orderitem = (ArrayList<OrderItem>) orderitemRepository.saveAll(orderitem);
			
			
			/*
			 * Itemテーブルのstock数を減らすDB登録
			 */
			int newStock = stock - orderNum;
			items.setStock(newStock);
			
			//ストックが0になった際、論理削除を行う
			if(newStock == 0) {
				items.setDeleteFlag(1);
			}
			
			items = itemRepository.save(items);
			
		/*
		 * for文終わりの ｝
		 */
		}
		
		/*
		 * 注文登録用セッション破棄
		 */
		session.removeAttribute("orderForm");
		session.removeAttribute("orderItemBeans");
		session.removeAttribute("basketBeans");
		
		
		return "redirect:/client/order/completes";
		
	}
		
	
	/*
	 * 処理９
	 * 注文確認画面へ
	 */
	@RequestMapping(path="/client/order/completes",method=RequestMethod.GET)
	public String complete() {
		
	
		return "client/order/complete";
	}
	
}
	
