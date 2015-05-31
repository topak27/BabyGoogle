package com.clone.google.web;

import com.clone.google.entity.Item;
import com.clone.google.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "search";
    }

    @RequestMapping(value = "/search", method = { RequestMethod.GET, RequestMethod.POST })
    public String search(@RequestParam(value="q", required=true) String query, Model model) {
        List<Item> items = this.itemService.findForQuery(query, 0, 10);
        model.addAttribute("items", items);
        model.addAttribute("query", query);
        return "result";
    }

    @RequestMapping("/more")
    public String more(
            @RequestParam(value="q", required=true) String query,
            @RequestParam(value="page", required=true) int page,
            @RequestParam(value="limit", required=true) int limit,
            Model model
    ) {
        List<Item> items = this.itemService.findForQuery(query, page, limit);
        model.addAttribute("items", items);
        model.addAttribute("query", query);
        return "more";
    }
}
