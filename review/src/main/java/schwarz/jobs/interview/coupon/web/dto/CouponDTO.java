package schwarz.jobs.interview.coupon.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CouponDTO {

    private BigDecimal discount;

    private String code;

    private BigDecimal minBasketValue;

    private LocalDate expirationDate;

}
