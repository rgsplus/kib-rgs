package nl.rgs.kib.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.rgs.kib.model.user.User;
import nl.rgs.kib.model.user.dto.CreateUser;
import nl.rgs.kib.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/user")
@Tag(name = "User")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping()
    @Operation(
            summary = "Find all users",
            description = "Find all users",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found all users"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
            }
    )
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Find a user by id",
            description = "Find a user by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found the user"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content()
                    ),
            }
    )
    public ResponseEntity<User> findById(@PathVariable() String id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    @Operation(
            summary = "Create an user",
            description = "Create an user",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created the user"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
            }
    )
    public ResponseEntity<User> create(@Valid() @RequestBody() CreateUser createUser) {
        return ResponseEntity.status(201).body(userService.create(createUser));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an user",
            description = "Update an user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updated"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content()
                    ),
            }
    )
    public ResponseEntity<User> update(@Valid() @RequestBody() User user) {
        return userService.update(user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete an user by id",
            description = "Delete an user by id",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Deleted",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content()
                    ),
            }
    )
    public ResponseEntity<Void> deleteById(@PathVariable() String id) {
        return userService.deleteById(id)
                .map(inspectionMethod -> ResponseEntity.noContent().<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("email-exists/{email}")
    @Operation(
            summary = "Email exists",
            description = "Get if an email is already used by an user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get if an email is already used by an user"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
            }
    )
    public ResponseEntity<Boolean> emailExists(@PathVariable() String email) {
        return ResponseEntity.ok(userService.emailExists(email));
    }

    @GetMapping("username-exists/{username}")
    @Operation(
            summary = "Username exists",
            description = "Get if an username is already used by an user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get if an username is already used by an user"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
            }
    )
    public ResponseEntity<Boolean> usernameExists(@PathVariable() String username) {
        return ResponseEntity.ok(userService.usernameExists(username));
    }

    @GetMapping("admin-users-count")
    @Operation(
            summary = "Admin users count",
            description = "Get the count of admin users",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get the count of admin users"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
            }
    )
    public ResponseEntity<Long> adminUsersCount() {
        return ResponseEntity.ok(userService.adminUsersCount());
    }
}
