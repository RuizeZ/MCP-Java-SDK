package com.example.mcpShoppingList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
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

    @Tool(name = "getItems",
            description = "Get all items currently in the shopping list. Returns a list of items with their names and quantities")
    public List<ShoppingItem> getItems() {
        return new ArrayList<>(shoppingList.values());
    }

    @Tool(name = "removeItem",
            description = "Remove a specified quantity of an item from the shopping list. Specify the item name and the quantity to remove. If the new quantity after the remove is less than 0, the item is removed from the list ")
    public String removeItem(String name, int quantity) {
        if (name == null || name.trim().isEmpty()) {
            return "Error: Invalid item name.";
        }

        name = name.toLowerCase();
        ShoppingItem item = shoppingList.get(name);

        if (item == null) {
            return "Error: item" + name + " is not in the list.";
        }

        quantity = Math.max(item.quantity() - quantity, 0);

        if (quantity == 0) {
            shoppingList.remove(name);
            return "Removed " + name + "from the list";
        } else {
            shoppingList.put(name, new ShoppingItem(name, quantity));
            return "the number of " + name + " in the list is" + quantity;
        }
    }
}
