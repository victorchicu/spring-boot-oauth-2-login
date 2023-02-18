package com.letmespringyou.springbootoauth2login.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OAuthLoginController {
    @RequestMapping("/login/oauth2")
    public String index() {
        return "sign-in";
    }
}
