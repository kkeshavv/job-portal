package com.capg.searchservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.*;

class SearchServiceApplicationTests {

    @Test
    void main_doesNotThrow() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(any(Class.class), any(String[].class)))
                  .thenReturn(mock(ConfigurableApplicationContext.class));
            SearchServiceApplication.main(new String[]{});
            mocked.verify(() -> SpringApplication.run(SearchServiceApplication.class, new String[]{}));
        }
    }
}
