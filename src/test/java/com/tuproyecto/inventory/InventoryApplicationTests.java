package com.tuproyecto.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
// 1. Esto le dice a Spring: "Ignora PostgreSQL, usa una base temporal"
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
// 2. Esto inyecta la configuración H2 a la fuerza
@TestPropertySource(properties = {
		"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
		"spring.datasource.driverClassName=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
class InventoryApplicationTests {

	@Test
	void contextLoads() {
		// Si este test pasa, la configuración H2 funcionó
	}
}