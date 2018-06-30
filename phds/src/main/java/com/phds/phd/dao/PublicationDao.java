package com.phds.phd.dao;

import com.phds.phd.model.Publication;

import java.util.List;

public interface PublicationDao {
    List<Publication> getPublication();

    Publication findById(int id);

    void removePublication(Publication publication);

    int insertPublication(Publication publication);

    void updatePublication(Publication publication);

}
