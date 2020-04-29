package org.archer.archermq.common.utils;


import org.apache.commons.lang3.StringUtils;
import org.archer.archermq.common.Namespace;
import org.archer.archermq.common.constants.Delimiters;

/**
 * 命名空间解析
 *
 * @author dongyue
 * @date 2020年04月16日18:55:12
 */
public class NameSpaceUtil {

    private static final char DEFAULT_DELIMITER = Delimiters.PERIOD;

    public static Namespace namespace(String namespace){
        if(StringUtils.isBlank(namespace)){
            return new DefaultNamespaceImpl(StringUtils.EMPTY,null);
        }
        int lastIndex = namespace.lastIndexOf(DEFAULT_DELIMITER);
        if(lastIndex==-1){
            return new DefaultNamespaceImpl(namespace,null);
        }else{
            return new DefaultNamespaceImpl(namespace.substring(lastIndex+1),namespace(namespace.substring(0,lastIndex)));
        }

    }

}
