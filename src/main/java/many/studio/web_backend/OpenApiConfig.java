package many.studio.web_backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Autenticação de Usuários",
                description = """
                        API responsável pelo gerenciamento de autenticação, agendamento e autorização de usuários.
                        
                        Integrantes:
                        - Bill Hebert
                        - Davi Santana
                        - Dereck Baksa
                        - Guilherme Aoki
                        - João Pedro
                        - Rafael Mechi
                        """,
                license = @License(name = "UNLICENSED"),
                version = "1.0.0"
        )
)
@SecurityScheme(
        name = "Bearer",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}