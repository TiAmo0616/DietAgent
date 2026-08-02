package com.diet.controller.mcp;

import com.diet.tool.mcp.McpReadOnlyToolGateway;
import com.diet.tool.mcp.McpToolDescriptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** MCP discovery endpoint; execution remains behind the authenticated gateway. */
@RestController
@RequestMapping("/api/v1/mcp")
public class McpToolController {
    private final McpReadOnlyToolGateway gateway;

    public McpToolController(McpReadOnlyToolGateway gateway) {
        this.gateway = gateway;
    }

    @GetMapping("/tools")
    public List<McpToolDescriptor> listTools() {
        return gateway.listTools();
    }
}
