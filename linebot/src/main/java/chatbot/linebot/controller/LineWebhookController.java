package chatbot.linebot.controller;

import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/callback") //webhook URL

public class LineWebhookController {

    private static final String CHANNEL_SECRET = "MY_CHANNEL_SECRET"; // Replace with your channel secret

    @PostMapping
    public ResponseEntity<String> callback(
                                            @RequestBody String payload,
                                            @RequestHeader("X-Line-Signature") String signature) {
        if  (!validateSignature(payload, signature)) {
            return new ResponseEntity<>("Invalid signature", HttpStatus.FORBIDDEN);
        }
        
        // TODO: Process the payload here

        System.out.println("Received payload: " + payload);
        return new ResponseEntity<>("OK", HttpStatus.OK);
        }
        private boolean validateSignature(String payload, String signature) {
            try{
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(CHANNEL_SECRET.getBytes(), "HmacSHA256"));
                byte[] digest = mac.doFinal(payload.getBytes());
                String encoded = Base64.getEncoder().encodeToString(digest);
                return encoded.equals(signature);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
