import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Created by 25131 on 2019/3/6.
 */
@SpringBootConfiguration
//@EntityScan("com.xuecheng.framework.domain.cms")//扫描实体类
@ComponentScan(basePackages = {"com.xuecheng.api"})//扫描接口
@ComponentScan(basePackages = {"online.changfeng.music", "org.n3r.idworker"})//扫描接口
@MapperScan(basePackages = "online.changfeng.music.mapper")
@ServletComponentScan(basePackages = {"online.changfeng.music"})
@EnableAutoConfiguration
public class MusicApplication {
    public static void main(String[] args) {
        SpringApplication.run(MusicApplication.class,args);
    }
}
