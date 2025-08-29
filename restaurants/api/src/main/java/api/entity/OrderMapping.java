package api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_mapping")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderMapping {

    @Id
    @Column(name = "local_order_id")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "local_order_id")
    private Order order;

    @Column(name = "company_id", nullable = false)
    private Integer companyId;

    @Column(name = "company_name", nullable = false)
    private String companyName;
}
