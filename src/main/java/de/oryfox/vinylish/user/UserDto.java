package de.oryfox.vinylish.user;

import de.oryfox.vinylish.ImageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private Long id;
    private String email;
    private String displayName;
    private ImageType imageType;

    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.displayName = user.getDisplayName();
        this.imageType = user.getImageType() == null ? ImageType.DEFAULT : user.getImageType();
    }
}
