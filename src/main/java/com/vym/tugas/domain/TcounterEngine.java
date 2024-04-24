/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 24 - 4 - 2024
 */

package com.vym.tugas.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "tcounter_engine")
public class TcounterEngine {
    @Id
    @Size(max = 55)
    @Column(name = "`key`", nullable = false, length = 55)
    private String key;

    @Size(max = 255)
    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

}