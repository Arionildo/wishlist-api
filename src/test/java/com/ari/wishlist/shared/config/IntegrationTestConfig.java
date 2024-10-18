package com.ari.wishlist.shared.config;

import com.ari.wishlist.domain.repository.WishlistRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class IntegrationTestConfig {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected WishlistRepository wishlistRepository;

    @BeforeEach
    void setUp() {
        wishlistRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        wishlistRepository.deleteAll();
    }
}
