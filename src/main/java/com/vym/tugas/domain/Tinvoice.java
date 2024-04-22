/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 23 - 3 - 2024
 */

package com.vym.tugas.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "tinvoice")
public class Tinvoice implements Serializable {
    private static final long serialVersionUID = -6772211105407481102L;

    @Id
    @Column(name = "tinvoice_pk", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mproduct_fk", nullable = false)
    private Mproduct mproductFk;

    @NotNull
    @Column(name = "qty", nullable = false)
    private Integer qty;

    @NotNull
    @Column(name = "price", nullable = false, precision = 10)
    private BigDecimal price;

}