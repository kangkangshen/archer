package org.archer.archermq.protocol;

import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;


/**
 * virtualHost
 *
 * @date 2020年04月18日09:39:14
 * @author dongyue
 */
@Data
public class VirtualHostConfig {

    private String name;

    List<Pair<String/*host*/,Integer/*port*/>> actualHostConfigs;


}
