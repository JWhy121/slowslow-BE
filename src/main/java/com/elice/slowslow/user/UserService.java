package com.elice.slowslow.user;

import com.elice.slowslow.user.dto.MembershipDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void membershipProcess(MembershipDto membershipDto){
        String name = membershipDto.getName();
        String memberId = membershipDto.getMemberId();
        String password = membershipDto.getPassword();
        String phoneNumber = membershipDto.getPhoneNumber();
        String email = membershipDto.getEmail();
        User.RoleType role = membershipDto.getRole();

        User user = new User();

        user.setName(name);
        user.setMemberId(memberId);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setRole(role);

        userRepository.save(user);
    }
}
