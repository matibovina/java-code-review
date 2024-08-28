package schwarz.jobs.interview.coupon.core.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.exception.InvalidDiscountException;
import schwarz.jobs.interview.coupon.core.model.BasketBO;
import schwarz.jobs.interview.coupon.core.repository.CouponRepository;
import schwarz.jobs.interview.coupon.core.services.CouponService;
import schwarz.jobs.interview.coupon.web.dto.CouponRequestDTO;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    // --- NEW CODE
    @Override
    public BasketBO applyCouponDiscount(final BasketBO basket, final String code) {

        final Coupon coupon = couponRepository.findByCode(code).orElseThrow(
                () -> new InvalidDiscountException("Coupon not found for code: " + code));

        if (basket.getValue().doubleValue() == 0 || basket.getValue().compareTo(coupon.getMinBasketValue()) < 0) {
            basket.setApplicationSuccessful(false);
        } else {
            basket.applyDiscount(coupon.getDiscount());
        }
        return basket;
    }

    // --- NEW CODE
    @Override
    public Coupon createCoupon(final Coupon coupon) {
        return couponRepository.save(coupon);
    }

    // --- NEW CODE
    @Override
    @Transactional(readOnly = true)
    public List<Coupon> getCoupons(final CouponRequestDTO couponRequestDTO) {

        return couponRepository.findByCodeIn(couponRequestDTO.getCodes());
    }
}
