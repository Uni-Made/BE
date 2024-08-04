package umc.unimade.domain.accounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.entity.Provider;
import umc.unimade.global.security.UserLoginForm;

import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {

    @Query("SELECT b.socialId as id, b.role AS role FROM Buyer b WHERE b.socialId = :socialId AND  b.refreshToken is not null")
    Optional<UserLoginForm> findBySocialIdAndRefreshToken(@Param("socialId") String socialId);

    @Query("SELECT b.socialId as id, b.role AS role FROM Buyer b WHERE b.socialId = :socialId AND b.refreshToken = :refreshToken")
    Optional<UserLoginForm> findBySocialIdAndRefreshToken(@Param("socialId") String socialId, @Param("refreshToken") String refreshToken);

    Buyer findBySocialIdAndRefreshTokenIsNotNullAndIsLoginIsTrue(String socialId);

    Buyer findBySocialIdAndProvider(String socialId, Provider provider);

    Optional<Buyer> findBySocialId(String socialId);

    Optional<Buyer> findByEmail(String email);
}
