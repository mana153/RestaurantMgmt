package com.lodgingrestaurant.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String redirectUrl = "/";
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();
            if (authorityName.equals("ROLE_ADMIN")) {
                redirectUrl = "/admin/dashboard";
                break;
            } else if (authorityName.equals("ROLE_MANAGER")) {
                redirectUrl = "/manager/dashboard";
                break;
            } else if (authorityName.equals("ROLE_RECEPTIONIST")) {
                redirectUrl = "/receptionist/dashboard";
                break;
            } else if (authorityName.equals("ROLE_CASHIER")) {
                redirectUrl = "/cashier/dashboard";
                break;
            } else if (authorityName.equals("ROLE_CHEF")) {
                redirectUrl = "/chef/dashboard";
                break;
            } else if (authorityName.equals("ROLE_HOUSEKEEPING")) {
                redirectUrl = "/housekeeping/dashboard";
                break;
            } else if (authorityName.equals("ROLE_GUEST")) {
                redirectUrl = "/guest/dashboard";
                break;
            }
        }
        new SimpleUrlAuthenticationSuccessHandler(redirectUrl).onAuthenticationSuccess(request, response, authentication);
    }
}
