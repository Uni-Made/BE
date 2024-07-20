package umc.unimade.domain.orders.dto;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    private Long productId;
    private PurchaseFormRequest purchaseForm;
    private List<OrderOptionRequest> orderOptions;
}
