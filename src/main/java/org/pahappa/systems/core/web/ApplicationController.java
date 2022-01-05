package org.pahappa.systems.core.web;

import javax.servlet.ServletContext;
import org.pahappa.systems.core.services.PaymentService;
import org.pahappa.systems.security.HyperLinks;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.web.controllers.AbstractApplicationController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ApplicationController extends AbstractApplicationController {

    @Autowired
    private ServletContext context;

    @RequestMapping(value = {"/Index"})
    public ModelAndView indexHandler(ModelMap model) {
        return new ModelAndView("Index", model);
    }

    @RequestMapping(value = {"/Error"})
    public ModelAndView errorHandler(ModelMap model) {
        return new ModelAndView("Error", model);
    }

    @RequestMapping(value = {HyperLinks.REGISTER})
    public ModelAndView getRegister(ModelMap model) {
        return new ModelAndView(HyperLinks.REGISTER.replace("/", ""), model);
    }

    @RequestMapping(value = HyperLinks.PAYMENT_COMPLETION_VIEW)
    public ModelAndView getRavePayment(@RequestParam("tx_ref") String tx_ref,
            @RequestParam("transaction_id") String transaction_id,
            @RequestParam("status") String status,
            ModelMap model) {
        System.out.println("***********Transcation response***********************");
        System.out.println("tx_ref: " + tx_ref);
        System.out.println("transaction_id: " + transaction_id);
        System.out.println("Status: " + status);
       
        try {
            ApplicationContextProvider.getBean(PaymentService.class).updatePayment(tx_ref, transaction_id);
             model.addAttribute("successfullPaymet",true);
             model.addAttribute("description", "You shall recieve a confirmation email shoartly");
        } catch (Exception e) {
            System.err.println("Some error occurred\n" + e.getLocalizedMessage());
              model.addAttribute("title","Some error occurred");
             model.addAttribute("description",e.getLocalizedMessage());
        } finally {
            return new ModelAndView(HyperLinks.PAYMENT_COMPLETION_VIEW.replace("/", ""), model);
        }

    }

    public String getAppContextUrl() {
        return context.getContextPath();
    }

}
