package red.rock.gobanggame;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("red.rock.gobanggame")
@SpringBootApplication
public class GobanggameApplication {

    public static void main(String[] args) {
        SpringApplication.run(GobanggameApplication.class, args);
    }

}
