package umc.unimade.domain.products.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOptionCategory is a Querydsl query type for OptionCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOptionCategory extends EntityPathBase<OptionCategory> {

    private static final long serialVersionUID = -726425998L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOptionCategory optionCategory = new QOptionCategory("optionCategory");

    public final umc.unimade.global.common.QBaseEntity _super = new umc.unimade.global.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final QProducts product;

    public final QProductRegister productRegister;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final ListPath<OptionValue, QOptionValue> values = this.<OptionValue, QOptionValue>createList("values", OptionValue.class, QOptionValue.class, PathInits.DIRECT2);

    public QOptionCategory(String variable) {
        this(OptionCategory.class, forVariable(variable), INITS);
    }

    public QOptionCategory(Path<? extends OptionCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOptionCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOptionCategory(PathMetadata metadata, PathInits inits) {
        this(OptionCategory.class, metadata, inits);
    }

    public QOptionCategory(Class<? extends OptionCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProducts(forProperty("product"), inits.get("product")) : null;
        this.productRegister = inits.isInitialized("productRegister") ? new QProductRegister(forProperty("productRegister"), inits.get("productRegister")) : null;
    }

}

