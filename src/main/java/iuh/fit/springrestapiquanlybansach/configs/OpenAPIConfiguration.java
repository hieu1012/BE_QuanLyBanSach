package iuh.fit.springrestapiquanlybansach.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {
	@Bean
	public OpenAPI defineOpenApi() {
		Server server = new Server();
		// Nếu API của bạn được triển khai ở context path khác (ví dụ /api/v1) hãy chỉnh ở đây
		server.setUrl("/api");
		server.setDescription("Base path for Book Store API");

//		Info information = new Info()
//				.title("Book Store - Quản lý bán sách API")
//				.version("1.0.0")
//				.description("API để quản lý sách, danh mục, đơn hàng và khách hàng trong ứng dụng bán sách.")
//				.contact(new Contact().name("BookStore Team").email("support@bookstore.local"))
//				.license(new License().name("MIT").url("https://opensource.org/licenses/MIT"));

		Info information = new Info()
				.title("Book Store - Quản lý bán sách API")
				.version("1.0")
				.description("API để quản lý sách, danh mục, đơn hàng và khách hàng trong ứng dụng bán sách.");

		return new OpenAPI().info(information).servers(List.of(server));
	}
}