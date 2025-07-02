package com.thc.sprbasic2025.controller;

import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.dto.UserDto;
import com.thc.sprbasic2025.service.UserService;
import com.thc.sprbasic2025.util.TokenFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthRestController {

    final TokenFactory tokenFactory;

    private final String prefix = "Bearer ";

    @PostMapping("")
    public ResponseEntity<Void> access(HttpServletRequest request){
        String returnValue = null;
        String refreshToken = request.getHeader("RefreshToken");
        if(refreshToken.startsWith(prefix)){
            refreshToken = refreshToken.substring(prefix.length());
            String accessToken = tokenFactory.generateAccessToken(refreshToken);
            returnValue = prefix + accessToken;
        }
        return ResponseEntity.status(HttpStatus.OK).header("Authorization", returnValue).build();
    }

}
