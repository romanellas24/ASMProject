package asm.couriers.courier_allocation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Table(name = "id_mapping", uniqueConstraints = {
        @UniqueConstraint(name = "uq_company_order_id_company_id",
                columnNames = {"company_order_id", "company_id"}
        )
})
public class IdMapping {

    @Id
    private Integer orderId;


    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "courier_order_id", referencedColumnName = "order_id")
    private Order order;

    @Column(name = "company_order_id", nullable = false)
    private Integer companyOrderId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}
