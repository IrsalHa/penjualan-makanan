/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 18 - 3 - 2024
 */

package com.vym.tugas.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "muser")
public class Muser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "muser_pk", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Size(max = 20)
    @NotNull
    @Column(name = "userid", nullable = false, length = 20)
    private String userid;

    @Size(max = 30)
    @NotNull
    @Column(name = "password", nullable = false, length = 30)
    private String password;

}