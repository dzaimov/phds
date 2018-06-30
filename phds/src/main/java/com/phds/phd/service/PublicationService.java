package com.phds.phd.service;

import com.phds.phd.model.Publication;

import java.util.List;

public interface PublicationService {
    List<Publication> getPublication();

    Publication findById(int id);

    void removePublication(Publication publication);

    int insertPublication(Publication publication);

    void updatePublication(Publication publication);

}
