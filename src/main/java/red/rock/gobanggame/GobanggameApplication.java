package red.rock.gobanggame;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@MapperScan("red.rock.gobanggame")
@ServletComponentScan
@SpringBootApplication
public class GobanggameApplication {

    public static void main(String[] args) {
        SpringApplication.run(GobanggameApplication.class, args);
    }

}
