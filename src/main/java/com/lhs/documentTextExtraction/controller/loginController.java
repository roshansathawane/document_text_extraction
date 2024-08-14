package com.lhs.documentTextExtraction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lhs.documentTextExtraction.entity.UserMast;
import com.lhs.documentTextExtraction.repo.UserMastRepo;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/api")
public class loginController {
	
	
	@Autowired
    private UserMastRepo repo;


	
//	@GetMapping(value = { "/login" })
	@GetMapping("/login")
	public String login() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}


		return "loginPage";
	}// login method end
	
	
	


//	@GetMapping("/loginAuth")
	@RequestMapping(value = "/loginAuth", method = { RequestMethod.GET, RequestMethod.POST })
	public String ValidateUser(ModelMap model, UserMast user,
			@RequestParam(value = "login_id", required = false) String login_id,
			@RequestParam(value = "login_pwd", required = false) String login_pwd, RedirectAttributes redirectAttr) {

		List<UserMast> list = new ArrayList<>();
		System.out.println("login_id======="+login_id);
		System.out.println("login_pwd======="+login_pwd);
		String  user_code = null;
	 
		
		 list = repo.validatedUser(login_id, login_pwd);
			System.out.println("list--->"+list);
			
		String redirectAtion = "redirect:/loginPage";
		try {

			if (user != null ) {
				 if(login_id != null && login_pwd != null) {
					 					 
					 list = repo.validatedUser(login_id, login_pwd);
					System.out.println("list--->"+list);
										 
				 }
				
				if (list != null &&  !list.isEmpty()) {
					System.out.println("Validate User");

					return redirectAtion = "redirect:/documentUpload";
				} else {
					
					System.out.println("Not Validate User");
					redirectAttr.addFlashAttribute("errorMessage1", "Invalid Login ID or Password provided.");
					//return redirectAtion = "redirect:/loginForm";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return redirectAtion;
	}


	
	
	
	 @GetMapping("/userForm")
	    public String showUserForm(UserMast userMast) {
		 System.out.println(" inside..........userForm");
	        return "userform";
	    }

	 @PostMapping("/saveUser")
	 public String saveUser(@ModelAttribute UserMast userMast) {
		 repo.save(userMast);
	     return "redirect:/userList";
	 }

	    @GetMapping("/userList")
	    public String showUserList(Model model) {
	    	List<UserMast> all = repo.findAll();
	    	
	        model.addAttribute("users", all );
	        return "userList";
	    }
	
}
	 
