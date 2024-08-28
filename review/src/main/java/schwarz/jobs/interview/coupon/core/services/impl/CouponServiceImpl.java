package schwarz.jobs.interview.coupon.core.services.impl;

import java.time.LocalDate;
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

    @Override
    public BasketBO applyCouponDiscount(final BasketBO basket, final String code) {

        final Coupon coupon = couponRepository.findByCode(code.toUpperCase()).orElseThrow(
                () -> new InvalidDiscountException("Coupon not found for code: " + code));

        if (isExpired(coupon)) {
            basket.setApplicationSuccessful(false);
            throw new InvalidDiscountException("Coupon " + code + " is not active.");
        } else if (basket.getValue().doubleValue() == 0 || basket.getValue().compareTo(coupon.getMinBasketValue()) < 0) {
            basket.setApplicationSuccessful(false);
        } else {
            basket.applyDiscount(coupon.getDiscount());
        }
        return basket;
    }

    @Override
    public Coupon createCoupon(final Coupon coupon) {
        coupon.setCode(coupon.getCode().toUpperCase());
        return couponRepository.save(coupon);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Coupon> getCoupons(final CouponRequestDTO couponRequestDTO) {

        return couponRepository.findByCodeIn(couponRequestDTO.getCodes());
    }

    private boolean isExpired(Coupon coupon) {
        return (coupon.getExpirationDate() != null && coupon.getExpirationDate().isBefore(LocalDate.now()));
    }
}
