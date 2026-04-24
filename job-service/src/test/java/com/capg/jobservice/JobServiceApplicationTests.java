package com.capg.jobservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.*;

class JobServiceApplicationTests {

    @Test
    void main_doesNotThrow() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(JobServiceApplication.class, new String[]{}))
                  .thenReturn(null);
            JobServiceApplication.main(new String[]{});
            mocked.verify(() -> SpringApplication.run(JobServiceApplication.class, new String[]{}));
        }
    }
}
