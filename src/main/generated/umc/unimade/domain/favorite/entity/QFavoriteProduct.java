package umc.unimade.domain.favorite.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFavoriteProduct is a Querydsl query type for FavoriteProduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFavoriteProduct extends EntityPathBase<FavoriteProduct> {

    private static final long serialVersionUID = 1023155836L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFavoriteProduct favoriteProduct = new QFavoriteProduct("favoriteProduct");

    public final umc.unimade.global.common.QBaseEntity _super = new umc.unimade.global.common.QBaseEntity(this);

    public final umc.unimade.domain.accounts.entity.QBuyer buyer;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final umc.unimade.domain.products.entity.QProducts product;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QFavoriteProduct(String variable) {
        this(FavoriteProduct.class, forVariable(variable), INITS);
    }

    public QFavoriteProduct(Path<? extends FavoriteProduct> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFavoriteProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFavoriteProduct(PathMetadata metadata, PathInits inits) {
        this(FavoriteProduct.class, metadata, inits);
    }

    public QFavoriteProduct(Class<? extends FavoriteProduct> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.buyer = inits.isInitialized("buyer") ? new umc.unimade.domain.accounts.entity.QBuyer(forProperty("buyer")) : null;
        this.product = inits.isInitialized("product") ? new umc.unimade.domain.products.entity.QProducts(forProperty("product"), inits.get("product")) : null;
    }

}

