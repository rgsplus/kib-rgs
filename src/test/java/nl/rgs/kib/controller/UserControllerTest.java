package nl.rgs.kib.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rgs.kib.model.user.User;
import nl.rgs.kib.model.user.UserRole;
import nl.rgs.kib.model.user.dto.CreateUser;
import nl.rgs.kib.service.ApiAccountService;
import nl.rgs.kib.service.UserService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    private final static String domain = "/user";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private ApiAccountService apiAccountService;

    @Test
    @WithMockUser
    public void findAll_Returns200() throws Exception {
        mockMvc.perform(get(domain)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).findAll();
    }

    @Test
    public void findAll_WithoutAuthentication_Returns401() throws Exception {
        mockMvc.perform(get(domain)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void findById_WhenExists_Returns200() throws Exception {
        String id = new ObjectId().toHexString();
        User user = new User();
        user.setId(id);

        when(userService.findById(id)).thenReturn(Optional.of(user));

        mockMvc.perform(get(domain + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).findById(id);
    }

    @Test
    @WithMockUser
    public void findById_WhenNotExists_Returns404() throws Exception {
        String id = new ObjectId().toHexString();
        when(userService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get(domain + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService).findById(id);
    }

    @Test
    public void findById_WithoutAuthentication_Returns401() throws Exception {
        String id = new ObjectId().toHexString();
        mockMvc.perform(get(domain + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void create_Returns201() throws Exception {
        CreateUser createUser = new CreateUser(
                "firstName",
                "lastName",
                "templates/email",
                UserRole.USER,
                false
        );


        User user = new User();
        user.setId(new ObjectId().toHexString());
        user.setFirstName(createUser.firstName());
        user.setLastName(createUser.lastName());
        user.setEmail(createUser.email());
        user.setRole(createUser.role());

        when(userService.create(createUser)).thenReturn(user);

        mockMvc.perform(post(domain)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUser)))
                .andExpect(status().isCreated());

        verify(userService).create(createUser);
    }

    @Test
    @WithMockUser
    public void create_WhenInvalid_Returns400() throws Exception {
        CreateUser createUser = new CreateUser(
                "firstName",
                "lastName",
                null,
                UserRole.USER,
                false
        );

        mockMvc.perform(post(domain)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_WithoutAuthentication_Returns401() throws Exception {
        CreateUser createUser = new CreateUser(
                "firstName",
                "lastName",
                "templates/email",
                UserRole.USER,
                false
        );

        mockMvc.perform(post(domain)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUser)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void update_WhenExists_Returns200() throws Exception {
        User user = new User();
        user.setId(new ObjectId().toHexString());
        user.setEmail("email updated");
        user.setLastName("lastname");
        user.setFirstName("username");
        user.setRole(UserRole.USER);
        user.setTwoFactorAuthentication(false);

        when(userService.update(user)).thenReturn(Optional.of(user));

        mockMvc.perform(put(domain + "/" + user.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        verify(userService).update(user);
    }

    @Test
    @WithMockUser
    public void update_WhenInvalid_Returns400() throws Exception {
        User user = new User();
        user.setId(new ObjectId().toHexString());
        user.setEmail(null);
        user.setLastName("lastname");
        user.setFirstName("username");

        mockMvc.perform(put(domain + "/" + user.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void update_WhenNotExists_Returns404() throws Exception {
        User user = new User();
        user.setId(new ObjectId().toHexString());
        user.setEmail("email");
        user.setLastName("lastname");
        user.setFirstName("username");
        user.setRole(UserRole.USER);
        user.setTwoFactorAuthentication(false);

        when(userService.update(user)).thenReturn(Optional.empty());

        mockMvc.perform(put(domain + "/" + user.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());

        verify(userService).update(user);
    }

    @Test
    public void update_WithoutAuthentication_Returns401() throws Exception {
        User user = new User();
        user.setId(new ObjectId().toHexString());
        user.setEmail("templates/email");
        user.setLastName("lastname");
        user.setFirstName("username");

        mockMvc.perform(put(domain + "/" + user.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void deleteById_WhenExists_Returns204() throws Exception {
        User user = new User();
        user.setId(new ObjectId().toHexString());

        when(userService.deleteById(user.getId())).thenReturn(Optional.of(user));

        mockMvc.perform(delete(domain + "/" + user.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService).deleteById(user.getId());
    }

    @Test
    @WithMockUser
    public void deleteById_WhenNotExists_Returns404() throws Exception {
        String id = new ObjectId().toHexString();

        when(userService.deleteById(id)).thenReturn(Optional.empty());

        mockMvc.perform(delete(domain + "/" + id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService).deleteById(id);
    }

    @Test
    public void deleteById_WithoutAuthentication_Returns401() throws Exception {
        String id = new ObjectId().toHexString();

        mockMvc.perform(delete(domain + "/" + id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void emailExist_Returns200() throws Exception {
        String email = "example@email.com";

        when(userService.emailExists(email)).thenReturn(true);

        mockMvc.perform(get(domain + "/email-exists/" + email)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).emailExists(email);
    }

    @Test
    public void emailExist_Returns401() throws Exception {
        String email = "example@email.com";

        mockMvc.perform(get(domain + "/email-exists/" + email)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void usernameExist_Returns200() throws Exception {
        String username = "john.doe";

        when(userService.usernameExists(username)).thenReturn(true);

        mockMvc.perform(get(domain + "/username-exists/" + username)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).usernameExists(username);
    }

    @Test
    public void usernameExist_Returns401() throws Exception {
        String username = "john.doe";

        mockMvc.perform(get(domain + "/username-exists/" + username)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}