package org.archer.archermq.protocol.persistence.repository;

import org.archer.archermq.protocol.persistence.domain.MsgLifeCycle;
import org.springframework.data.repository.CrudRepository;

public interface MsgLifeCycleRepository extends CrudRepository<MsgLifeCycle, Integer> {
}
