package com.phds.phd.service.impl;

import com.phds.phd.dao.PublicationDao;
import com.phds.phd.model.Publication;
import com.phds.phd.service.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicationServiceImpl implements PublicationService {

    private PublicationDao publicationDao;

    @Autowired
    public PublicationServiceImpl (PublicationDao publicationDao) {
        this.publicationDao = publicationDao;
    }
    @Override
    public List<Publication> getPublication() {
        return publicationDao.getPublication();
    }

    @Override
    public Publication findById(int id) {
        return publicationDao.findById(id);
    }

    @Override
    public void removePublication(Publication publication) {
publicationDao.removePublication(publication);
    }

    @Override
    public int insertPublication(Publication publication) {
        return publicationDao.insertPublication(publication);
    }

    @Override
    public void updatePublication(Publication publication) {
        publicationDao.updatePublication(publication);
    }
}
