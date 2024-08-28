package schwarz.jobs.interview.coupon.core.converter;

import org.springframework.stereotype.Component;
import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.model.BasketBO;
import schwarz.jobs.interview.coupon.web.dto.BasketDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;

// --- NEW CODE
@Component
public class CouponConverter {

    public BasketBO convertToBasket(BasketDTO basketDTO) {

        return BasketBO.builder()
                .value(basketDTO.getValue())
                .appliedDiscount(basketDTO.getAppliedDiscount())
                .applicationSuccessful(basketDTO.isApplicationSuccessful())
                .build();
    }

    public BasketDTO convertToBasketDTO(BasketBO basket) {
        BasketDTO basketDTO = new BasketDTO();
        basketDTO.setValue(basket.getValue());
        basketDTO.setAppliedDiscount(basket.getAppliedDiscount());
        basketDTO.setApplicationSuccessful(basket.isApplicationSuccessful());
        return basketDTO;
    }

    public CouponDTO convertToCouponDTO(Coupon coupon) {
        return CouponDTO.builder()
                .code(coupon.getCode())
                .discount(coupon.getDiscount())
                .minBasketValue(coupon.getMinBasketValue())
                .build();
    }

    public Coupon convertToCoupon(CouponDTO coupon) {
        return Coupon.builder()
                .code(coupon.getCode())
                .discount(coupon.getDiscount())
                .minBasketValue(coupon.getMinBasketValue())
                .build();
    }
}
