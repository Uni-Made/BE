package umc.unimade.domain.orders.dto;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderOptionRequest {
    private Long OptionId;
    private Long OptionValueId;
    private Long count;
}
