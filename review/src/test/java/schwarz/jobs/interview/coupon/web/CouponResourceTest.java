package schwarz.jobs.interview.coupon.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import schwarz.jobs.interview.coupon.core.converter.CouponConverter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CouponResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldApplyDiscountToBasket() throws Exception {
        String applicationRequestDTO = "{ \"basket\": { \"value\": 100 }, \"code\": \"TEST1\" }";

        mockMvc.perform(post("/api/basket")
                        .contentType("application/json")
                        .content(applicationRequestDTO))
                .andExpect(content().json("{\"value\":90,\"appliedDiscount\":10,\"applicationSuccessful\":true}"))
                .andExpect(status().isOk());
    }


    @Test
    void shouldReturnBadRequestError() throws Exception {
        String applicationRequestDTO = "{ \"basket\": { \"value\": 100 }, \"code\": \"TEST\" }";
        mockMvc.perform(post("/api/basket")
                        .contentType("application/json")
                        .content(applicationRequestDTO))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnIsConflict() throws Exception {
        String applicationRequestDTO = "{ \"basket\": { \"value\": 10 }, \"code\": \"TEST1\" }";
        mockMvc.perform(post("/api/basket")
                        .contentType("application/json")
                        .content(applicationRequestDTO))
                .andExpect(content().json("{\"value\":10,\"appliedDiscount\":null,\"applicationSuccessful\":false}"))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnIsBadRequest() throws Exception {
        String applicationRequestDTO = "{ \"basket\": { \"value\": -10 }, \"code\": \"TEST1\" }";
        mockMvc.perform(post("/api/basket")
                        .contentType("application/json")
                        .content(applicationRequestDTO))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateCoupon() throws Exception {
        String couponDTO = "{ \"code\": \"TEST5\", \"discount\": 20, \"minBasketValue\": 100 }";
        mockMvc.perform(post("/api/coupons")
                        .contentType("application/json")
                        .content(couponDTO))
                .andExpect(content().json("{ \"code\": \"TEST5\", \"discount\": 20, \"minBasketValue\": 100 }"))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldGetCoupons() throws Exception {
        String coupons = "{\"codes\": [\"TEST1\", \"TEST2\", \"TEST3\"]}";
        mockMvc.perform(post("/api/coupons/query")
                        .contentType("application/json")
                        .content(coupons))
                .andExpect(content().json(
                        "[{\"discount\":10,\"code\":\"TEST1\",\"minBasketValue\":50}" +
                                ",{\"discount\":15,\"code\":\"TEST2\",\"minBasketValue\":100}" +
                                ",{\"discount\":20,\"code\":\"TEST3\",\"minBasketValue\":200}]"
                ))
                .andExpect(status().isOk());
    }

}