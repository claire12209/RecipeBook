import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.example.springboot.Category;
import com.example.springboot.CategoryRepository;
import com.example.springboot.Recipe;
import com.example.springboot.RecipeRepository;
import com.example.springboot.User;
import com.example.springboot.UserRepository;


@SpringBootTest(classes = Application.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    public void setUp() {
        // Ensure the DINNER category exists
        if (categoryRepository.findByName("DINNER") == null) {
            categoryRepository.save(new Category("DINNER"));
        }

        // Ensure there is at least one recipe in the database
        if (recipeRepository.count() == 0) {
            Recipe recipe = new Recipe();
            recipe.setName("Grilled Chicken");
            recipe.setCategory(categoryRepository.findByName("DINNER"));
            recipeRepository.save(recipe);
        }

        // Ensure a user exists
        if (userRepository.findByUsername("alice") == null) {
            User alice = new User();
            alice.setUsername("alice");
            alice.setPassword(new BCryptPasswordEncoder().encode("password")); // Use the encoder
            userRepository.save(alice);
        }
    }

    // 1. Test the Home Page (`/home`)
    @Test
    public void testHomePageWithoutFilters() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("recipes", "categories", "allOption"));
    }

    @Test
    public void testHomePageWithQueryFilter() throws Exception {
        mockMvc.perform(get("/home").param("query", "chicken"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("recipes", "categories", "allOption"));
    }

    @Test
    public void testHomePageWithCategoryFilter() throws Exception {
        mockMvc.perform(get("/home").param("category", "dinner"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("recipes", "categories", "allOption"));
    }

    // 2. Test Recipes by Category (`/recipesByCategory`)
    @Test
    public void testRecipesByCategory() throws Exception {
        Category dinnerCategory = categoryRepository.findByName("DINNER");
    
        mockMvc.perform(get("/recipesByCategory")
                .param("categoryId", String.valueOf(dinnerCategory.getId()))
                .with(SecurityMockMvcRequestPostProcessors.user("alice"))) // Simulate authenticated user
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("recipes", "categories", "selectedCategoryId"));
    }
    

    // 3. Test Show Recipe Details (`/recipe/{id}`)
    @Test
    public void testShowRecipeDetails() throws Exception {
        mockMvc.perform(get("/recipe/1")
                .with(SecurityMockMvcRequestPostProcessors.user("alice"))) // Simulate authenticated user
                .andExpect(status().isOk())
                .andExpect(view().name("recipeDetails"))
                .andExpect(model().attributeExists("recipe", "reviews", "filter"));
    }
    

    // 4. Test Rate Recipe (`/recipe/{id}/rate`)
    @Test
    public void testRateRecipe() throws Exception {
        mockMvc.perform(post("/recipe/1/rate")
                .param("rating", "5")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("alice"))) // Simulate user
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipe/1")); // Redirect to recipe details
    }

    // 5. Test Add Recipe (`/addRecipe`)
    @Test
    public void testShowAddRecipeForm() throws Exception {
        mockMvc.perform(get("/addRecipe")
                .with(SecurityMockMvcRequestPostProcessors.user("alice"))) // Simulate authenticated user
                .andExpect(status().isOk())
                .andExpect(view().name("addRecipe"))
                .andExpect(model().attributeExists("categories", "recipe"));
    }

    @Test
    public void testAddRecipe() throws Exception {
        mockMvc.perform(post("/addRecipe")
                .param("name", "New Recipe")
                .param("ingredients", "Chicken, Salt")
                .param("instructions", "Cook for 20 minutes")
                .with(SecurityMockMvcRequestPostProcessors.csrf()) // Add CSRF token
                .with(SecurityMockMvcRequestPostProcessors.user("alice"))) // Simulate authenticated user
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile")); // Redirect after successful recipe addition
    }

 
}
