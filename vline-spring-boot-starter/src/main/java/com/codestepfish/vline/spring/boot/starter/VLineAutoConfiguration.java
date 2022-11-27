package com.codestepfish.vline.spring.boot.starter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.codestepfish.vline.core.Device;
import com.codestepfish.vline.core.VLine;
import com.codestepfish.vline.core.database.DataBaseDevice;
import com.codestepfish.vline.core.etcd.EtcdDevice;
import com.codestepfish.vline.core.grpc.GrpcDevice;
import com.codestepfish.vline.core.http.HttpDevice;
import com.codestepfish.vline.core.mqtt.MqttDevice;
import com.codestepfish.vline.core.redis.RedisDevice;
import com.codestepfish.vline.core.serial_port.SerialPortDevice;
import com.codestepfish.vline.core.tcp.TcpDevice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@AutoConfigureBefore({DataSourceAutoConfiguration.class})
@EnableConfigurationProperties({VLineProperties.class})
@ConditionalOnProperty(prefix = VLine.PREFIX, name = "enabled", havingValue = "true")
@ConfigurationPropertiesScan(basePackages = "com.codestepfish.vline.spring.boot.starter")
public class VLineAutoConfiguration implements InitializingBean, DisposableBean {

    private final VLineProperties vLineProperties;

    @Override
    public void destroy() throws Exception {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("vline start init ...");

        if (!CollectionUtils.isEmpty(vLineProperties.getDevices())) {
            initDevice(vLineProperties.getDevices());
        }

        log.info("vline started success ....");
    }

    private void initDevice(List<Device> devices) {
        CountDownLatch countDownLatch = new CountDownLatch(devices.size());
        devices.forEach(device -> {

            ThreadUtil.execute(() -> {
                getDevice(device).init();

                countDownLatch.countDown();
            });

        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private Device getDevice(Device device) {
        return switch (device.getType()) {
            case TCP -> BeanUtil.copyProperties(device, TcpDevice.class);
            case HTTP -> (HttpDevice) device;
            case ETCD -> (EtcdDevice) device;
            case GRPC -> (GrpcDevice) device;
            case MQTT -> (MqttDevice) device;
            case DATABASE -> (DataBaseDevice) device;
            case REDIS -> (RedisDevice) device;
            case SERIAL_PORT -> (SerialPortDevice) device;
        };
    }

}
