package com.example.vehicle_auction.infrastructure.payment.vnpay;

import com.example.vehicle_auction.application.dto.payment.PaymentRequest;
import com.example.vehicle_auction.application.dto.payment.PaymentResponse;
import com.example.vehicle_auction.application.dto.payment.RefundRequest;
import com.example.vehicle_auction.application.port.out.PaymentGatewayPort;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class VnpayAdapter implements PaymentGatewayPort {
    @Value("${vnpay.tmn-code}")
    private String vnpTmnCode;

    @Value("${vnpay.hash-secret}")
    private String vnpHashSecret;

    @Value("${vnpay.url}")
    private String vnpUrl;

    @Value("${vnpay.return-url}")
    private String vnpReturnUrl;

    @Override
    public PaymentResponse createPaymentUrl(PaymentRequest request) {
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnpTmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(request.amount() * 100)); // VNPay requires amount * 100
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", request.referenceId());
        vnp_Params.put("vnp_OrderInfo", request.orderInfo());
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnpReturnUrl);
        vnp_Params.put("vnp_IpAddr", request.ipAddress());

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        vnp_Params.put("vnp_CreateDate", formatter.format(cld.getTime()));

        cld.add(Calendar.MINUTE, 15);
        vnp_Params.put("vnp_ExpireDate", formatter.format(cld.getTime()));

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                hashData.append(fieldName).append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = hmacSHA512(vnpHashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        return new PaymentResponse(vnpUrl + "?" + queryUrl);
    }

    @Override
    public boolean verifyCallback(Map<String, String> callbackParams) {
        Map<String, String> fields = new HashMap<>(callbackParams);
        String vnp_SecureHash = fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();

        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                hashData.append(fieldName).append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    hashData.append('&');
                }
            }
        }

        String signValue = hmacSHA512(vnpHashSecret, hashData.toString());
        return signValue.equals(vnp_SecureHash);
    }

    // Mock refund method
    @Override
    public boolean refund(RefundRequest request) {

        if (request.transactionDate() == null || request.transactionDate().isEmpty()) {
            log.error("❌ CRITICAL ERROR: transactionDate is NULL! Please test with a NEW deposit.");
            return false;
        }
        String vnp_RequestId = UUID.randomUUID().toString();
        String vnp_Version = "2.1.0";
        String vnp_Command = "refund";
        String vnp_TmnCode = this.vnpTmnCode;
        String vnp_TransactionType = request.transactionType();
        String vnp_TxnRef = request.transactionReference();
        String vnp_Amount = String.valueOf(request.amount());
        String vnp_TransactionNo = (request.transactionNo() != null && !request.transactionNo().isEmpty())
                ? request.transactionNo()
                : "0";
        String vnp_TransactionDate = request.transactionDate();
        String vnp_CreateBy = request.createBy();
        String vnp_CreateDate = request.createDate();
        String vnp_IpAddr = "8.8.8.8"; //127.0.0.1
        String vnp_OrderInfo = "Refund transaction " + vnp_TxnRef;

        String hashData = String.join("|",
                vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode,
                vnp_TransactionType, vnp_TxnRef, vnp_Amount, vnp_TransactionNo,
                vnp_TransactionDate, vnp_CreateBy, vnp_CreateDate,
                vnp_IpAddr, vnp_OrderInfo
        );

        String vnp_SecureHash = hmacSHA512(this.vnpHashSecret, hashData);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("vnp_RequestId", vnp_RequestId);
        requestBody.put("vnp_Version", vnp_Version);
        requestBody.put("vnp_Command", vnp_Command);
        requestBody.put("vnp_TmnCode", vnp_TmnCode);
        requestBody.put("vnp_TransactionType", vnp_TransactionType);
        requestBody.put("vnp_TxnRef", vnp_TxnRef);
        requestBody.put("vnp_Amount", request.amount());

        long transNo = (vnp_TransactionNo != null && !vnp_TransactionNo.isEmpty())
                ? Long.parseLong(vnp_TransactionNo)
                : 0L;

        requestBody.put("vnp_TransactionNo", transNo);
        requestBody.put("vnp_TransactionDate", vnp_TransactionDate);
        requestBody.put("vnp_CreateBy", vnp_CreateBy);
        requestBody.put("vnp_CreateDate", vnp_CreateDate);
        requestBody.put("vnp_IpAddr", vnp_IpAddr);
        requestBody.put("vnp_OrderInfo", vnp_OrderInfo);
        requestBody.put("vnp_SecureHash", vnp_SecureHash);

        try {

            ObjectMapper mapper = new ObjectMapper();
            log.info("VNPAY REFUND REQUEST PAYLOAD: {}", mapper.writeValueAsString(requestBody));

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            String vnpapi = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

            log.info("Sending refund request to VNPAY...");

            String responseBody = restTemplate.postForObject(vnpapi, entity, String.class);
            log.info("OFFICIAL RESPONSE FROM VNPAY: {}", responseBody);

            JsonNode jsonNode = mapper.readTree(responseBody);
            String responseCode = jsonNode.get("vnp_ResponseCode").asText();

            if ("00".equals(responseCode)) {
                log.info("✅ REFUND SUCCESSFUL ON VNPAY SYSTEM!");
                return true;
            } else {
                log.error("❌ VNPAY REJECTED REFUND. ERROR CODE: {}, MESSAGE: {}",
                        responseCode, jsonNode.get("vnp_Message").asText());
                return false;
            }

        } catch (Exception e) {
            log.error("Network error or unable to connect to VNPAY API: ", e);
            return false;
        }
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] result = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to generate HMAC-SHA512 for VNPay", ex);
        }
    }


}
