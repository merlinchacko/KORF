package com.assignment.imageservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Merlin
 *
 */
@Controller
public class MainController {

	@RequestMapping("/")
    public String index() {
        return "index";
    }
}
