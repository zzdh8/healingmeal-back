package com.example.thehealingmeal.member.handler;

import com.example.thehealingmeal.member.domain.PrincipalDetail;
import com.example.thehealingmeal.member.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        PrincipalDetail authUser = (PrincipalDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        session.setAttribute("LOGIN_USER", authUser);

        // set our response to OK status
        response.setStatus(HttpServletResponse.SC_OK);
        String user_id = userService.userID(authentication.getName()).toString();
//        Cookie cookie = new Cookie("user_id", user_id);
//        response.addCookie(cookie);


        // we will redirect the user after successfully login
        response.sendRedirect("/success?user_id=" + user_id);
    }
}
