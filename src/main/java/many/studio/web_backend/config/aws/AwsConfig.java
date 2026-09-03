package many.studio.web_backend.config.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String accessSecret;

    @Value("${cloud.aws.credentials.sessionToken}")
    private String sessionToken;

    @Value("${cloud.aws.region}")
    private String region;



}
