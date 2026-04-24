package com.capg.resumeservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.*;

class ResumeServiceApplicationTests {

    @Test
    void main_doesNotThrow() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(ResumeServiceApplication.class, new String[]{}))
                  .thenReturn(null);
            ResumeServiceApplication.main(new String[]{});
            mocked.verify(() -> SpringApplication.run(ResumeServiceApplication.class, new String[]{}));
        }
    }
}
