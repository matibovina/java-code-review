package schwarz.jobs.interview.coupon.core.domain;

import java.math.BigDecimal;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //---NEW CODE
    private Long id;

    @NotNull //---NEW CODE
    @Column(unique = true) //---NEW CODE
    private String code;

    @NotNull //---NEW CODE
    @Column(precision = 10, scale = 2) //---NEW CODE
    private BigDecimal discount;

    @Column(precision = 10, scale = 2) //---NEW CODE
    private BigDecimal minBasketValue;

}
