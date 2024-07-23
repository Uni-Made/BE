package umc.unimade.domain.accounts.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBuyer is a Querydsl query type for Buyer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBuyer extends EntityPathBase<Buyer> {

    private static final long serialVersionUID = 283777458L;

    public static final QBuyer buyer = new QBuyer("buyer");

    public final umc.unimade.global.common.QBaseEntity _super = new umc.unimade.global.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final ListPath<umc.unimade.domain.favorite.entity.FavoriteProduct, umc.unimade.domain.favorite.entity.QFavoriteProduct> favoriteProducts = this.<umc.unimade.domain.favorite.entity.FavoriteProduct, umc.unimade.domain.favorite.entity.QFavoriteProduct>createList("favoriteProducts", umc.unimade.domain.favorite.entity.FavoriteProduct.class, umc.unimade.domain.favorite.entity.QFavoriteProduct.class, PathInits.DIRECT2);

    public final ListPath<umc.unimade.domain.favorite.entity.FavoriteSeller, umc.unimade.domain.favorite.entity.QFavoriteSeller> favoriteSellers = this.<umc.unimade.domain.favorite.entity.FavoriteSeller, umc.unimade.domain.favorite.entity.QFavoriteSeller>createList("favoriteSellers", umc.unimade.domain.favorite.entity.FavoriteSeller.class, umc.unimade.domain.favorite.entity.QFavoriteSeller.class, PathInits.DIRECT2);

    public final EnumPath<Gender> gender = createEnum("gender", Gender.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<umc.unimade.domain.orders.entity.Orders, umc.unimade.domain.orders.entity.QOrders> orders = this.<umc.unimade.domain.orders.entity.Orders, umc.unimade.domain.orders.entity.QOrders>createList("orders", umc.unimade.domain.orders.entity.Orders.class, umc.unimade.domain.orders.entity.QOrders.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final StringPath profileImage = createString("profileImage");

    public final EnumPath<Provider> provider = createEnum("provider", Provider.class);

    public final ListPath<umc.unimade.domain.qna.entity.Questions, umc.unimade.domain.qna.entity.QQuestions> questions = this.<umc.unimade.domain.qna.entity.Questions, umc.unimade.domain.qna.entity.QQuestions>createList("questions", umc.unimade.domain.qna.entity.Questions.class, umc.unimade.domain.qna.entity.QQuestions.class, PathInits.DIRECT2);

    public final ListPath<umc.unimade.domain.review.entity.Review, umc.unimade.domain.review.entity.QReview> reviews = this.<umc.unimade.domain.review.entity.Review, umc.unimade.domain.review.entity.QReview>createList("reviews", umc.unimade.domain.review.entity.Review.class, umc.unimade.domain.review.entity.QReview.class, PathInits.DIRECT2);

    public final EnumPath<Role> role = createEnum("role", Role.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QBuyer(String variable) {
        super(Buyer.class, forVariable(variable));
    }

    public QBuyer(Path<? extends Buyer> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBuyer(PathMetadata metadata) {
        super(Buyer.class, metadata);
    }

}

