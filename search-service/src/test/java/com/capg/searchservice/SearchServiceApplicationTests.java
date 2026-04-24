package com.capg.searchservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.*;

class SearchServiceApplicationTests {

    @Test
    void main_doesNotThrow() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(SearchServiceApplication.class, new String[]{}))
                  .thenReturn(null);
            SearchServiceApplication.main(new String[]{});
            mocked.verify(() -> SpringApplication.run(SearchServiceApplication.class, new String[]{}));
        }
    }
}
