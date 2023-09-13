package project.mini.batch3.vttp.miniprojectserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class MiniProjectServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniProjectServerApplication.class, args);
	}

}
