import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springboot.Application;
import com.example.springboot.User;
import com.example.springboot.UserRepository;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)

public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        // Ensure the user "testUser" exists in the database
        if (userRepository.findByUsername("testUser") == null) {
            User testUser = new User();
            testUser.setUsername("testUser");
            testUser.setPassword(new BCryptPasswordEncoder().encode("password")); // Use encoded password
            userRepository.save(testUser);
        }
    }

    // Test public access to permitted endpoints
    @Test
    public void testPublicEndpoints() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
    }

    // Test restricted access to private endpoints
    @Test
    public void testRestrictedEndpointsRedirectToLogin() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    // Test login success
    @Test
    public void testLoginSuccess() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "testUser") // Use the username added in @BeforeEach
                .param("password", "password") // Use the matching password
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile")); // Ensure login redirects to profile
    }
    

    // Test login failure
    @Test
    public void testLoginFailure() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "invalidUser")
                .param("password", "wrongPassword")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error")); // Ensure failed login redirects back to login with error
    }

    // Test authenticated access to private endpoints
    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testAuthenticatedAccessToProfile() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk());
    }
}
