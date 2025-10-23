package com.elice.slowslow.domain.user.mapper;


import com.elice.slowslow.domain.user.User;
import com.elice.slowslow.domain.user.dto.MembershipDTO;
import com.elice.slowslow.domain.user.dto.MypageResponseDTO;
import com.elice.slowslow.domain.user.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User userDTOToUser(UserDTO userDTO);

    UserDTO userToUserDTO(User user);

    User mypageDtoToUser(MypageResponseDTO mypageResponseDTO);

    MypageResponseDTO userToMypageDto(User user);

    User membershipDtoToUser(MembershipDTO membershipDTO);
}
