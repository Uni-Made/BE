package umc.unimade.domain.products.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductsImage is a Querydsl query type for ProductsImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductsImage extends EntityPathBase<ProductsImage> {

    private static final long serialVersionUID = 1966060696L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductsImage productsImage = new QProductsImage("productsImage");

    public final umc.unimade.global.common.QBaseEntity _super = new umc.unimade.global.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final QProducts product;

    public final QProductRegister productRegister;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QProductsImage(String variable) {
        this(ProductsImage.class, forVariable(variable), INITS);
    }

    public QProductsImage(Path<? extends ProductsImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductsImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductsImage(PathMetadata metadata, PathInits inits) {
        this(ProductsImage.class, metadata, inits);
    }

    public QProductsImage(Class<? extends ProductsImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProducts(forProperty("product"), inits.get("product")) : null;
        this.productRegister = inits.isInitialized("productRegister") ? new QProductRegister(forProperty("productRegister"), inits.get("productRegister")) : null;
    }

}

