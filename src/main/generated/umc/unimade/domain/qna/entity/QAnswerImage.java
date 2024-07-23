package umc.unimade.domain.qna.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAnswerImage is a Querydsl query type for AnswerImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAnswerImage extends EntityPathBase<AnswerImage> {

    private static final long serialVersionUID = 156201530L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAnswerImage answerImage = new QAnswerImage("answerImage");

    public final umc.unimade.global.common.QBaseEntity _super = new umc.unimade.global.common.QBaseEntity(this);

    public final QAnswers answer;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAnswerImage(String variable) {
        this(AnswerImage.class, forVariable(variable), INITS);
    }

    public QAnswerImage(Path<? extends AnswerImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAnswerImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAnswerImage(PathMetadata metadata, PathInits inits) {
        this(AnswerImage.class, metadata, inits);
    }

    public QAnswerImage(Class<? extends AnswerImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.answer = inits.isInitialized("answer") ? new QAnswers(forProperty("answer"), inits.get("answer")) : null;
    }

}

