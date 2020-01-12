package com.tericcabrel.authorization.controllers;

import com.tericcabrel.authorization.dtos.LoginUserDto;
import com.tericcabrel.authorization.dtos.RefreshTokenDto;
import com.tericcabrel.authorization.dtos.ValidateTokenDto;
import com.tericcabrel.authorization.models.User;
import com.tericcabrel.authorization.models.common.ApiResponse;
import com.tericcabrel.authorization.models.common.AuthToken;
import com.tericcabrel.authorization.models.redis.RefreshToken;
import com.tericcabrel.authorization.repositories.RefreshTokenRepository;
import com.tericcabrel.authorization.services.interfaces.UserService;
import com.tericcabrel.authorization.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Date;
import java.util.HashMap;

import static com.tericcabrel.authorization.utils.Constants.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/token")
public class TokenController {
    private Log logger = LogFactory.getLog(this.getClass());

    private JwtTokenUtil jwtTokenUtil;

    private RefreshTokenRepository refreshTokenRepository;

    private UserService userService;

    public TokenController(
            JwtTokenUtil jwtTokenUtil, RefreshTokenRepository refreshTokenRepository, UserService userService
    ) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
    }

    @PostMapping(value = "/validate")
    public ResponseEntity<ApiResponse> validate(@Valid @RequestBody ValidateTokenDto validateTokenDto) {
        String username = null;
        HashMap<String, String> result = new HashMap<>();

        try {
            username = jwtTokenUtil.getUsernameFromToken(validateTokenDto.getToken());
        } catch (IllegalArgumentException e) {
            logger.error(JWT_ILLEGAL_ARGUMENT_MESSAGE, e);
            result.put("message", "JWT_ILLEGAL_ARGUMENT_MESSAGE");
        } catch (ExpiredJwtException e) {
            logger.warn(JWT_EXPIRED_MESSAGE, e);
            result.put("message", "JWT_EXPIRED_MESSAGE");
        } catch(SignatureException e){
            logger.error(JWT_SIGNATURE_MESSAGE);
            result.put("message", "JWT_SIGNATURE_MESSAGE");
        }

        if (username != null) {
            result.put("message", "success");
            return ResponseEntity.ok(new ApiResponse(200, result));
        }

        return ResponseEntity.badRequest().body(new ApiResponse(400, result));
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<ApiResponse> refresh(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        RefreshToken refreshToken = refreshTokenRepository.findByValue(refreshTokenDto.getToken());
        HashMap<String, String> result = new HashMap<>();

        if (refreshToken == null) {
            result.put("message", "The token is Invalid!");
            return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST.value(), result));
        }

        User user = userService.findById(refreshToken.getId());
        if (user == null) {
            result.put("message", "The token is unallocated!");
            return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST.value(), result));
        }

        String token = jwtTokenUtil.createTokenFromUser(user);
        Date expirationDate = jwtTokenUtil.getExpirationDateFromToken(token);

        return ResponseEntity.ok(
                new ApiResponse(HttpStatus.OK.value(), new AuthToken(token, refreshToken.getValue(), expirationDate.getTime()))
        );
    }
}