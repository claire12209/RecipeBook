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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springboot.Application;
import com.example.springboot.Recipe;
import com.example.springboot.RecipeService;
import com.example.springboot.Review;
import com.example.springboot.ReviewService;
import com.example.springboot.User;
import com.example.springboot.UserService;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private UserService userService;

    private User mockUser;
    private Recipe mockRecipe;

    @BeforeEach
    public void setUp() {
        // Mock user
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");

        // Mock recipe
        mockRecipe = new Recipe();
        mockRecipe.setId(1L);
        mockRecipe.setName("Test Recipe");

        when(userService.findByUsername("testUser")).thenReturn(mockUser);
        when(recipeService.findById(1L)).thenReturn(mockRecipe);
    }

    @Test
    public void testAddReview() throws Exception {
        mockMvc.perform(post("/recipe/1/addReview")
                .param("rating", "5")
                .param("comment", "Delicious recipe!")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("testUser")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipe/1"));

        // Verify the save operation
        Mockito.verify(reviewService).save(Mockito.any(Review.class));
    }
}
