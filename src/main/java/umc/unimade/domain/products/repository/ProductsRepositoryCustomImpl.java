package umc.unimade.domain.products.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import umc.unimade.domain.products.entity.ProductStatus;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.products.entity.QProducts;
import com.querydsl.core.types.OrderSpecifier;
import umc.unimade.domain.products.entity.SortType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class ProductsRepositoryCustomImpl implements ProductsRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Products> findProductsByFilters(List<Long> categoryIds, String keyword, Long minPrice, Long maxPrice, SortType sort, String cursor, int pageSize) {
        QProducts products = QProducts.products;

        CursorInfo cursorInfo = extractCursorAndId(cursor);

        BooleanExpression cursorCondition = cursorCondition(sort, products, cursorInfo.getCursorValue(), cursorInfo.getCursorId());

        return queryFactory
                .selectFrom(products)
                .where(
                        categoryIn(categoryIds),
                        keywordContains(keyword),
                        priceBetween(minPrice, maxPrice),
                        statusEq(ProductStatus.SELLING),
                        cursorCondition
                )
                .orderBy(getSortOrder(sort, products))
                .limit(pageSize)
                .fetch();
    }


    private BooleanExpression categoryIn(List<Long> categoryIds) {
        return categoryIds != null && !categoryIds.isEmpty() ? QProducts.products.category.id.in(categoryIds) : null;
    }

    private BooleanExpression keywordContains(String keyword) {
        return keyword != null ? QProducts.products.name.containsIgnoreCase(keyword)
                .or(QProducts.products.content.containsIgnoreCase(keyword)) : null;
    }

    private BooleanExpression priceBetween(Long minPrice, Long maxPrice) {
        if (minPrice == null && maxPrice == null) {
            return null;
        }
        if (minPrice == null) {
            return QProducts.products.price.loe(maxPrice);
        }
        if (maxPrice == null) {
            return QProducts.products.price.goe(minPrice);
        }
        return QProducts.products.price.between(minPrice, maxPrice);
    }

    private BooleanExpression statusEq(ProductStatus status) {
        return QProducts.products.status.eq(status);
    }

    private CursorInfo extractCursorAndId(String cursor) {
        if (cursor == null || cursor.isEmpty()) {
            return new CursorInfo(null, null);
        }
        try {
            String[] parts = cursor.split("_");
            String cursorKey = parts[0];
            Long cursorId = parts.length > 1 ? Long.parseLong(parts[1]) : null;

            if ("nextCursor".equals(cursorKey)) {
                return new CursorInfo(cursorId, cursorId);  // LATEST의 경우 cursorValue는 cursorId로 설정
            }

            Object cursorValue = parseCursorValue(cursorKey);
            return new CursorInfo(cursorValue, cursorId);
        } catch (Exception e) {
            return new CursorInfo(null, null);
        }
    }


    private Object parseCursorValue(String cursorPart) {
        if (cursorPart.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return LocalDate.parse(cursorPart);
        } else if (cursorPart.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")) {
            return LocalDateTime.parse(cursorPart);
        } else {
            return Integer.parseInt(cursorPart);
        }
    }


    private BooleanExpression cursorCondition(SortType sort, QProducts products, Object cursorValue, Long cursorId) {
        if (cursorValue == null || cursorId == null) {
            return null;
        }

        return switch (sort) {
            case FAVORITE -> products.totalFavorite.lt((Integer) cursorValue)
                    .or(products.totalFavorite.eq((Integer) cursorValue)
                            .and(products.id.lt(cursorId)));
            case LATEST -> products.id.lt(cursorId);
            case DEADLINE -> products.deadline.gt((LocalDate) cursorValue)
                    .or(products.deadline.eq((LocalDate) cursorValue)
                            .and(products.id.lt(cursorId)));
        };
    }

    private OrderSpecifier<?>[] getSortOrder(SortType sort, QProducts products) {
        if (sort == null) {
            sort = SortType.FAVORITE;
        }
        return switch (sort) {
            case FAVORITE -> new OrderSpecifier[]{
                    products.totalFavorite.desc(),
                    products.createdAt.desc(),
                    products.id.desc()
            };
            case LATEST -> new OrderSpecifier[]{
                    products.id.desc()
            };
            case DEADLINE -> new OrderSpecifier[]{
                    products.deadline.asc(),
                    products.id.desc()
            };
        };
    }
    @Getter
    private static class CursorInfo {
        private final Object cursorValue;
        private final Long cursorId;

        public CursorInfo(Object cursorValue, Long cursorId) {
            this.cursorValue = cursorValue;
            this.cursorId = cursorId;
        }

    }
}