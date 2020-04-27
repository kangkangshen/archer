package org.archer.archermq.protocol.persistence.repository;

import org.archer.archermq.protocol.persistence.domain.MsgContent;
import org.springframework.data.repository.CrudRepository;

public interface MsgContentRepository extends CrudRepository<MsgContent, Long> {

}
