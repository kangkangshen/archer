package org.archer.archermq.config.register;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Component
public class StandardMetaRegistrar implements Registrar<String,Metadata>{

    @Autowired
    private MetadataRepository metadataRepository;

    @Override
    public boolean contains(String s) {
        return metadataRepository.existsById(s);
    }

    @Override
    public boolean register(String s, Metadata instance) {
        metadataRepository.save(instance);
        return true;
    }

    @Override
    public Metadata remove(String s) {
        Optional<Metadata> metadata = metadataRepository.findById(s);
        if(metadata.isPresent()){
            metadataRepository.deleteById(s);
            return metadata.get();
        }else{
            return null;
        }
    }

    @Override
    public Metadata get(String s) {
        Optional<Metadata> metadata = metadataRepository.findById(s);
        return metadata.orElse(null);
    }

    @Override
    public Set<String> ids() {
        Set<String> ids = Sets.newHashSet();
        metadataRepository.findAll().forEach(metadata->ids.add(metadata.getId()));
        return ids;
    }

    @Override
    public List<Metadata> instances() {
        return Lists.newArrayList(metadataRepository.findAll());
    }

    @Override
    public int size() {
        return (int) metadataRepository.count();
    }
}
