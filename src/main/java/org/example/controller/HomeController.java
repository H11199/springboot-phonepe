package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    /**
     * Handles requests to the root ("/") and "/index" endpoints,
     *                     rendering the index view.
     * @param model The Model object.
     * @return The name of the view to be rendered, typically "index".
     */
    @GetMapping(value = {"/", "/index2"})
    public String index(final Model model) {
        model.addAttribute("title", "My Title");
        return "index2";
    }


    @GetMapping("/phonepe")
    public String switchPhonepe() {
        return "index";
    }


    @GetMapping("/razorpay")
    public String razorpay(final Model model){
        model.addAttribute("title", "My Title");
        return "razorpay";
    }

    @PostMapping("/rsuccess/{amount}/{desc}")
    public String paymentsucces(final Model model, @PathVariable("amount") String amount
    , @PathVariable("desc") String desc){
    model.addAttribute("amount", amount);
    model.addAttribute("desc", desc);
     return "razor_success";
    }

    @GetMapping("/rpay")
    public String razorpay2(final Model model)
    {
        return "razor_success";
    }



}
