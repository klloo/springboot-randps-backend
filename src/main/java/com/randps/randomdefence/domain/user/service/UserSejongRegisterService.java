package com.randps.randomdefence.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.randps.randomdefence.domain.user.domain.User;
import com.randps.randomdefence.domain.user.domain.UserRegister;
import com.randps.randomdefence.domain.user.domain.UserRegisterRepository;
import com.randps.randomdefence.domain.user.domain.UserRepository;
import com.randps.randomdefence.domain.user.dto.UserInfoResponse;
import com.randps.randomdefence.global.component.parser.BojProfileMessageParserImpl;
import com.randps.randomdefence.global.component.parser.BojProfileSchoolParserImpl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserSejongRegisterService {

    private final UserRepository userRepository;

    private final UserRegisterRepository userRegisterRepository;

    private final BojProfileMessageParserImpl bojProfileMessageParser;

    private final BojProfileSchoolParserImpl bojProfileSchoolParser;

    private final UserService userService;

    private final String SEJONG = "세종대학교";

    /*
     * 이미 회원가입한 유저인지 여부를 판단한다.
     */
    @Transactional
    public Boolean isAlreadyExistUser(String bojHandle) {
        return userRepository.findByBojHandle(bojHandle).isPresent();
    }

    /*
     * 세종대학교 학생인 유저인지 여부를 판단한다.
     */
    @Transactional
    public Boolean isSejongUser(String bojHandle) throws JsonProcessingException {
        List<Object> list = bojProfileSchoolParser.getBojUserProfile(bojHandle);

        for (Object o : list) {
            if (o.toString().contains(SEJONG))
                return true;
        }
        return false;
    }

    /*
     * 유저 인증을 위한 UUID를 생성한다.
     */
    @Transactional
    public String makeRegisterCode(String bojHandle) {
        Optional<UserRegister> alreadyUserRegister = userRegisterRepository.findByBojHandle(bojHandle);
        alreadyUserRegister.ifPresent(userRegisterRepository::delete); // 이미 유저의 회원가입 진행 기록이 존재한다면 삭제한다.

        UserRegister userRegister = UserRegister.builder()
                .bojHandle(bojHandle)
                .build();
        userRegisterRepository.save(userRegister);
        return userRegister.getCode();
    }

    /*
     * 유저의 상태메시지가 발급받은 UUID와 같은지 여부를 확인한다.
     */
    @Transactional
    public Boolean validBojUserMessage(String bojHandle) throws JsonProcessingException, IllegalAccessException {
        UserRegister userRegister = userRegisterRepository.findByBojHandle(bojHandle).orElseThrow(() -> new IllegalAccessException("Invalid Registration"));
        if (userRegister.getValid()) {
            throw new IllegalAccessException("Already Validating");
        }
        if (userRegister.isTimeOut()) {
            throw new IllegalAccessException("Registration Time Out");
        }

        String profileMessage = bojProfileMessageParser.getBojUserProfile(bojHandle).get(0).toString();
        if (profileMessage.contains(userRegister.getCode())) {
            userRegister.setValid();
            userRegisterRepository.save(userRegister);
            return true;
        }
        return false;
    }

    /*
     *
     */
    @Transactional
    public UserInfoResponse registerUser(String bojHandle, String password) throws JsonProcessingException {
        User user = userService.save(bojHandle, password, UUID.randomUUID().toString(), 0L, "");

        return user.toUserInfoResponse();
    }

}
