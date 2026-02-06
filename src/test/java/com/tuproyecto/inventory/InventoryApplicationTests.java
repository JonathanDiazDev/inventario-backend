package com.tuproyecto.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
// 1. ✅ ESTO CORRIGE EL ERROR ROJO (Usamos Replace.ANY)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
// 2. ✅ ESTO CONFIGURA LA BASE DE DATOS Y EL TOKEN PARA QUE NO FALLE
@TestPropertySource(properties = {
		"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
		"spring.datasource.driverClassName=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
		"spring.jpa.hibernate.ddl-auto=create-drop",
		"api.security.secret=ClaveSecretaDePruebaParaLosTests1234567890",
		"api.security.token.expiration=3600"
})
class InventoryApplicationTests {

	@Test
	void contextLoads() {
		// Verifica que la aplicación arranca correctamente
	}
}