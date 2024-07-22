package umc.unimade.global.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CursorPageRequest {
    private Long cursor;
    private int pageSize;
}
