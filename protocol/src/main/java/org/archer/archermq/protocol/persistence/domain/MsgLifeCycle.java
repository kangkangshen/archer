package org.archer.archermq.protocol.persistence.domain;


import javax.persistence.*;

@Entity
public class MsgLifeCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String msgId;

    @Column
    private String msgKey;

    @Column
    private String phase;

    @Column
    private String phaseStatus;

    @Column
    private String host;

    @Column
    private Integer port;


}
