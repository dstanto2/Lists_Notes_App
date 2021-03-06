package com.springexperiment.controller;

import java.security.Principal;

import com.springexperiment.model.UpdateItemList;
import com.springexperiment.validator.UpdateItemListValidator;
import com.springexperiment.model.UpdateItem;

//import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestAttribute;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
//import org.springframework.ui.ExtendedModelMap;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.session.SessionRegistry;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
//import org.springframework.validation.support.BindingAwareModelMap;

import com.springexperiment.dao.ExperimentDao;
import com.springexperiment.model.Item;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@RequestMapping(value="/")
public class ExperimentController {
	private static final Logger logger = LogManager.getLogger(ExperimentController.class);
//	private static final Logger rootLogger = LogManager.getRootLogger();
	private ExperimentDao experimentDao;
	private UpdateItemListValidator updateItemListValidator;
	
	public void setExperimentDao(ExperimentDao experimentDao) {
		this.experimentDao = experimentDao;
	}
	public void setUpdateItemListValidator(UpdateItemListValidator updateItemListValidator) {
		this.updateItemListValidator = updateItemListValidator;
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Set.class, "updateItems", new CustomCollectionEditor(List.class) {
			@Override
			protected Object convertElement(Object element) {
//				System.out.println("in convertElement and element is " + element.toString());
				return null;
			}
		});
		binder.setValidator(updateItemListValidator);
	}
	
	@RequestMapping(value="/")
	public String experiment() {
		return "redirect:/login";
	}
	
	@GetMapping("/login")
	public String login(HttpServletRequest req, HttpServletResponse res) {
		logger.debug("logging in");
		return "login";
	}
//	@PostMapping("/login")
//	public String logIn() {
//		System.out.println("logIn");
//		return "redirect:/items";
//	}
	
	
	@PostMapping("/logoutSave")
	public String logout(@ModelAttribute("updateItems") @Validated UpdateItemList updateItems, BindingResult result, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("log out saving");
		
		if (result.hasErrors()) {
			System.out.println("errors found in input");
		}
		
		for (UpdateItem item : updateItems.getUpdateItems()) {
			switch(item.getType()) {
			case "create":
				experimentDao.createItem(principal.getName(), item);
				break;
			case "update":
				experimentDao.updateItem(principal.getName(), item);
				break;
			case "delete":
				experimentDao.deleteItem(principal.getName(), item);
				break;
			default:
				break;
			}
		}
//		session.invalidate();
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//      if (auth != null) {
//          new SecurityContextLogoutHandler().logout(req, res, auth);
//      }
		
		Cookie cookie = new Cookie("JSESSIONID", null);
		cookie.setPath((request.getContextPath() == "") ? "/" : request.getContextPath() + "/");
		cookie.setMaxAge(0);
		cookie.setSecure(true);
		response.addCookie(cookie);
		
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		SecurityContextHolder.getContext().setAuthentication(null);
		SecurityContextHolder.clearContext();
		
		
		
		return "redirect:/login?logout";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("logging out");
		
//		session.invalidate();
//		
//		Cookie[] cookies = req.getCookies();
//		for (Cookie cookie : cookies) {
//			cookie.setValue("");
//			cookie.setPath("/");
//			cookie.setMaxAge(0);
//			res.addCookie(cookie);
//		}
		
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//	      if (auth != null) {
//	          new SecurityContextLogoutHandler().logout(req, res, auth);
//	      }

		Cookie cookie = new Cookie("JSESSIONID", null);
		cookie.setPath((request.getContextPath() == "") ? "/" : request.getContextPath() + "/");
		cookie.setMaxAge(0);
		cookie.setSecure(true);
		response.addCookie(cookie);
		
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		SecurityContextHolder.getContext().setAuthentication(null);
		SecurityContextHolder.clearContext();
		
		
		return "redirect:/login?logout";
	}

//	@GetMapping("/logout")
//	public String logout(HttpServletRequest request, HttpServletResponse response) {
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null) {
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//          
//        return "redirect:/login?logout";
//	}

	@PostMapping("/signup")
	public ModelAndView signup(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword) {
		logger.debug("signing up");
		ModelAndView mv = new ModelAndView("login");
		if (username.contains(" ")) {
			mv.addObject("errMsg", "invalid username");
		}
		else if (username.length() < 5) {
			mv.addObject("errMsg", "username too short");
		}
		else if (username.length() > 30) {
			mv.addObject("errMsg", "username too long");
		}
		else if (password.contains(" ")) {
			mv.addObject("errMsg", "invalid password");
		}
		else if (password.length() < 8) {
			mv.addObject("errMsg", "password too short");
		}
		else if (password.length() > 64) {
			mv.addObject("errMsg", "password too long");
		}
		else if (!password.equals(confirmPassword)) {
			mv.addObject("errMsg", "passwords must match");
		}
		else {
			mv.addObject("errMsg", experimentDao.signUp(username, password));
		}
		return mv;
	}
	
	@PostMapping("/denied")
	public String denied() {
		logger.debug("denied");
		return "denied";
	}
	
//	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(path = "/updateItem", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> updateItem(@ModelAttribute("updateItems") @Validated UpdateItemList updateItems, BindingResult result, Principal principal) {
		logger.debug("BEGIN SAVING");
		
		System.out.println(result);
		
		if (result.hasErrors()) {
			System.out.println("errors found in input");
		}
		
		for (UpdateItem item : updateItems.getUpdateItems()) {
			switch(item.getType()) {
				case "create":
					experimentDao.createItem(principal.getName(), item);
					break;
				case "update":
					experimentDao.updateItem(principal.getName(), item);
					break;
				case "delete":
					experimentDao.deleteItem(principal.getName(), item);
					break;
				default:
					break;
			}
		}
		logger.debug("END SAVING");
		return new ResponseEntity<>("", HttpStatus.CREATED);
	}
	
	@GetMapping("/items")
	public String items(Model model, Principal principal) {
		logger.debug("getting items");
		
		List<Item> items = experimentDao.loadUserItems(principal.getName());
		List<String> listNames = experimentDao.loadUserListNames(principal.getName());
		List<String> tagNames = experimentDao.loadUserTagNames(principal.getName());
		
//		System.out.println("items: " + items);
//		System.out.println("listNames: " + listNames);
//		System.out.println("tagNames: " + tagNames);
		
		for (Item item : items) {
			item.setCreated(item.getCreated().substring(0,10));
			item.setModified(item.getModified().substring(0,10));
//			System.out.println(item.getCreated());
//			System.out.println(item.getModified());
		}
		
		model.addAttribute("items", items);
		model.addAttribute("listNames", listNames);
		model.addAttribute("tagNames", tagNames);
		model.addAttribute("updateItems", new UpdateItemList());
		
		return "items";
	}
//	@GetMapping("/admin")
//	public String admin() {
//		logger.debug("admin");
//		return "privatePage";
//	}
}
