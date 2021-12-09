package rvib.lab4.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class IgniteClientConfiguration {
    @Bean
    public IgniteConfiguration igniteConfiguration() {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();

        igniteConfiguration.setPeerClassLoadingEnabled(true);

        return igniteConfiguration;
    }

    @Bean
    public Ignite ignite(IgniteConfiguration igniteConfiguration) throws IgniteException {
        return Ignition.getOrStart(igniteConfiguration);
    }
}
