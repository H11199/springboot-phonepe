package org.example.service;

import com.phonepe.sdk.pg.common.http.PhonePeResponse;
import com.phonepe.sdk.pg.payments.v1.PhonePePaymentClient;
import com.phonepe.sdk.pg.payments.v1.models.request.PgPayRequest;
import com.phonepe.sdk.pg.payments.v1.models.response.PayPageInstrumentResponse;
import com.phonepe.sdk.pg.payments.v1.models.response.PgPayResponse;
import com.phonepe.sdk.pg.payments.v1.models.response.PgTransactionStatusResponse;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.example.config.PhonePeProperties;
import org.example.config.RazorpayProperties;
import org.example.model.Payment;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PaymentService {



    private final Map<String, Payment> payments ;

    /**
     * Represents the PhonePe payment client used for processing payments.
     * This client is expected to be initialized only once and remains constant
     * throughout the lifecycle of the class.
     */
    @Autowired
    private PhonePePaymentClient phonepeClient;
    /**
     * Represents the PhonePe payment properties used for processing payments.
     * This client is expected to be initialized only once and remains constant
     * throughout the lifecycle of the class.
     */
    @Autowired
    private PhonePeProperties phonePeProperties;


    @Autowired
    private RazorpayClient razorpayClient;
    @Autowired
    private RazorpayProperties razorpayProperties;

    private final String KEY_ID ="rzp_test_XDw7UM5Ne41EzZ";

    @Value("${razorpay.rzp_key_id}")
    private String key3 ;

    private final String SECRET_KEY = "IGKt94kKjt3gn9keKjTJEddO";


    public PaymentService() {
        this.payments = new ConcurrentHashMap<>();
       // this.razorpayProperties = new RazorpayProperties();

    }

    /**
     * Initiates payment process by generating a redirect URL for the pay page.
     *                    redirect request. Should not be null.
     * @return A RedirectView pointing to the generated Pay Page URL.
     * @param payment
     * @see PgPayRequest
     * @see PgPayResponse
     * @see PayPageInstrumentResponse
     */
    public URL pay(final Payment payment) throws MalformedURLException {

        this.payments.put(payment.getTransactionId(), payment);

        PgPayRequest pgPayRequest = PgPayRequest.PayPagePayRequestBuilder()
                .amount(payment.getAmount())
                .merchantUserId(payment.getUserName())
                .merchantId(phonePeProperties.merchantId())
                .merchantTransactionId(payment.getTransactionId())
                .callbackUrl(this.phonePeProperties.callbackUrl())
                .redirectUrl(this.phonePeProperties.callbackUrl())
                .redirectMode("POST")
                .build();

        PhonePeResponse<PgPayResponse> payResponse =
                this.phonepeClient.pay(pgPayRequest);
        PayPageInstrumentResponse payPageInstrumentResponse =
                (PayPageInstrumentResponse) payResponse.getData()
                        .getInstrumentResponse();
        System.out.println(payPageInstrumentResponse);
        return new URL(payPageInstrumentResponse.getRedirectInfo().getUrl());
    }

    /**
     * Handles the return URL callback for payment notifications.
     * @param code
     * @param merchantId
     * @param providerReferenceId
     * @param transactionId
     * @return The name of the view to be rendered, typically "index".
     */
    public Object getStatus(final String code,
                            final String transactionId,
                            final String merchantId,
                            final String providerReferenceId,
                            final long amount) {
        if (code.equals("PAYMENT_SUCCESS")
                && merchantId.equals(this.phonePeProperties.merchantId())
                && transactionId != null
                && providerReferenceId != null) {
            Payment payment = this.payments.get(transactionId);
            if (amount == payment.getAmount()) {
                PhonePeResponse<PgTransactionStatusResponse> statusResponse
                        = this.phonepeClient.checkStatus(transactionId);
                return statusResponse.getData();
            } else {
                throw new IllegalArgumentException("Payment invalid " + transactionId);
            }

        }
        return null;
    }
    public String createOrder(JSONObject object){
        Order order = null;
        try{
           // RazorpayClient razorpayClient = new RazorpayClient(KEY_ID, SECRET_KEY);
            order = razorpayClient.orders.create(object);
        }catch (RazorpayException e){
            e.printStackTrace();
        }
        return order.get("id");
    }
}