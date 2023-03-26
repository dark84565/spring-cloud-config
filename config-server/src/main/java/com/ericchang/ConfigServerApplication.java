package com.ericchang;

import com.ericchang.repository.EnvironmentPropertiesModel;
import com.ericchang.repository.EnvironmentPropertiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.bus.endpoint.RefreshBusEndpoint;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Objects.nonNull;

@EnableConfigServer
@SpringBootApplication
@RestController
public class ConfigServerApplication {

    @Autowired
    RefreshBusEndpoint refreshBusEndpoint;
    @Autowired
    EnvironmentPropertiesRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }

    @GetMapping("/refreshAll")
    public void refreshAllClients() {
        refreshBusEndpoint.busRefresh();
    }

    @PostMapping("/update")
    public void updateEnvironmentProperties(@RequestBody EnvironmentPropertiesModel properties) {
        updateRepository(properties);
        refreshBusEndpoint.busRefreshWithDestination(new String[]{properties.getApplication()});
    }

    private void updateRepository(EnvironmentPropertiesModel properties) {
        EnvironmentPropertiesModel origin = repository.findByApplicationAndProfileAndLabelAndKey(
                properties.getApplication(), properties.getProfile(), properties.getLabel(), properties.getKey());
        if (nonNull(origin)) {
            origin.setValue(properties.getValue());
            repository.save(origin);
        } else {
            repository.save(properties);
        }
    }
}