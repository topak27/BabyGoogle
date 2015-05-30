package com.clone.google.web;

import com.clone.google.entity.Item;
import com.clone.google.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;


@Controller
public class IndexController {

    @Autowired
    ItemService itemService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String index(@RequestParam(value="q", required=true) String url, Model model) throws IOException {
        Item item = this.itemService.index(url);
        model.addAttribute("item", item);
        return "indexed";
    }
}
