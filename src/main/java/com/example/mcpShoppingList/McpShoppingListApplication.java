package com.example.mcpShoppingList;

import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class McpShoppingListApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpShoppingListApplication.class, args);
    }

    @Bean
    public List<ToolCallback> tools(ShoppingCart shoppingCart) {
		return List.of(ToolCallbacks.from(shoppingCart));
    }
}
