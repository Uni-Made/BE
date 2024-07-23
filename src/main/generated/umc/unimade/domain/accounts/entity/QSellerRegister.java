package umc.unimade.domain.accounts.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSellerRegister is a Querydsl query type for SellerRegister
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSellerRegister extends EntityPathBase<SellerRegister> {

    private static final long serialVersionUID = -1666356701L;

    public static final QSellerRegister sellerRegister = new QSellerRegister("sellerRegister");

    public final StringPath email = createString("email");

    public final EnumPath<Gender> gender = createEnum("gender", Gender.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final StringPath profileImage = createString("profileImage");

    public final EnumPath<Provider> provider = createEnum("provider", Provider.class);

    public final StringPath reason = createString("reason");

    public final EnumPath<umc.unimade.global.registerStatus.RegisterStatus> registerStatus = createEnum("registerStatus", umc.unimade.global.registerStatus.RegisterStatus.class);

    public QSellerRegister(String variable) {
        super(SellerRegister.class, forVariable(variable));
    }

    public QSellerRegister(Path<? extends SellerRegister> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSellerRegister(PathMetadata metadata) {
        super(SellerRegister.class, metadata);
    }

}

