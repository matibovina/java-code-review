package schwarz.jobs.interview.coupon.core.converter;

import org.springframework.stereotype.Component;
import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.model.BasketBO;
import schwarz.jobs.interview.coupon.web.dto.BasketDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;

@Component
public class CouponConverter {

    public BasketBO convertToBasket(BasketDTO basketDTO) {

        return BasketBO.builder()
                .value(basketDTO.getValue())
                .appliedDiscount(basketDTO.getAppliedDiscount())
                .applicationSuccessful(basketDTO.isApplicationSuccessful())
                .build();
    }

    public BasketDTO convertToBasketDTO(BasketBO basketBO) {
        BasketDTO basketDTO = new BasketDTO();
        basketDTO.setValue(basketBO.getValue());
        basketDTO.setAppliedDiscount(basketBO.getAppliedDiscount());
        basketDTO.setApplicationSuccessful(basketBO.isApplicationSuccessful());
        return basketDTO;
    }

    public CouponDTO convertToCouponDTO(Coupon coupon) {
        return CouponDTO.builder()
                .code(coupon.getCode())
                .discount(coupon.getDiscount())
                .minBasketValue(coupon.getMinBasketValue())
                .expirationDate(coupon.getExpirationDate())
                .build();
    }

    public Coupon convertToCoupon(CouponDTO couponDTO) {
        return Coupon.builder()
                .code(couponDTO.getCode())
                .discount(couponDTO.getDiscount())
                .minBasketValue(couponDTO.getMinBasketValue())
                .expirationDate(couponDTO.getExpirationDate())
                .build();
    }
}
