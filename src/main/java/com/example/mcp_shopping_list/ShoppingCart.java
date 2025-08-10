package com.example.mcp_shopping_list;

import io.swagger.v3.oas.annotations.servers.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.tool.annotation.Tool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Server
public class ShoppingCart {
    /**
     * 商品细节
     *
     * @param name     商品名
     * @param quantity 数量
     */
    public record ShoppingItem(String name, int quantity) {
    }

    private final Map<String, ShoppingItem> shoppingList = new ConcurrentHashMap<>();

    @Tool(name = "addItem",
            description = "Add an item to the shopping list or update its quantity. Specify item name and quantity")
    public String addItem(String name, int quantity) {
        if (StringUtils.isBlank(name) || quantity < 0) {
            return "Error: Invalid item name or quantity.";
        }
        shoppingList.compute(name.toLowerCase(), (k, v) -> {
            if (v == null) {
                return new ShoppingItem(name, quantity);
            } else {
                return new ShoppingItem(v.name(), v.quantity() + quantity);
            }
        });
        return String.format("Added %d of %s to the shopping list", quantity, name);
    }
}
