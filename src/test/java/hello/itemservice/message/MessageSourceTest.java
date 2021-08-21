package hello.itemservice.message;

import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class MessageSourceTest {

    // messageSource를 Autowired하면 message.properties들을 가지고 있다.
    @Autowired
    MessageSource ms;

    @Test
    void helloMessage(){
        /**
         * 메시지 코드로 hello를 입력하고 locale정보를 null로 주었기때문에 가장기본값인 messages.properties의
         * hello key의 value인 '안녕'이 출력된다.
         */
        String result = ms.getMessage("hello", null, null);
        assertThat(result).isEqualTo("안녕");
    }

    @Test
    void notFoundMessageCode(){
        /**
         * messages.properties에서 no_code라는 key가 없기때문에 NoSuchMessageException이 발생한다.
         */
        assertThatThrownBy(() -> ms.getMessage("no_code",null,null))
                .isInstanceOf(NoSuchMessageException.class);
    }

    @Test
    void notFoundMessageCodeDefaultMessage() {
        /**
         * no_code라는 key가 없더라도 default값을 설정해준다면 설정해준 default값이 출력되게 된다.
         */
        String result = ms.getMessage("no_code", null, "기본 메시지", null);
        assertThat(result).isEqualTo("기본 메시지");
    }

    @Test
    void argumentMessage(){

        /**
         * 설정파일에 값을 전달해줄 수 있다.
         * hello.name=안녕 {0}
         * 이렇게 설정해줫기 때문에 뒤에 0이 들어가있는곳에 object 배열을 args로 넘겨줄 수 있고
         * 안녕 뒤에 0이 보내준 Spring으로 치환되게 된다.
         */
        String message = ms.getMessage("hello.name", new Object[]{"Spring"}, null);
        assertThat(message).isEqualTo("안녕 Spring");
    }


    // 아래부터 국제화
    @Test
    void defaultLang(){
        /**
         * 둘다 안녕이 출력된다. 왜냐하면 Locale을 Korea로 설정했어도 Korea 관련 파일이 존재하지 않기 때문에
         * default 설정파일이 작동하게 된다.
         */
        assertThat(ms.getMessage("hello",null,null)).isEqualTo("안녕");
        assertThat(ms.getMessage("hello",null, Locale.KOREA)).isEqualTo("안녕");
    }

    @Test
    void enLang(){
        /**
         * 얘는 Locale이 English로 설정해주었기 때문에 en 설정파일로 가게 된다.
         * 스프링 메시지 관리 소스는 국제화도 설정해준다.
         */
        assertThat(ms.getMessage("hello",null,Locale.ENGLISH)).isEqualTo("hello");
    }

}
