package umc.unimade.domain.accounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import umc.unimade.domain.accounts.entity.Provider;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.global.security.UserLoginForm;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    @Query("SELECT s.email AS id, s.role AS role FROM Seller s WHERE s.email = :sellerId AND  s.refreshToken is not null")
    Optional<UserLoginForm> findByEmailAndRefreshToken(@Param("sellerId") String sellerId);

    @Query("SELECT s.email AS id, s.role AS role FROM Seller s WHERE s.email = :sellerId AND s.password = :password")
    Optional<UserLoginForm> findByEmailAndPassword(@Param("sellerId") String sellerId, @Param("password") String password);

    Seller findByEmailAndRefreshTokenIsNotNullAndIsLoginIsTrue(String email);
    Seller findByEmailAndProvider(String socialId, Provider provider);

    Optional<Seller> findByEmailAndPasswordAndProvider(String email, String password, Provider provider);

    Optional<Seller> findByEmail(String email);
}
