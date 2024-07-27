package umc.unimade.domain.products.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.products.entity.QProducts;
import umc.unimade.domain.favorite.entity.QFavoriteProduct;
import com.querydsl.core.types.OrderSpecifier;

import java.util.List;

@RequiredArgsConstructor
public class ProductsRepositoryCustomImpl implements ProductsRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Products> findProductsByFilters(String category, String keyword, Long minPrice, Long maxPrice, String sort, Long cursor, int pageSize) {
        QProducts products = QProducts.products;
        QFavoriteProduct favoriteProduct = QFavoriteProduct.favoriteProduct;

        return queryFactory
                .selectFrom(products)
                .leftJoin(favoriteProduct).on(favoriteProduct.product.eq(products))
                .where(
                        categoryEq(category),
                        keywordContains(keyword),
                        priceBetween(minPrice, maxPrice),
                        cursorLessThan(cursor)
                )
                .groupBy(products.id)
                .orderBy(getSortOrder(sort, products, favoriteProduct))
                .limit(pageSize)
                .fetch();
    }

    private BooleanExpression categoryEq(String category) {
        return category != null ? QProducts.products.category.name.eq(category) : null;
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

    private BooleanExpression cursorLessThan(Long cursor) {
        return cursor != null ? QProducts.products.id.lt(cursor) : null;
    }

    private OrderSpecifier<?> getSortOrder(String sort, QProducts products, QFavoriteProduct favoriteProduct) {
        if (sort == null || sort.isEmpty()) {
            sort = "favorite";
        }
        return switch (sort) {
            case "favorite" -> favoriteProduct.id.count().desc();
            case "latest" -> products.createdAt.desc();
            case "deadline" -> products.deadline.asc();
            default -> products.id.desc();
        };
    }
}
