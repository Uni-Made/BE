package umc.unimade.domain.orders.dto;
import lombok.*;
import umc.unimade.domain.products.entity.PickupOption;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseFormRequest {
    private String name;
    private String phoneNumber;
    private PickupOption pickupOption;
    private String address;
    private String detailAddress;
    private Boolean isAgree;

}
