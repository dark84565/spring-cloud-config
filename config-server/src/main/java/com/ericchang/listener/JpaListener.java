package com.ericchang.listener;

import com.ericchang.repository.EnvironmentPropertiesModel;
import jakarta.persistence.PostUpdate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JpaListener {

    @PostUpdate
    private void logPostUpdate(Object entity) {
        EnvironmentPropertiesModel properties = (EnvironmentPropertiesModel) entity;
        log.info(properties.getApplication());
    }
}
