package com.elice.slowslow.user.service;

import com.elice.slowslow.user.User;
import com.elice.slowslow.user.repository.UserRepository;
import com.elice.slowslow.user.dto.MembershipDto;
import com.elice.slowslow.user.dto.UserDTO;
import com.elice.slowslow.user.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper mapper;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserMapper mapper){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mapper = mapper;
    }

    public void membershipProcess(MembershipDto membershipDto){

        String username = membershipDto.getUsername();
        String password = membershipDto.getPassword();
        String name = membershipDto.getName();
        String phoneNumber = membershipDto.getPhoneNumber();
        User.RoleType role = membershipDto.getRole();

        User user = new User();

        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        user.setRole(role);

        userRepository.save(user);
    }

    public void save(UserDTO userDTO) {
        User user = User.toUser(userDTO);
        userRepository.save(user);
    }

    public UserDTO login(UserDTO userDTO) {
        Optional<User> byUserEmail = userRepository.findByEmail(userDTO.getUserEmail());
        if(byUserEmail.isPresent()) {
            //조회 결과가 있음(해당 이메일을 가진 회원정보가 있음)
            User user = byUserEmail.get();
            if(user.getPassword().equals(userDTO.getUserPassword())) {
                //비밀번호 일치
                UserDTO dto = UserDTO.toUserDTO(user);
                return dto;
            } else {
                //비밀번호 불일치(로그인 실패)
                return null;
            }
        } else {
            //조회 결과가 없음(해당 이메일을 가진 회원정보가 없음)
            return null;
        }
    }

    public List<UserDTO> findAll() {
        List<User> userList = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        for(User user: userList) {
            userDTOList.add(UserDTO.toUserDTO(user));
//            UserDTO userDTO = UserDTO.toUserDTO(user);
//            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    public UserDTO findById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()) {
            return UserDTO.toUserDTO(optionalUser.get());
        } else {
            return null;
        }
    }

    public UserDTO updateForm(String myEmail) {
        Optional<User> optionalUser = userRepository.findByEmail(myEmail);
        if(optionalUser.isPresent()) {
            return UserDTO.toUserDTO(optionalUser.get());
        } else {
            return null;
        }
    }

    public void update(UserDTO userDTO) {
        userRepository.save(User.toUpdateUser(userDTO));
    }

    public void deletedById(Long id) {
        userRepository.deleteById(id);
    }

}
