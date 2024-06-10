package jp.co.sss.shop.controller.client.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.repository.OrderRepository;

@Controller
public class ClientOrderShowController {
	
@Autowired
OrderItemRepository OIrepository;

@Autowired
OrderRepository Orepository;

	
@RequestMapping("/client/order/list")
	public String list(Model model,HttpSession session) {
				
	//ユーザIDを取得
	//そのユーザIDを使用してオーダーId取得
	//オーダーIDからオーダーの全データを取得してスコープ保存
	


	

//Integer userID = ((UserBean) session.getAttribute("userid")).getId();	
//int userID=(int) session.getAttribute("userId");
//
//int userID=form.getId();
//System.out.print(userID);
	model.addAttribute("orders",Orepository.findAll());

//	model.addAttribute("total",);
	
	
	 return "client/order/list";
	 
 }
//	
	
//	/client/order/detail/{id}
//	
//	
}
