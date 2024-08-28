package schwarz.jobs.interview.coupon.web.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

// --- NEW CODE
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasketDTO {

    @NotNull
    private BigDecimal value;

    private BigDecimal appliedDiscount;

    private boolean applicationSuccessful;
}
