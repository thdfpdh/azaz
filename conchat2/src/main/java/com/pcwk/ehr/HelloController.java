package com.pcwk.ehr;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelloController {
	
	//http://localhost:8080/ehr/hello/hello.do
	@RequestMapping(value = "/hello/hello.do", method = RequestMethod.GET)
	
	public String hello(Model model) {

		String message = "Hello world!";

		System.out.println("========================");
		System.out.println("=====message===="+message);
		System.out.println("========================");
		
		model.addAttribute("message",message);
		//http://localhost:8080/ehr/hello/hello.do
		
		//hello.jsp호출
		return "hello";

	}
}
