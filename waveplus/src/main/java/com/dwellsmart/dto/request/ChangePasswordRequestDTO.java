package com.dwellsmart.dto.request;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordRequestDTO {
    @NotBlank
    @JsonProperty("old_password")
    private String oldPassword;

    @NotBlank
  @JsonProperty("new_password")
    private String newPassword;

    // Getters and Setters
}
