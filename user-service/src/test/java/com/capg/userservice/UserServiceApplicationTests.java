package com.capg.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.*;

class UserServiceApplicationTests {

    @Test
    void main_doesNotThrow() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(UserServiceApplication.class, new String[]{}))
                  .thenReturn(null);
            UserServiceApplication.main(new String[]{});
            mocked.verify(() -> SpringApplication.run(UserServiceApplication.class, new String[]{}));
        }
    }
}
