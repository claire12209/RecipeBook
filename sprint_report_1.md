# Sprint x Report 
Video Link: 
## What's New (User Facing)
 * User Registration and Login system implemented as one feature.
 * Styling of Home Page and Profile Page.
 * Connection of Spring Boot to MySQL database for storing users.

## Work Summary (Developer Facing)
During Sprint 1, we focused on building the foundation of our Recipe Book Website. 
The team worked on core functionality such as user registration and login, integrating 
them into one feature. We started styling both the home and profile pages to 
create a clean and user-friendly interface. Additionally, we tackled connecting 
Spring Boot to a MySQL database, ensuring user data is securely stored. One of the 
key challenges we overcame was learning how to configure and integrate the database 
with our Spring Boot application. This sprint served as an important learning phase, 
enabling the team to become more familiar with the technology stack and setting the groundwork 
for faster progress in upcoming sprints.

## Unfinished Work
Profile Page Styling: We made progress on the front-end styling for the profile page but didn't complete it. We encountered some challenges with consistent design across different screen sizes and layouts. This work will be carried over to the next sprint for further refinement.

Home Page Styling: While the core functionality of the home page is working, we are still in the process of finalizing its design. We need to improve the user interface and ensure that it's responsive and aligned with our project's style guidelines. This will be continued in the next sprint.

## Completed Issues/User Stories
Here are links to the issues that we completed in this sprint:

 * [URL of issue 1](https://github.com/claire12209/RecipeBook/issues/1)
 * [URL of issue 2](https://github.com/claire12209/RecipeBook/pull/9)
 * [URL of issue 3](https://github.com/claire12209/RecipeBook/issues/10)
 
 ## Incomplete Issues/User Stories
 Here are links to issues we worked on but did not complete in this sprint:
 
 * [URL of issue 1](https://github.com/claire12209/RecipeBook/issues/11) <<Did not have enough time to completely finish styling the home and profile pages but did make a good amount of progress.>>

## Code Files for Review
Please review the following code files, which were actively developed during this sprint, for quality:
Following all in springboot folder. URL: https://github.com/claire12209/RecipeBook/tree/main/RecipeBook/initial/src/main/java/com/example/springboot
 * HomeController.java
 * ProfileController.java
 * RegistrationController.java
 * SecurityConfig.java
 * User.java
 * UserDetailsServiceImpl.java
 * UserRegistrationDto.java
 * UserRepository.java
 * UserService.java
 Following all in templates folder. URL: https://github.com/claire12209/RecipeBook/tree/main/RecipeBook/initial/src/main/resources/templates
 * home.html
 * login.html
 * register.html
 * profile.html
 
## Retrospective Summary
Here's what went well:
  * The team collaborated effectively to navigate through initial challenges.
  * We successfully integrated Spring Boot with MySQL, creating a working database connection.
  * Core features such as user registration and login, and page styling were completed.
 
Here's what we'd like to improve:
   * We aim to further refine our code structure for clarity and maintainability.
   * We can improve the efficiency of testing by automating more parts of the process.
   * We will focus on improving the overall user interface to make it more intuitive and visually engaging.
  
Here are changes we plan to implement in the next sprint:
   * Focus on improving the front-end user experience by refining the look and feel of the site.
   * We will work on store recipes into the database
   * Plan on working on the search features and sorting by catagory