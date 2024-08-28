package schwarz.jobs.interview.coupon.web.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationRequestDTO {

    @NotBlank
    private String code;

    // --- NEW CODE
    @NotNull
    private BasketDTO basket;
}
