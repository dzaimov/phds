package com.phds.phd.service.impl;

import com.phds.phd.dao.AnnotationDAO;
import com.phds.phd.model.Annotation;
import com.phds.phd.service.AnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnotationServiceImpl implements AnnotationService {

    private AnnotationDAO annotationDAO;


    @Autowired
    public AnnotationServiceImpl( AnnotationDAO annotationDAO) {
        this.annotationDAO = annotationDAO;
    }

    @Override
    public List<Annotation> getAnnotations() {
        return annotationDAO.getAnnotations();
    }

    @Override
    public Annotation findById(int id) {
        return annotationDAO.findById(id);
    }

    @Override
    public int insertAnnotations(Annotation annotation) {
        return annotationDAO.insert(annotation);
    }

    @Override
    public void updateAnnotation(Annotation annotation) {
        annotationDAO.updateAnnotation(annotation);
    }
}
