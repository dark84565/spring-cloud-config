package com.ericchang.listener;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bus.endpoint.RefreshBusEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class MysqlBinLogListener {

    private static final List<String> TABLE_NAME = List.of("properties");
    {
//        getThread().start();
    }

    @Autowired
    RefreshBusEndpoint refreshBusEndpoint;

    public Thread getThread() {
        BinaryLogClient client = new BinaryLogClient("localhost", 3306, "root",  "0000");
        EventDeserializer eventDeserializer = new EventDeserializer();
        eventDeserializer.setCompatibilityMode(
                EventDeserializer.CompatibilityMode.DATE_AND_TIME_AS_LONG,
                EventDeserializer.CompatibilityMode.CHAR_AND_BINARY_AS_BYTE_ARRAY
        );
        client.setEventDeserializer(eventDeserializer);

        return new Thread(() -> {
            client.registerEventListener(event -> {
                final EventData data = event.getData();
                if(data instanceof UpdateRowsEventData || data instanceof WriteRowsEventData) {
//                    ((UpdateRowsEventData) data).getRows().forEach(row -> {
//                        Arrays.stream(row.getValue()).forEach(value -> {
//                            System.out.println(value);
//                        });
//                    });
                    refreshBusEndpoint.busRefresh();
                }
            });
            try {
                client.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
