package umc.unimade.domain.qna.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestions is a Querydsl query type for Questions
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestions extends EntityPathBase<Questions> {

    private static final long serialVersionUID = 238382730L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestions questions = new QQuestions("questions");

    public final umc.unimade.global.common.QBaseEntity _super = new umc.unimade.global.common.QBaseEntity(this);

    public final ListPath<Answers, QAnswers> answers = this.<Answers, QAnswers>createList("answers", Answers.class, QAnswers.class, PathInits.DIRECT2);

    public final umc.unimade.domain.accounts.entity.QBuyer buyer;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final umc.unimade.domain.products.entity.QProducts product;

    public final ListPath<QuestionImage, QQuestionImage> questionImages = this.<QuestionImage, QQuestionImage>createList("questionImages", QuestionImage.class, QQuestionImage.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QQuestions(String variable) {
        this(Questions.class, forVariable(variable), INITS);
    }

    public QQuestions(Path<? extends Questions> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestions(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestions(PathMetadata metadata, PathInits inits) {
        this(Questions.class, metadata, inits);
    }

    public QQuestions(Class<? extends Questions> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.buyer = inits.isInitialized("buyer") ? new umc.unimade.domain.accounts.entity.QBuyer(forProperty("buyer")) : null;
        this.product = inits.isInitialized("product") ? new umc.unimade.domain.products.entity.QProducts(forProperty("product"), inits.get("product")) : null;
    }

}

