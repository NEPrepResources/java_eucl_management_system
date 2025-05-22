package com.eucl.eucl_management_system.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object containing a simple message")
public class MessageResponse {
    @Schema(description = "Message content", example = "Operation successful", required = true)
    private String message;

    public MessageResponse(String message) { this.message = message; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}