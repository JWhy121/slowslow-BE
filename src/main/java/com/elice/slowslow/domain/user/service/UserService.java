package com.elice.slowslow.domain.user.service;

import com.elice.slowslow.domain.user.User;
import com.elice.slowslow.domain.user.dto.MypageResponseDTO;
import com.elice.slowslow.domain.user.repository.UserRepository;
import com.elice.slowslow.domain.user.dto.UserDTO;
import com.elice.slowslow.domain.user.mapper.UserMapper;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
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

    public User membershipProcess(User user){

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public boolean isEmailDuplicated(String username) {
        return userRepository.existsByUsername(username);
    }

//    public List<UserDTO> findAll() {
//        List<User> userList = userRepository.findAll();
//        List<UserDTO> userDTOList = new ArrayList<>();
//        for(User user: userList) {
//            userDTOList.add(mapper.userToUserDTO(user));
//        }
//        return userDTOList;
//    }
    public List<UserDTO> findAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
            .map(mapper::userToUserDTO)
            .collect(Collectors.toList());
    }


    public MypageResponseDTO findByNameProc(String username){
        User user = userRepository.findByUsername(username);
        if(!user.equals("")){
            return mapper.userToMypageDto(user);
        }

        return null;

    }

    public UserDTO findByName(String username){
        User user = userRepository.findByUsername(username);
        if(!user.equals("")){
            return mapper.userToUserDTO(user);
        }

        return null;

    }

    // 사용자 비밀번호 확인 메소드
    // 서비스 메서드 시그니처가 필요한 형식을 맞추도록 확인
    public boolean checkPassword(UserDTO user, String inputPassword) {
        if (user == null) {
            throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다.");
        }

        String storedPassword = user.getPassword();

        log.info("Input Password: {}", inputPassword);
        log.info("Stored Password: {}", storedPassword);
        if (!bCryptPasswordEncoder.matches(inputPassword, storedPassword)) {
            throw new IllegalArgumentException("사용자의 비밀번호 정보가 올바르지 않습니다.");
        }

        return true; // 여기까지 도달하면 비밀번호가 일치
    }

    public UserDTO update(UserDTO userDTO) {
        // 사용자 엔티티 생성
        User user = mapper.userDTOToUser(userDTO);

        // 비밀번호 암호화
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        // 사용자 정보 업데이트
        User updatedUser = userRepository.save(user);

        // 업데이트된 사용자 DTO 반환
        return mapper.userToUserDTO(updatedUser);
    }

    public void deletedByName(String username) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(username));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setDeleted(true);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public void restorationByName(String username) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(username));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setDeleted(false);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
