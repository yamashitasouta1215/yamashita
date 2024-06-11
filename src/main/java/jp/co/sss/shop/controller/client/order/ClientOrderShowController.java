package jp.co.sss.shop.controller.client.order;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.bean.OrderBean;
import jp.co.sss.shop.bean.OrderItemBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.service.BeanTools;
import jp.co.sss.shop.service.PriceCalc;

@Controller
public class ClientOrderShowController {
	
@Autowired
OrderItemRepository OIrepository;

@Autowired
OrderRepository Orepository;

	
//@RequestMapping("/client/order/list")
//	public String list(Model model,HttpSession session) {
//				
//
//	//そのユーザIDを使用してオーダーId取得
//	//オーダーIDからオーダーの全データを取得してスコープ保存
//	User user =new User();
//	UserBean userbean=new UserBean();
//	BeanUtils.copyProperties(user, userbean);
//	String userid=session.getId();

//	Object userid=session.getAttribute("user");
//	int num = Integer.parseInt(userid);

	
//	System.out.print(userid);

//Integer userID = ((UserBean) session.getAttribute("userid")).getId();	
//int userID=(int) session.getAttribute("userId");
//	Order order=Orepository.getReferenceById(userid);
	
//	model.addAttribute("orders",order);
//	int orderid=order.getId();
//	OrderItem orderitem=OIrepository.getReferenceById(orderid);
//	model.addAttribute("orders",order);
//	
//	
//
////	model.addAttribute("total",);
//	
//	
//	 return "client/order/list";
//	 
// }
//
//

@Autowired
	HttpSession session;

	/**
	 * 合計金額計算サービス
	 */
	@Autowired
	PriceCalc priceCalc;

	/**
	 * Entity、Form、Bean間のデータ生成、コピーサービス
	 */
	@Autowired
	BeanTools beanTools;
	
@RequestMapping(path = "/client/order/list", method = { RequestMethod.GET, RequestMethod.POST })
public String showOrderList(Model model, Pageable pageable) {

	// すべての注文情報を取得(注文日降順)
	//表示画面でページングが必要なため、ページ情報付きの検索を行う
	Integer userid = ((UserBean) session.getAttribute("user")).getId();

	
	List<Order> orderList = Orepository.findByUserIdOrderByInsertDateDesc(userid);

	// 注文情報リストを生成
	List<OrderBean> orderBeanList = new ArrayList<OrderBean>();
	for (Order order : orderList) {
		// BeanToolsクラスのcopyEntityToOrderBeanメソッドを使用して表示する注文情報を生成
		OrderBean orderBean = beanTools.copyEntityToOrderBean(order);
		//orderレコードから紐づくOrderItemのListを取り出す
		List<OrderItem> orderItemList = order.getOrderItemsList();
		
//		
		
		
		//PriceCalcクラスのorderItemPriceTotalメソッドを使用して合計金額を算出
		int total = priceCalc.orderItemPriceTotal(orderItemList);
//				
		//合計金額のセット
		orderBean.setTotal(total);

		orderBeanList.add(orderBean);
	}
	

	// 注文情報リストをViewへ渡す
//	model.addAttribute("pages", orderList);
	model.addAttribute("orders", orderBeanList);

	return "client/order/list";

}


	@RequestMapping(path = "/client/order/detail/{id}")
	public String showOrder(@PathVariable int id, Model model) {

		// 選択された注文情報に該当する情報を取得
		Order order = Orepository.getReferenceById(id);

		// 表示する注文情報を生成
		OrderBean orderBean = beanTools.copyEntityToOrderBean(order);

		// 注文商品情報を取得
		List<OrderItemBean> orderItemBeanList = beanTools.generateOrderItemBeanList(order.getOrderItemsList());

		// 合計金額を算出
		int total = priceCalc.orderItemBeanPriceTotalUseSubtotal(orderItemBeanList);

		// 注文情報をViewへ渡す
		model.addAttribute("order", orderBean);
		model.addAttribute("orderItemBeans", orderItemBeanList);
		model.addAttribute("total", total);

		return "client/order/detail";
	}

}