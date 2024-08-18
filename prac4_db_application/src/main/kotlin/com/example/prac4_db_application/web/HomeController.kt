package com.example.prac4_db_application.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class HomeController {

    @RequestMapping("/")
    fun home(): String {
        return "redirect:/items"
    }
}