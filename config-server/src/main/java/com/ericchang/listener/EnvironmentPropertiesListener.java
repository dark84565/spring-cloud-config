package com.ericchang.listener;

import com.ericchang.repository.EnvironmentPropertiesModel;
import com.ericchang.repository.EnvironmentPropertiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bus.endpoint.RefreshBusEndpoint;
import org.springframework.cloud.bus.event.EnvironmentChangeListener;
import org.springframework.cloud.bus.event.EnvironmentChangeRemoteApplicationEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringTokenizer;


@Component
public class EnvironmentPropertiesListener extends EnvironmentChangeListener {

    @Autowired
    EnvironmentPropertiesRepository repository;

    @Autowired
    RefreshBusEndpoint refreshBusEndpoint;

    @Override
    public void onApplicationEvent(EnvironmentChangeRemoteApplicationEvent event) {
        super.onApplicationEvent(event);

        if (event.getDestinationService().equals("**")) {
            String key = event.getValues().keySet().stream().findFirst().orElse(null);
            List<EnvironmentPropertiesModel> propertiesModels = repository.findByKey(key);
            propertiesModels.forEach(properties -> {
                properties.setValue(event.getValues().get(key));
            });
            repository.saveAll(propertiesModels);
            refreshBusEndpoint.busRefresh();
        } else {
            String applicationName = new StringTokenizer(event.getDestinationService(), ":").nextToken();
            List<EnvironmentPropertiesModel> propertiesModels = repository.findByApplication(applicationName);
            propertiesModels.forEach(properties -> {
                String newValue = event.getValues().values().stream().findFirst().orElse(null);
                properties.setValue(newValue);
            });
            repository.saveAll(propertiesModels);
            refreshBusEndpoint.busRefreshWithDestination(new String[]{applicationName});
        }
    }
}
