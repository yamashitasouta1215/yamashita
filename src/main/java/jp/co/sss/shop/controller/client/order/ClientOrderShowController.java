package jp.co.sss.shop.controller.client.order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.repository.OrderRepository;

@Controller
public class ClientOrderShowController {
	
@Autowired
OrderItemRepository OIrepository;

@Autowired
OrderRepository Orepository;

	
@RequestMapping("/client/order/list")
	public String list(Model model,UserForm form) {
				
	//ユーザIDを取得
	//そのユーザIDを使用してオーダーId取得
	//オーダーIDからオーダーの全データを取得してスコープ保存
	

////	System.out.print(userId);
//	Integer userID=(Integer) session.getAttribute("userId");
////	System.out.print(userId);
//	int userID=form.getId();
//	System.out.print(userID);
	model.addAttribute("orders",Orepository.findAll());
	
//	model.addAttribute("total",);
	
	
	 return "client/order/list";
	 
 }
//	
	
//	/client/order/detail/{id}
//	
//	
}
