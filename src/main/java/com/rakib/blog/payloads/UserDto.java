package com.rakib.blog.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private int id;

    @NotEmpty
    @Size(min = 4, message = "user name must be 4 character")
    private String name;

    @Email(message = "Your email is not valid")
    private String email;

    @NotEmpty
    @Size(min = 4, max = 10, message = "Password must be min 4 character and maximum 10 character")
    private String password;

    @NotEmpty
    private String about;

    private String imageUrl;
}
