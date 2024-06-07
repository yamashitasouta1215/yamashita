package jp.co.sss.shop.controller.client.basket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;

@Controller
public class ClientBasketController {
	@Autowired
	HttpSession session;
	
	//買い物かごタブをクリックした際のメソッド
	//かご内の一覧表示
	@RequestMapping(path ="client/basket/list", method = RequestMethod.GET)
	public String ShowBasket() {
		
		@SuppressWarnings("unchecked")
		
	}
}
