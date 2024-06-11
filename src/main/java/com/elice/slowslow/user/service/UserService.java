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

    public User membershipProcess(MembershipDto membershipDto){

        if (userRepository.existsByUsername(membershipDto.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

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

        return user;
    }

    public void save(UserDTO userDTO) {
        User user = mapper.userDTOToUser(userDTO);
        userRepository.save(user);
    }

    public UserDTO login(UserDTO userDTO) {
        Optional<User> byUserEmail = Optional.ofNullable(
            userRepository.findByUsername(userDTO.getUsername()));
        if(byUserEmail.isPresent()) {
            //조회 결과가 있음(해당 이메일을 가진 회원정보가 있음)
            User user = byUserEmail.get();
            if(user.getPassword().equals(userDTO.getPassword())) {
                //비밀번호 일치
                UserDTO dto = mapper.userToUserDTO(user);
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
            userDTOList.add(mapper.userToUserDTO(user));
        }
        return userDTOList;
    }

    public UserDTO findById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()) {
            return mapper.userToUserDTO(optionalUser.get());
        } else {
            return null;
        }
    }

    public UserDTO updateForm(String myEmail) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(myEmail));
        if(optionalUser.isPresent()) {
            return mapper.userToUserDTO(optionalUser.get());
        } else {
            return null;
        }
    }

    public void update(UserDTO userDTO) {
        userRepository.save(mapper.userDTOToUser(userDTO));
    }

    public void deletedById(Long id) {
        userRepository.deleteById(id);
    }
}
