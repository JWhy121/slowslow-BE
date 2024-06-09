package com.elice.slowslow.user.mapper;


import com.elice.slowslow.user.User;
import com.elice.slowslow.user.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User userDTOToUser(UserDTO userDTO);

    UserDTO userToUserDTO(User user);

}
