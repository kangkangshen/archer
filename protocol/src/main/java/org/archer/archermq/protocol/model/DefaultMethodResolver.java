package org.archer.archermq.protocol.model;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

@Component
public class DefaultMethodResolver implements MethodResolver {

    private final Multimap<Integer,Integer> methodRegistry = ArrayListMultimap.create();

    @Override
    public boolean support(int classId, int methodId) {
        if(!methodRegistry.containsKey(classId)){
            return false;
        }
        Collection<Integer> methods = methodRegistry.get(classId);
        return !CollectionUtils.isEmpty(methods)&&methods.contains(methodId);
    }

    @Override
    public void register(int classId, int methodId) {
        methodRegistry.put(classId,methodId);
    }
}
