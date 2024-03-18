/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 18 - 3 - 2024
 */

package com.vym.tugas.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "mmenu")
public class Mmenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mmenu_pk", nullable = false)
    private Integer id;

    @Size(max = 20)
    @Column(name = "menugroup", length = 20)
    private String menugroup;

    @Size(max = 20)
    @Column(name = "menugroupicon", length = 20)
    private String menugroupicon;

    @Size(max = 20)
    @Column(name = "menuicon", length = 20)
    private String menuicon;

    @Size(max = 40)
    @Column(name = "menuname", length = 40)
    private String menuname;

    @Column(name = "menuorderno")
    private Integer menuorderno;

    @Size(max = 10)
    @Column(name = "menuparamname", length = 10)
    private String menuparamname;

    @Size(max = 20)
    @Column(name = "menuparamvalue", length = 20)
    private String menuparamvalue;

    @Size(max = 100)
    @Column(name = "menupath", length = 100)
    private String menupath;

    @Size(max = 20)
    @Column(name = "menusubgroup", length = 20)
    private String menusubgroup;

    @Size(max = 20)
    @Column(name = "menusubgroupicon", length = 20)
    private String menusubgroupicon;

}