package dev.ebullient.micrometer.deployment.export;

import javax.inject.Inject;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.quarkus.test.QuarkusUnitTest;

public class PrometheusEnabledTest {
    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addClass(PrometheusRegistryProcessor.REGISTRY_CLASS)
                    .addAsResource(new StringAsset(
                            "quarkus.micrometer.export.prometheus.enabled=true\n"
                                    + "quarkus.micrometer.registry-enabled-default=false"),
                            "application.properties"));

    @Inject
    MeterRegistry registry;

    @Inject
    PrometheusMeterRegistry promRegistry;

    @Test
    public void testMeterRegistryPresent() {
        // Prometheus is enabled (only registry)
        Assertions.assertNotNull(registry, "A registry should be configured");
        Assertions.assertTrue(PrometheusMeterRegistry.class.equals(registry.getClass()), "Should be PrometheusMeterRegistry");
    }
}