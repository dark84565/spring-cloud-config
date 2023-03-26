package com.ericchang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EnvironmentPropertiesRepository extends JpaRepository<EnvironmentPropertiesModel, String> {
    EnvironmentPropertiesModel findByApplicationAndProfileAndLabelAndKey(String application, String profile, String label, String key);

    List<EnvironmentPropertiesModel> findByApplication(String application);

    List<EnvironmentPropertiesModel> findByKey(String key);
}
