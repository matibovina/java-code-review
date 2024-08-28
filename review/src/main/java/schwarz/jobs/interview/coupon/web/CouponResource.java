package schwarz.jobs.interview.coupon.web;


import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import schwarz.jobs.interview.coupon.core.converter.CouponConverter;
import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.exception.InvalidDiscountException;
import schwarz.jobs.interview.coupon.core.model.BasketBO;
import schwarz.jobs.interview.coupon.core.services.impl.CouponServiceImpl;
import schwarz.jobs.interview.coupon.web.dto.ApplicationRequestDTO;
import schwarz.jobs.interview.coupon.web.dto.BasketDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponRequestDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class CouponResource {

    private final CouponServiceImpl couponServiceImpl;
    private final CouponConverter couponConverter = new CouponConverter();

    /**
     * @param applicationRequestDTO contains a basket and a coupon code.
     * @return basket or error message
     */
    @ApiOperation(value = "Applies currently active promotions and coupons from the request to the requested BasketBO - Version 1")
    @PostMapping(value = "/basket")
    public ResponseEntity<BasketDTO> applyCoupon(
            @ApiParam(value = "Provides the necessary basket and customer information required for the coupon application", required = true)
            @RequestBody @Valid final ApplicationRequestDTO applicationRequestDTO) {


        if (applicationRequestDTO.getBasket().getValue().doubleValue() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        final BasketDTO basketDTO = applicationRequestDTO.getBasket();

        try {
            log.info("Applying coupon");

            final BasketBO basket =
                    couponServiceImpl.applyCouponDiscount(couponConverter.convertToBasket(basketDTO),
                            applicationRequestDTO.getCode());

            if (!basket.isApplicationSuccessful()) {
                log.warn("Application was not successful - BasketBO value less than 0 or less than coupon´s minimum basket value.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(couponConverter.convertToBasketDTO(basket));
            }

            log.info("Applied coupon successfully");
            return ResponseEntity.ok().body(couponConverter.convertToBasketDTO(basket));
        } catch (InvalidDiscountException e) {
            log.error("ERROR: ".concat(e.getLocalizedMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * @param couponDTO contains all coupon parameters for creation
     * @return couponDTO created or error message.
     */
    @ApiOperation(value = "Creates a coupon from CouponDTO - Version 1")
    @PostMapping("/coupons")
    public ResponseEntity<CouponDTO> createCoupon(
            @ApiParam(value = "Provides coupon information for creating a coupon", required = true)
            @RequestBody @Valid final CouponDTO couponDTO) {

        try {
            final Coupon coupon = couponServiceImpl.createCoupon(couponConverter.convertToCoupon(couponDTO));
            return ResponseEntity.status(HttpStatus.CREATED).body(couponConverter.convertToCouponDTO(coupon));
        } catch (IllegalArgumentException e) {
            log.error("Invalid coupon: ".concat(e.getLocalizedMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Unexpected error: ".concat(e.getLocalizedMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * @param couponRequestDTO contains all coupon´s codes
     * @return a list of coupons
     */
    @ApiOperation(value = "Gets all the coupons requested in request body - Version 1")
    @PostMapping("/coupons/query")
    public List<CouponDTO> getCoupons(
            @ApiParam(value = "A list of strings with the codes of the requested coupons", required = true)
            @RequestBody @Valid final CouponRequestDTO couponRequestDTO) {

        return couponServiceImpl.getCoupons(couponRequestDTO).stream().map(
                couponConverter::convertToCouponDTO
        ).collect(Collectors.toList());
    }

}
