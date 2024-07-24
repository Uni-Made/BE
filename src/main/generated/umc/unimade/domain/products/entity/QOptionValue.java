package umc.unimade.domain.products.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOptionValue is a Querydsl query type for OptionValue
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOptionValue extends EntityPathBase<OptionValue> {

    private static final long serialVersionUID = 830489725L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOptionValue optionValue = new QOptionValue("optionValue");

    public final umc.unimade.global.common.QBaseEntity _super = new umc.unimade.global.common.QBaseEntity(this);

    public final QOptionCategory category;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath value = createString("value");

    public QOptionValue(String variable) {
        this(OptionValue.class, forVariable(variable), INITS);
    }

    public QOptionValue(Path<? extends OptionValue> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOptionValue(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOptionValue(PathMetadata metadata, PathInits inits) {
        this(OptionValue.class, metadata, inits);
    }

    public QOptionValue(Class<? extends OptionValue> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new QOptionCategory(forProperty("category"), inits.get("category")) : null;
    }

}

