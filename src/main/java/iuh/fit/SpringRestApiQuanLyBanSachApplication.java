package iuh.fit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class SpringRestApiQuanLyBanSachApplication {

	public static void main(String[] args) {
//		SpringApplication.run(SpringRestApiQuanLyBanSachApplication.class, args);

		ConfigurableApplicationContext ctx = SpringApplication.run(SpringRestApiQuanLyBanSachApplication.class, args);
		Environment env = ctx.getEnvironment();

		// Resolve port (runtime-assigned port available under 'local.server.port' when using random port)
		String port = env.getProperty("local.server.port", env.getProperty("server.port", "8080"));

		// Resolve context path (may be empty)
		String contextPath = env.getProperty("server.servlet.context-path", "");
		if (contextPath == null) contextPath = "";
		if (!contextPath.isEmpty() && !contextPath.startsWith("/")) contextPath = "/" + contextPath;

		// Resolve Swagger UI path (common property for springdoc; adjust if using springfox)
		String swaggerUiPath = env.getProperty("springdoc.swagger-ui.path",
				env.getProperty("springfox.documentation.swagger-ui.base-url", "/swagger-ui/index.html"));
		if (!swaggerUiPath.startsWith("/")) swaggerUiPath = "/" + swaggerUiPath;

		String url = "http://localhost:" + port + contextPath + swaggerUiPath;

		System.out.println();
		System.out.println("================================================================");
		System.out.println("Swagger UI is available at: " + url);
		System.out.println("================================================================");
		System.out.println();
	}

}
