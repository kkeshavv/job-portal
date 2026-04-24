package com.capg.analyticsservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.*;

class AnalyticsServiceApplicationTests {

    @Test
    void main_doesNotThrow() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(AnalyticsServiceApplication.class, new String[]{}))
                  .thenReturn(null);
            AnalyticsServiceApplication.main(new String[]{});
            mocked.verify(() -> SpringApplication.run(AnalyticsServiceApplication.class, new String[]{}));
        }
    }
}
