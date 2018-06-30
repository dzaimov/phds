package com.phds.phd.service;

import com.phds.phd.model.Annotation;

import java.util.List;

public interface AnnotationService {

    List<Annotation> getAnnotations();

    Annotation findById(int id);

    int insertAnnotations(Annotation Annotation);

    void updateAnnotation(Annotation annotation);
}
