package org.archer.archermq.config.register;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Metadata {

    @Id
    private String id;

    @Column
    private String desc;

    @Column
    private String virtualHost;

    @Column
    private String tag;

    @Column
    private String properties;
}
