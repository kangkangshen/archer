package org.archer.archermq.protocol.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MsgContent {

    @Id
    private String msgId;

    @Column
    private String msgKey;

    @Column
    private byte[] msgContent;

    @Column
    private String msgProperties;


}
