package umc.unimade.domain.accounts.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.repository.SellerRepository;

@Service
@RequiredArgsConstructor
public class SellerCommandService {

    private final SellerRepository sellerRepository;

    @Transactional
    public void saveDescription(Seller seller, String description) {
        seller.updateDescription(description);
        sellerRepository.save(seller);
    }
}
