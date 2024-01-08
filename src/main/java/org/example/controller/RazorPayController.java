package org.example.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.example.service.PaymentService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RazorPayController {
    @Autowired
    private PaymentService paymentService;

    /**
     *  The default amount for a transaction.
     */
    private static final long DEFAULT_AMOUNT = 100;
    @GetMapping(value = "/pay/createOrder")
    @ResponseBody
    public String createOrder() throws JSONException {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount",DEFAULT_AMOUNT);
        orderRequest.put("currency","INR");
        orderRequest.put("receipt", "receipt#1");
        JSONObject notes = new JSONObject();
        notes.put("notes_key_1","Tea, Earl Grey, Hot");
        orderRequest.put("notes",notes);
        String orderId = paymentService.createOrder(orderRequest);
        System.out.println(orderId);
        return orderId;
    }
}
