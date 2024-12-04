import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.springboot.Application;
import com.example.springboot.Folder;
import com.example.springboot.FolderService;
import com.example.springboot.User;
import com.example.springboot.UserService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class FolderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FolderService folderService;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp() {
        // Mock the user returned by UserService
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");
        mockUser.setPassword("testPassword");
    
        // Mock the UserService to return the mock user
        when(userService.findByUsername("testUser")).thenReturn(mockUser);
    
        // Mock folder creation success
        when(folderService.createFolder(1L, "Test Folder"))
                .thenReturn("Folder created successfully.");
    
        // Mock folder creation failure
        when(folderService.createFolder(1L, "Duplicate Folder"))
                .thenReturn("Folder name already exists.");
    
        // Mock folder retrieval
        when(folderService.getAllFoldersByUser(1L))
                .thenReturn(List.of(
                    new Folder("Folder1", mockUser),
                    new Folder("Folder2", mockUser)
                ));
    }

    @Test
    public void testCreateFolderSuccess() throws Exception {
        mockMvc.perform(post("/folders/create")
                .param("folderName", "Test Folder")
                .with(SecurityMockMvcRequestPostProcessors.csrf()) // Add CSRF token
                .with(SecurityMockMvcRequestPostProcessors.user("testUser"))) // Simulate authenticated user
                .andExpect(status().is3xxRedirection()) // Expect redirection
                .andExpect(redirectedUrl("/profile")); // Redirects to profile
    }
    
    @Test
    public void testCreateFolderFailure() throws Exception {
        mockMvc.perform(post("/folders/create")
                .param("folderName", "Duplicate Folder")
                .with(SecurityMockMvcRequestPostProcessors.csrf()) // Add CSRF token
                .with(SecurityMockMvcRequestPostProcessors.user("testUser"))) // Simulate authenticated user
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile")); // No query parameters in the redirect URL
    }
    
    

    @Test
    public void testGetAllFoldersByUser() throws Exception {
        mockMvc.perform(get("/folders/user")
                .with(SecurityMockMvcRequestPostProcessors.user("testUser")))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("folders"));
    }
    
    
}
