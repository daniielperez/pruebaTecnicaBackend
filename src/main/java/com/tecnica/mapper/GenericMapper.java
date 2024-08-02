package com.tecnica.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenericMapper {

    private static final ModelMapper modelMapper = new ModelMapper();
    private static final ModelMapper modelMapperAmbiguityIgnored = new ModelMapper();

    public static <S, D> List<D> mapList(List<S> sourceList, Class<D> destinationClass) {
        return sourceList.stream()
                .map(source -> modelMapper.map(source, destinationClass))
                .collect(Collectors.toList());
    }


    public static <S, D> D map(S source, Class<D> destinationClass) {
        return modelMapper.map(source, destinationClass);
    }

    public static <S, D> List<D> mapListAmbiguityIgnored(List<S> sourceList, Class<D> destinationClass) {
        modelMapperAmbiguityIgnored.getConfiguration().setAmbiguityIgnored(true);
        return sourceList.stream()
                .map(source -> modelMapperAmbiguityIgnored.map(source, destinationClass))
                .collect(Collectors.toList());
    }

    public static <S, D> D mapAmbiguityIgnored(S source, Class<D> destinationClass) {
        modelMapperAmbiguityIgnored.getConfiguration().setAmbiguityIgnored(true);
        return modelMapperAmbiguityIgnored.map(source, destinationClass);
    }

}
