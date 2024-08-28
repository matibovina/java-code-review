package schwarz.jobs.interview.coupon.core.services;

import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.model.BasketBO;
import schwarz.jobs.interview.coupon.web.dto.CouponRequestDTO;

import java.util.List;

public interface CouponService {

    BasketBO applyCouponDiscount(final BasketBO basket, final String code);

    Coupon createCoupon(final Coupon couponDTO);

    List<Coupon> getCoupons(CouponRequestDTO couponRequestDTO);
}
