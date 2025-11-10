package LearnToeic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
@SpringBootApplication(
    exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
    }
)
public class LearnToeicApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnToeicApplication.class, args);
	}

}
