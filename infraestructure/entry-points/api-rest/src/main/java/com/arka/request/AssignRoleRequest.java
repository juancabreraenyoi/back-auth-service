package com.arka.request;

import com.arka.entities.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignRoleRequest {
    @NotNull(message = "El Role es obligatorio")
    private Role role;
}


