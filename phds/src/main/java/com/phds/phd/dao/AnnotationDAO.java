package com.phds.phd.dao;

import com.phds.phd.model.Annotation;

import java.util.List;

public interface AnnotationDAO {
    List<Annotation> getAnnotations();

    Annotation findById(int id);

    int insert(Annotation annotation);

    void updateAnnotation(Annotation annotation);
}
