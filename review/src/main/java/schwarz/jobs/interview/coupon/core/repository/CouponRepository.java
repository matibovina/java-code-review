package schwarz.jobs.interview.coupon.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import schwarz.jobs.interview.coupon.core.domain.Coupon;

@Repository // --- NEW CODE
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCode(final String code);

    List<Coupon> findByCodeIn(List<String> codes);


}
