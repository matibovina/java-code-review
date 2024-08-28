package schwarz.jobs.interview.coupon.core.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.model.BasketBO;
import schwarz.jobs.interview.coupon.core.repository.CouponRepository;
import schwarz.jobs.interview.coupon.core.services.impl.CouponServiceImpl;
import schwarz.jobs.interview.coupon.web.dto.CouponRequestDTO;

@ExtendWith(SpringExtension.class)
class CouponServiceImplTest {

    @InjectMocks
    private CouponServiceImpl couponServiceImpl;

    @Mock
    private CouponRepository couponRepository;

    private Coupon coupon;
    private Coupon coupon2;


    private BasketBO basketBO;

    CouponServiceImplTest() {
    }

    @BeforeEach
    void setUp() {
        coupon = Coupon.builder()
                .code("12345")
                .discount(BigDecimal.TEN)
                .minBasketValue(BigDecimal.valueOf(50))
                .build();

        coupon2 = Coupon.builder()
                .code("1111")
                .discount(BigDecimal.TEN)
                .minBasketValue(BigDecimal.valueOf(100))
                .build();

        basketBO = BasketBO.builder()
                .value(BigDecimal.valueOf(100))
                .build();

        when(couponRepository.findByCode("1111")).thenReturn(Optional.of(coupon2));
    }

    @Test
    void shouldCreateCoupon() {

        when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);

        Coupon result = couponServiceImpl.createCoupon(coupon);

        assertThat(result).isNotNull();
        assertEquals(coupon.getCode(), result.getCode());
        assertEquals(coupon.getDiscount(), result.getDiscount());
        assertEquals(coupon.getMinBasketValue(), result.getMinBasketValue());
        verify(couponRepository, times(1)).save(any());


    }

    @Test
    void shouldApplyDiscount() {

        basketBO.setValue(BigDecimal.valueOf(100));

        BasketBO result = couponServiceImpl.applyCouponDiscount(basketBO, "1111");

        assertThat(result).isNotNull();
        assertThat(result.getAppliedDiscount()).isEqualTo(BigDecimal.TEN);
        assertThat(result.isApplicationSuccessful()).isTrue();
    }

    @Test
    void test_apply_coupon_method() {

        basketBO.setValue(BigDecimal.valueOf(0));

        BasketBO result = couponServiceImpl.applyCouponDiscount(basketBO, "1111");

        assertThat(result).isEqualTo(basketBO);
        assertThat(result.getAppliedDiscount()).isNull();
        assertThat(result.isApplicationSuccessful()).isFalse();

    }

    @Test
    void shouldThrowInvalidDiscountException() {

        basketBO.setValue(BigDecimal.valueOf(100));

        assertThatThrownBy(() -> {
            couponServiceImpl.applyCouponDiscount(basketBO, "11");
        }).isInstanceOf(RuntimeException.class)
                .hasMessage("Coupon not found for code: 11");
    }

    @Test
    void shouldReturnListOfCoupons() {

        CouponRequestDTO dto = CouponRequestDTO.builder()
                .codes(Arrays.asList("1111", "12345"))
                .build();

        List<Coupon> coupons = List.of(coupon, coupon2);

        when(couponRepository.findByCodeIn(anyList()))
                .thenReturn(coupons);

        List<Coupon> returnedCoupons = couponServiceImpl.getCoupons(dto);

        assertThat(returnedCoupons.get(0).getCode()).isEqualTo("12345");

        assertThat(returnedCoupons.get(1).getCode()).isEqualTo("1111");

    }

    @Test
    void shouldReturnEmptyList() {

        CouponRequestDTO dto = CouponRequestDTO.builder()
                .codes(Arrays.asList("1111", "1234"))
                .build();

        when(couponRepository.findByCodeIn(anyList()))
                .thenReturn(Collections.emptyList());

        List<Coupon> returnedCoupons = couponServiceImpl.getCoupons(dto);

        assertTrue(returnedCoupons.isEmpty());
    }
}
