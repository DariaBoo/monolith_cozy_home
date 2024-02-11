package com.cozyhome.onlineshop.keepaliveserver;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/ping")
public class KeepAliveController {
	
	@GetMapping()
	public ResponseEntity<String> pingServer() {
	    log.warn("[ON pingServer] :: Received a keep-alive request at /api/v1/ping");
	    return ResponseEntity.ok().build();
	}
}
