import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.springboot.Application;
import com.example.springboot.Folder;
import com.example.springboot.FolderService;
import com.example.springboot.User;
import com.example.springboot.UserService;

@SpringBootTest(classes = Application.class)

@AutoConfigureMockMvc
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private FolderService folderService;

    private User mockUser;

    @BeforeEach
    public void setUp() {
        // Mock user data
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");

        when(userService.findByUsername("testUser")).thenReturn(mockUser);

        // Mock folders
        when(folderService.getAllFoldersByUser(1L))
                .thenReturn(List.of(
                        new Folder("Folder1", mockUser),
                        new Folder("Folder2", mockUser)
                ));
    }

    @Test
    public void testProfilePage() throws Exception {
        mockMvc.perform(get("/profile")
                .with(SecurityMockMvcRequestPostProcessors.user("testUser")))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("username", "folders"));
    }

    @Test
    public void testDeleteFolderSuccess() throws Exception {
        Mockito.doNothing().when(folderService).deleteFolderById(1L);

        mockMvc.perform(post("/profile/delete/1")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("testUser")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"))
                .andExpect(flash().attribute("successMessage", "Folder deleted successfully!"));
    }

    @Test
    public void testDeleteFolderFailure() throws Exception {
        Mockito.doThrow(new RuntimeException("Error deleting folder")).when(folderService).deleteFolderById(1L);

        mockMvc.perform(post("/profile/delete/1")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("testUser")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"))
                .andExpect(flash().attribute("errorMessage", "Error deleting folder."));
    }
}
