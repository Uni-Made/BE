package umc.unimade.domain.qna.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAnswers is a Querydsl query type for Answers
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAnswers extends EntityPathBase<Answers> {

    private static final long serialVersionUID = -1113452302L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAnswers answers = new QAnswers("answers");

    public final umc.unimade.global.common.QBaseEntity _super = new umc.unimade.global.common.QBaseEntity(this);

    public final ListPath<AnswerImage, QAnswerImage> answerImages = this.<AnswerImage, QAnswerImage>createList("answerImages", AnswerImage.class, QAnswerImage.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QQuestions question;

    public final umc.unimade.domain.accounts.entity.QSeller seller;

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAnswers(String variable) {
        this(Answers.class, forVariable(variable), INITS);
    }

    public QAnswers(Path<? extends Answers> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAnswers(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAnswers(PathMetadata metadata, PathInits inits) {
        this(Answers.class, metadata, inits);
    }

    public QAnswers(Class<? extends Answers> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.question = inits.isInitialized("question") ? new QQuestions(forProperty("question"), inits.get("question")) : null;
        this.seller = inits.isInitialized("seller") ? new umc.unimade.domain.accounts.entity.QSeller(forProperty("seller")) : null;
    }

}

