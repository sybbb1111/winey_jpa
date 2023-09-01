package com.green.winey_final.auth;

import com.green.winey_final.auth.model.SignInReqDto;
import com.green.winey_final.auth.model.AuthResVo;
import com.green.winey_final.auth.model.SignOutReqDto;
import com.green.winey_final.auth.model.SignUpReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원가입, 로그인, 로그아웃, 토큰발급")
@Slf4j
@RestController
@RequestMapping("/sign-api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/sign-up")
    @Operation(summary = "로컬 회원가입")
    public ResponseEntity<AuthResVo> postSignUp(@RequestBody SignUpReqDto dto
            , HttpServletRequest req
            , HttpServletResponse res) {
        AuthResVo vo = service.signUp(dto, req, res);
        log.info("Dto : {}", dto);
        return ResponseEntity.ok(vo);
    }

    @PostMapping("/sign-in")
    @Operation(summary = "로컬 로그인")
    public ResponseEntity<AuthResVo> postSignIn(@RequestBody SignInReqDto dto
            , HttpServletRequest req
            , HttpServletResponse res) {
        AuthResVo vo = service.signIn(dto, req, res);
        return ResponseEntity.ok(vo);
    }

    @GetMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity getSignout(@RequestParam(required = false) String accessToken
            , HttpServletRequest req
            , HttpServletResponse res) {
        service.signOut(accessToken, req, res);
        return ResponseEntity.ok(1);
    }

    @GetMapping("/refresh-token")
    @Operation(summary = "리프레쉬 토큰으로 액세스 토큰 재발급 자동화")
    public ResponseEntity<AuthResVo> getRefresh(HttpServletRequest req) {
        AuthResVo vo = service.refresh(req);
        return ResponseEntity.ok(vo);
    }

}
