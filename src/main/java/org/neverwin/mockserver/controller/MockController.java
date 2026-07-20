package org.neverwin.mockserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.neverwin.mockserver.service.CacheService;
import org.neverwin.mockserver.service.MockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class MockController {

    //    private final TemplateEngineHelper templateHelper;
    private final MockService mockService;
    private final CacheService cacheService;

    @RequestMapping(value = "/**")
    public ResponseEntity<Object> handleMockRequest(HttpServletRequest request) {
        return mockService.execute(request);
    }

    @GetMapping(value = "admin/clear-cache")
    public ResponseEntity<Object> setting() {
        cacheService.clearAllCache();
        return new ResponseEntity<>("CACHE CLEAR", HttpStatus.OK);
    }

//    @GetMapping(value = "setting")
//    public ResponseEntity<Object> setting() {
//        String templateFromDatabase = """
//                {
//                  "messageId": "${messageId}",
//                  "sender": "${senderName}",
//                  "details": [
//                    <#list details as detail>
//                    "${detail?json_string}"<#if detail_has_next>,</#if>
//                    </#list>
//                  ]
//                }
//                """;
//
//        // 3. Siapkan data spesifik untuk transaksi saat ini
//        Map<String, Object> data = new HashMap<>();
//        data.put("messageId", UUID.randomUUID().toString());
//        data.put("senderName", "Service-Pembayaran");
//        data.put("details", Arrays.asList("Validasi sukses", "Saldo dipotong", "Notifikasi dikirim"));
//
//        String response = templateHelper.render(templateFromDatabase, data);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("TAMVAN", "OK");
//
//        try {
//            Thread.sleep(Duration.ofMillis(5000));
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        return new ResponseEntity<>(
//                response,
//                headers,
//                HttpStatus.valueOf("OK")
//        );
//    }

}
