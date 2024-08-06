package umc.unimade.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.global.common.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "review_report")
public class ReviewReport extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_report_id", nullable = false)
    private Long id;

    @Column(name = "status")
    private ReportStatus status; // 정지 상태

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ReportType type;

    @Column(name = "description")
    private String description;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @PostPersist
    private void setStatus() {
        status = ReportStatus.FREE;
    }

    public void processReport(LocalDateTime now, ReportStatus newStatus) {
        processedAt = now;
        status = newStatus;
    }

    public void changeStatus(ReportStatus newStatus) {
        status = newStatus;
    }

}