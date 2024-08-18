package com.example.prac4_db_application.web

import com.example.prac4_db_application.domain.Item
import com.example.prac4_db_application.repository.ItemSearchCond
import com.example.prac4_db_application.repository.ItemUpdateDto
import com.example.prac4_db_application.service.ItemService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/items")
class ItemController (private val itemService: ItemService ){

    @GetMapping
    fun items(@ModelAttribute("itemSearch") itemSearch : ItemSearchCond, model : Model): String {
        val items = itemService.findItems(itemSearch)
        model.addAttribute("items", items)
        return "items"
    }

    @GetMapping("/{itemId}")
    fun item(@PathVariable("itemId") itemId : Long, model : Model) : String {
        val item = itemService.findById(itemId)
        model.addAttribute("item",item )
        return "item"
    }

    @GetMapping("/add")
    fun addForm(): String {
        return "addForm"
    }

    @PostMapping("/add")
    fun addItem(@ModelAttribute item: Item, redirectAttribute: RedirectAttributes): String {
        val savedItem = itemService.save(item)
        redirectAttribute.addAttribute("itemId",savedItem.id)
        redirectAttribute.addAttribute("status","true")
        return "redirect:/items/{itemId}"
    }

    @GetMapping("/{itemId}/edit")
    fun editForm(@PathVariable itemId : Long, model: Model): String {
        model.addAttribute("item", itemService.findById(itemId))
        return "editForm"
    }

    @PostMapping("/{itemId}/edit")
    fun edit(@PathVariable itemId : Long, @ModelAttribute updateParam : ItemUpdateDto): String {
        itemService.update(itemId, updateParam)
        return "redirect:/items/{itemId}"
    }
}