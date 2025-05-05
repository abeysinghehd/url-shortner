package codetest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlRequestDto {
    @NotBlank(message = "Original URL cannot be blank")
    private String originalUrl;
}