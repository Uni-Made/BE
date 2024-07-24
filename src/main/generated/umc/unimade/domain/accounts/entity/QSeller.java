package umc.unimade.domain.accounts.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSeller is a Querydsl query type for Seller
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSeller extends EntityPathBase<Seller> {

    private static final long serialVersionUID = 678704992L;

    public static final QSeller seller = new QSeller("seller");

    public final umc.unimade.global.common.QBaseEntity _super = new umc.unimade.global.common.QBaseEntity(this);

    public final ListPath<umc.unimade.domain.qna.entity.Answers, umc.unimade.domain.qna.entity.QAnswers> answers = this.<umc.unimade.domain.qna.entity.Answers, umc.unimade.domain.qna.entity.QAnswers>createList("answers", umc.unimade.domain.qna.entity.Answers.class, umc.unimade.domain.qna.entity.QAnswers.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final ListPath<umc.unimade.domain.favorite.entity.FavoriteSeller, umc.unimade.domain.favorite.entity.QFavoriteSeller> favoriteSellers = this.<umc.unimade.domain.favorite.entity.FavoriteSeller, umc.unimade.domain.favorite.entity.QFavoriteSeller>createList("favoriteSellers", umc.unimade.domain.favorite.entity.FavoriteSeller.class, umc.unimade.domain.favorite.entity.QFavoriteSeller.class, PathInits.DIRECT2);

    public final EnumPath<Gender> gender = createEnum("gender", Gender.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final ListPath<umc.unimade.domain.products.entity.ProductRegister, umc.unimade.domain.products.entity.QProductRegister> productRegister = this.<umc.unimade.domain.products.entity.ProductRegister, umc.unimade.domain.products.entity.QProductRegister>createList("productRegister", umc.unimade.domain.products.entity.ProductRegister.class, umc.unimade.domain.products.entity.QProductRegister.class, PathInits.DIRECT2);

    public final ListPath<umc.unimade.domain.products.entity.Products, umc.unimade.domain.products.entity.QProducts> products = this.<umc.unimade.domain.products.entity.Products, umc.unimade.domain.products.entity.QProducts>createList("products", umc.unimade.domain.products.entity.Products.class, umc.unimade.domain.products.entity.QProducts.class, PathInits.DIRECT2);

    public final StringPath profileImage = createString("profileImage");

    public final EnumPath<Provider> provider = createEnum("provider", Provider.class);

    public final EnumPath<Role> role = createEnum("role", Role.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSeller(String variable) {
        super(Seller.class, forVariable(variable));
    }

    public QSeller(Path<? extends Seller> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSeller(PathMetadata metadata) {
        super(Seller.class, metadata);
    }

}

