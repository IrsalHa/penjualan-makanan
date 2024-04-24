/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 24 - 4 - 2024
 */

package com.vym.tugas.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tbatchinvoice")
public class Tbatchinvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tbatchinvoice_pk", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "invoiceno", nullable = false, length = 50)
    private String invoiceno;

    @Column(name = "created_at")
    private Instant createdAt;

    @NotNull
    @Column(name = "total_price", nullable = false, precision = 10)
    private BigDecimal totalPrice;

}