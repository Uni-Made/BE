package umc.unimade.domain.orders.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPurchaseForm is a Querydsl query type for PurchaseForm
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPurchaseForm extends EntityPathBase<PurchaseForm> {

    private static final long serialVersionUID = -364729979L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPurchaseForm purchaseForm = new QPurchaseForm("purchaseForm");

    public final umc.unimade.global.common.QBaseEntity _super = new umc.unimade.global.common.QBaseEntity(this);

    public final StringPath address = createString("address");

    public final umc.unimade.domain.accounts.entity.QBuyer buyer;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath detailAddress = createString("detailAddress");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isAgree = createBoolean("isAgree");

    public final StringPath name = createString("name");

    public final ListPath<Orders, QOrders> orders = this.<Orders, QOrders>createList("orders", Orders.class, QOrders.class, PathInits.DIRECT2);

    public final StringPath phoneNumber = createString("phoneNumber");

    public final EnumPath<umc.unimade.domain.products.entity.PickupOption> pickupOption = createEnum("pickupOption", umc.unimade.domain.products.entity.PickupOption.class);

    public final umc.unimade.domain.products.entity.QProducts product;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPurchaseForm(String variable) {
        this(PurchaseForm.class, forVariable(variable), INITS);
    }

    public QPurchaseForm(Path<? extends PurchaseForm> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPurchaseForm(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPurchaseForm(PathMetadata metadata, PathInits inits) {
        this(PurchaseForm.class, metadata, inits);
    }

    public QPurchaseForm(Class<? extends PurchaseForm> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.buyer = inits.isInitialized("buyer") ? new umc.unimade.domain.accounts.entity.QBuyer(forProperty("buyer")) : null;
        this.product = inits.isInitialized("product") ? new umc.unimade.domain.products.entity.QProducts(forProperty("product"), inits.get("product")) : null;
    }

}

