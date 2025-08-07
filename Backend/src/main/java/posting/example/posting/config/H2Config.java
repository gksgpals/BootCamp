package posting.example.posting.config;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class H2Config {

    /**
     * H2 TCP 서버를 포트 9092로 띄워서
     * -ifNotExists 옵션으로 DB 파일이 없으면 자동 생성되도록 설정합니다.
     * 외부 툴(DBeaver 등)에서 jdbc:h2:tcp://localhost:9092/~/test 으로 접속 가능합니다.
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2TcpServer() throws SQLException {
        return Server.createTcpServer(
                "-tcp",
                "-tcpPort", "9092",
                "-tcpAllowOthers",
                "-ifNotExists"
        );
    }
}
