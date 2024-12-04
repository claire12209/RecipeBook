import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.springboot.Application;

@SpringBootTest(classes = Application.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Test GET Request
    @Test
    public void testShowRegistrationForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
            .andExpect(status().isOk())
            .andExpect(view().name("register"))
            .andExpect(model().attributeExists("user"));
    }

    // Test POST Request (Success)
    @Test
    public void testRegisterUserSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
            .with(SecurityMockMvcRequestPostProcessors.csrf()) // Add CSRF token
            .param("username", "newuser")
            .param("password", "securepassword"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"));
    }

    // Test POST Request (Fail due to short password)
    @Test
    public void testRegisterUserFailPasswordTooShort() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
            .with(SecurityMockMvcRequestPostProcessors.csrf()) // Add CSRF token
            .param("username", "newuser")
            .param("password", "short"))
            .andExpect(status().isOk())
            .andExpect(view().name("register"))
            .andExpect(model().attributeExists("error"))
            .andExpect(model().attribute("error", "Password must be at least 8 characters long."));
    }

}
