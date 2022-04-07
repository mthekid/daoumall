package com.daou.project.daoumall.controller.auth

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.security.Principal


@Controller
class HomeController {

    @GetMapping(value = ["/", "/index"])
    fun index(): String {
        return "index"
    }

    @GetMapping("/home")
    fun home(principal: Principal, model: Model): String? {
        model.addAttribute("user", principal.name)
        model.addAttribute("roles", (principal as UsernamePasswordAuthenticationToken).authorities)
        return "/home"
    }

    @GetMapping("/admin")
    fun admin(principal: Principal, model: Model): String? {
        model.addAttribute("user", principal.name)
        model.addAttribute("roles", (principal as UsernamePasswordAuthenticationToken).authorities)
        return "/admin"
    }

    @GetMapping("/403")
    fun forbidden(): String {
        return "/403"
    }
}