package umc.unimade.domain.favorite.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFavoriteSeller is a Querydsl query type for FavoriteSeller
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFavoriteSeller extends EntityPathBase<FavoriteSeller> {

    private static final long serialVersionUID = 938088530L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFavoriteSeller favoriteSeller = new QFavoriteSeller("favoriteSeller");

    public final umc.unimade.global.common.QBaseEntity _super = new umc.unimade.global.common.QBaseEntity(this);

    public final umc.unimade.domain.accounts.entity.QBuyer buyer;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final umc.unimade.domain.accounts.entity.QSeller seller;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QFavoriteSeller(String variable) {
        this(FavoriteSeller.class, forVariable(variable), INITS);
    }

    public QFavoriteSeller(Path<? extends FavoriteSeller> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFavoriteSeller(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFavoriteSeller(PathMetadata metadata, PathInits inits) {
        this(FavoriteSeller.class, metadata, inits);
    }

    public QFavoriteSeller(Class<? extends FavoriteSeller> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.buyer = inits.isInitialized("buyer") ? new umc.unimade.domain.accounts.entity.QBuyer(forProperty("buyer")) : null;
        this.seller = inits.isInitialized("seller") ? new umc.unimade.domain.accounts.entity.QSeller(forProperty("seller")) : null;
    }

}

