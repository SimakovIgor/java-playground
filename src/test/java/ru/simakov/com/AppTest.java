package ru.simakov.com;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AppTest {

    @Test
    void main() {
        App app = new App();
        App.main(new String[] {});
        Assertions.assertThat(app).isNotNull();
    }
}
