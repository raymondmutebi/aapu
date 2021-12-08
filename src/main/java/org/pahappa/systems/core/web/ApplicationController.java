package org.pahappa.systems.core.web;

import org.sers.webutils.server.web.controllers.AbstractApplicationController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ApplicationController extends AbstractApplicationController {

	@RequestMapping(value = { "/Index" })
	public ModelAndView indexHandler(ModelMap model) {
		return new ModelAndView("Index", model);
	}

	@RequestMapping(value = { "/Error" })
	public ModelAndView errorHandler(ModelMap model) {
		return new ModelAndView("Error", model);
	}
}
