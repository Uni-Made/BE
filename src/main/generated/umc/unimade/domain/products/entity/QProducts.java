package umc.unimade.domain.products.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProducts is a Querydsl query type for Products
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProducts extends EntityPathBase<Products> {

    private static final long serialVersionUID = 1667604099L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProducts products = new QProducts("products");

    public final umc.unimade.global.common.QBaseEntity _super = new umc.unimade.global.common.QBaseEntity(this);

    public final StringPath accountName = createString("accountName");

    public final StringPath accountNumber = createString("accountNumber");

    public final StringPath bankName = createString("bankName");

    public final QCategory category;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DatePath<java.time.LocalDate> deadline = createDate("deadline", java.time.LocalDate.class);

    public final ListPath<umc.unimade.domain.favorite.entity.FavoriteProduct, umc.unimade.domain.favorite.entity.QFavoriteProduct> favoriteProducts = this.<umc.unimade.domain.favorite.entity.FavoriteProduct, umc.unimade.domain.favorite.entity.QFavoriteProduct>createList("favoriteProducts", umc.unimade.domain.favorite.entity.FavoriteProduct.class, umc.unimade.domain.favorite.entity.QFavoriteProduct.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<OptionCategory, QOptionCategory> optionCategories = this.<OptionCategory, QOptionCategory>createList("optionCategories", OptionCategory.class, QOptionCategory.class, PathInits.DIRECT2);

    public final ListPath<umc.unimade.domain.orders.entity.Orders, umc.unimade.domain.orders.entity.QOrders> orders = this.<umc.unimade.domain.orders.entity.Orders, umc.unimade.domain.orders.entity.QOrders>createList("orders", umc.unimade.domain.orders.entity.Orders.class, umc.unimade.domain.orders.entity.QOrders.class, PathInits.DIRECT2);

    public final EnumPath<PickupOption> pickupOption = createEnum("pickupOption", PickupOption.class);

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final ListPath<ProductsImage, QProductsImage> productImages = this.<ProductsImage, QProductsImage>createList("productImages", ProductsImage.class, QProductsImage.class, PathInits.DIRECT2);

    public final ListPath<umc.unimade.domain.orders.entity.PurchaseForm, umc.unimade.domain.orders.entity.QPurchaseForm> PurchaseForms = this.<umc.unimade.domain.orders.entity.PurchaseForm, umc.unimade.domain.orders.entity.QPurchaseForm>createList("PurchaseForms", umc.unimade.domain.orders.entity.PurchaseForm.class, umc.unimade.domain.orders.entity.QPurchaseForm.class, PathInits.DIRECT2);

    public final ListPath<umc.unimade.domain.qna.entity.Questions, umc.unimade.domain.qna.entity.QQuestions> questions = this.<umc.unimade.domain.qna.entity.Questions, umc.unimade.domain.qna.entity.QQuestions>createList("questions", umc.unimade.domain.qna.entity.Questions.class, umc.unimade.domain.qna.entity.QQuestions.class, PathInits.DIRECT2);

    public final ListPath<umc.unimade.domain.review.entity.Review, umc.unimade.domain.review.entity.QReview> reviews = this.<umc.unimade.domain.review.entity.Review, umc.unimade.domain.review.entity.QReview>createList("reviews", umc.unimade.domain.review.entity.Review.class, umc.unimade.domain.review.entity.QReview.class, PathInits.DIRECT2);

    public final umc.unimade.domain.accounts.entity.QSeller seller;

    public final EnumPath<ProductStatus> status = createEnum("status", ProductStatus.class);

    public final StringPath university = createString("university");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QProducts(String variable) {
        this(Products.class, forVariable(variable), INITS);
    }

    public QProducts(Path<? extends Products> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProducts(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProducts(PathMetadata metadata, PathInits inits) {
        this(Products.class, metadata, inits);
    }

    public QProducts(Class<? extends Products> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new QCategory(forProperty("category")) : null;
        this.seller = inits.isInitialized("seller") ? new umc.unimade.domain.accounts.entity.QSeller(forProperty("seller")) : null;
    }

}

