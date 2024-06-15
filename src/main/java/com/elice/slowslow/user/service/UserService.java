package com.elice.slowslow.user.service;

import com.elice.slowslow.user.User;
import com.elice.slowslow.user.dto.MypageResponseDTO;
import com.elice.slowslow.user.repository.UserRepository;
import com.elice.slowslow.user.dto.MembershipDTO;
import com.elice.slowslow.user.dto.UserDTO;
import com.elice.slowslow.user.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    public User membershipProcess(MembershipDTO membershipDto){

        if (userRepository.existsByUsername(membershipDto.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User membershipUser = mapper.membershipDtoToUser(membershipDto);

        membershipUser.setPassword(bCryptPasswordEncoder.encode(membershipUser.getPassword()));

        return userRepository.save(membershipUser);
    }

//    public void save(UserDTO userDTO) {
//        User user = mapper.userDTOToUser(userDTO);
//        userRepository.save(user);
//    }
//
//    public UserDTO login(UserDTO userDTO) {
//        Optional<User> byUserEmail = Optional.ofNullable(
//            userRepository.findByUsername(userDTO.getUsername()));
//        if(byUserEmail.isPresent()) {
//            //조회 결과가 있음(해당 이메일을 가진 회원정보가 있음)
//            User user = byUserEmail.get();
//            if(user.getPassword().equals(userDTO.getPassword())) {
//                //비밀번호 일치
//                UserDTO dto = mapper.userToUserDTO(user);
//                return dto;
//            } else {
//                //비밀번호 불일치(로그인 실패)
//                return null;
//            }
//        } else {
//            //조회 결과가 없음(해당 이메일을 가진 회원정보가 없음)
//            return null;
//        }
//    }
//
//    public List<UserDTO> findAll() {
//        List<User> userList = userRepository.findAll();
//        List<UserDTO> userDTOList = new ArrayList<>();
//        for(User user: userList) {
//            userDTOList.add(mapper.userToUserDTO(user));
//        }
//        return userDTOList;
//    }
//
//    public UserDTO findById(Long id) {
//        Optional<User> optionalUser = userRepository.findById(id);
//        if(optionalUser.isPresent()) {
//            return mapper.userToUserDTO(optionalUser.get());
//        } else {
//            return null;
//        }
//    }

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

//    public boolean checkPassword(UserDTO user, String inputPassword) {
//        // 실제로는 user 객체에서 비밀번호를 가져오는 방식에 따라 구현이 달라질 수 있습니다.
//        String storedPassword = user.getPassword(); // 예시: 저장된 비밀번호 가져오기
//
//        // 간단한 비밀번호 비교 예시
//        return storedPassword.equals(bCryptPasswordEncoder.encode(inputPassword));
//    }

    // 사용자 비밀번호 확인 메소드
    public boolean checkPassword(UserDTO user, String inputPassword) {
        // 사용자 정보가 null인 경우 예외 처리
        if (user == null) {
            throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다.");
        }

        String storedPassword = user.getPassword(); // 데이터베이스에서 가져온 저장된 해시된 비밀번호
        // 저장된 비밀번호가 null인 경우 예외 처리
        if (storedPassword == null) {
            throw new IllegalArgumentException("사용자의 비밀번호 정보가 올바르지 않습니다.");
        }

        // 사용자가 입력한 비밀번호와 저장된 비밀번호를 비교
        return bCryptPasswordEncoder.matches(inputPassword, storedPassword);
    }

//    public UserDTO updateForm(String myEmail) {
//        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(myEmail));
//        if(optionalUser.isPresent()) {
//            return mapper.userToUserDTO(optionalUser.get());
//        } else {
//            return null;
//        }
//    }
//
//    public UserDTO update(UserDTO userDTO) {
//        userRepository.save(mapper.userDTOToUser(userDTO));
//        return userDTO;
//    }

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


    public void deletedById(Long id) {
        userRepository.deleteById(id);
    }
}
