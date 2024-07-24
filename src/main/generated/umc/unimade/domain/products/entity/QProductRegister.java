package umc.unimade.domain.products.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductRegister is a Querydsl query type for ProductRegister
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductRegister extends EntityPathBase<ProductRegister> {

    private static final long serialVersionUID = 1131807411L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductRegister productRegister = new QProductRegister("productRegister");

    public final StringPath accountName = createString("accountName");

    public final StringPath accountNumber = createString("accountNumber");

    public final StringPath bankName = createString("bankName");

    public final QCategory category;

    public final StringPath content = createString("content");

    public final DatePath<java.time.LocalDate> deadline = createDate("deadline", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<OptionCategory, QOptionCategory> optionCategories = this.<OptionCategory, QOptionCategory>createList("optionCategories", OptionCategory.class, QOptionCategory.class, PathInits.DIRECT2);

    public final EnumPath<PickupOption> pickupOption = createEnum("pickupOption", PickupOption.class);

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final ListPath<ProductsImage, QProductsImage> productImages = this.<ProductsImage, QProductsImage>createList("productImages", ProductsImage.class, QProductsImage.class, PathInits.DIRECT2);

    public final StringPath reason = createString("reason");

    public final EnumPath<umc.unimade.global.registerStatus.RegisterStatus> registerStatus = createEnum("registerStatus", umc.unimade.global.registerStatus.RegisterStatus.class);

    public final umc.unimade.domain.accounts.entity.QSeller seller;

    public final EnumPath<ProductStatus> status = createEnum("status", ProductStatus.class);

    public final StringPath university = createString("university");

    public QProductRegister(String variable) {
        this(ProductRegister.class, forVariable(variable), INITS);
    }

    public QProductRegister(Path<? extends ProductRegister> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductRegister(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductRegister(PathMetadata metadata, PathInits inits) {
        this(ProductRegister.class, metadata, inits);
    }

    public QProductRegister(Class<? extends ProductRegister> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new QCategory(forProperty("category")) : null;
        this.seller = inits.isInitialized("seller") ? new umc.unimade.domain.accounts.entity.QSeller(forProperty("seller")) : null;
    }

}

