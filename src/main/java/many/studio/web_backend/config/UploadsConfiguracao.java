package many.studio.web_backend.config;

import many.studio.web_backend.service.ArmazenamentoImagemService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class UploadsConfiguracao implements WebMvcConfigurer {

    private final ArmazenamentoImagemService armazenamentoImagemService;

    public UploadsConfiguracao(ArmazenamentoImagemService armazenamentoImagemService) {
        this.armazenamentoImagemService = armazenamentoImagemService;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String local = armazenamentoImagemService.getRaiz().toUri().toString();
        if (!local.endsWith("/")) {
            local = local + "/";
        }

        registry.addResourceHandler("/uploads/**").addResourceLocations(local);
    }
}
