package com.deadpeace.selfie;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.MultipartConfigElement;
import java.io.File;

/**
 * Created by Виталий on 06.10.2015.
 */

@EnableWebMvc
@SpringBootApplication
public class Application extends WebMvcAutoConfiguration
{
    public static void main(String[] args)
    {
        SpringApplication.run(Application.class,args);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement()
    {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("10MB");
        factory.setMaxRequestSize("10MB");
        return factory.createMultipartConfig();
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(@Value("${keystore.file:src/main/resources/private/keystore}") final String keystoreFile,@Value("${keystore.pass:Ns9sBV3Itx}") final String keystorePass) throws Exception
    {
        return container->{
            TomcatEmbeddedServletContainerFactory tomcat=(TomcatEmbeddedServletContainerFactory) container;
            tomcat.addConnectorCustomizers(new TomcatConnectorCustomizer()
            {
                @Override
                public void customize(Connector connector)
                {
                    connector.setPort(8443);
                    connector.setSecure(true);
                    connector.setScheme("https");
                    Http11NioProtocol proto=(Http11NioProtocol) connector.getProtocolHandler();
                    proto.setSSLEnabled(true);
                    proto.setKeystoreFile(new File(keystoreFile).getAbsolutePath());
                    proto.setKeystorePass(keystorePass);
                    proto.setKeystoreType("JKS");
                    proto.setKeyAlias("tomcat");
                }
            });
        };
    }
}
