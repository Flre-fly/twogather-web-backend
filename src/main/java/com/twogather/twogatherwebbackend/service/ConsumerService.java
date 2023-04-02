package com.twogather.twogatherwebbackend.service;

import com.twogather.twogatherwebbackend.domain.AuthenticationType;
import com.twogather.twogatherwebbackend.domain.Consumer;
import com.twogather.twogatherwebbackend.dto.member.ConsumerResponse;
import com.twogather.twogatherwebbackend.dto.member.ConsumerSaveRequest;
import com.twogather.twogatherwebbackend.exception.MemberException;
import com.twogather.twogatherwebbackend.repository.ConsumerRepository;
import com.twogather.twogatherwebbackend.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.twogather.twogatherwebbackend.exception.MemberException.MemberErrorCode.NO_SUCH_EMAIL;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsumerService {
    private final ConsumerRepository consumerRepository;
    private final PasswordEncoder passwordEncoder;

    public ConsumerResponse join(final ConsumerSaveRequest request){
        validateDuplicateEmail(request.getEmail());
        Consumer consumer
                = new Consumer(request.getEmail(), passwordEncoder.encode(request.getPassword()),
                request.getName(), request.getPhone(), AuthenticationType.CONSUMER, true);
        Consumer storedConsumer = consumerRepository.save(consumer);

        return toConsumerResponse(storedConsumer);
    }

    @Transactional(readOnly = true)
    public ConsumerResponse getMemberWithAuthorities(final String email) {
        Consumer consumer = findMemberByEmailOrElseThrow(email);

        return toConsumerResponse(consumer);
    }
    @Transactional(readOnly = true)
    public ConsumerResponse getMemberWithAuthorities(){
        Optional<Consumer> optionalConsumer = SecurityUtils.getCurrentUsername().flatMap(consumerRepository::findByEmail);
        optionalConsumer.orElseThrow(()-> new MemberException(NO_SUCH_EMAIL));
        Consumer consumer = optionalConsumer.get();

        return toConsumerResponse(consumer);
    }

    private Consumer findMemberByEmailOrElseThrow(final String email){
        return consumerRepository.findByEmail(email).orElseThrow(() -> new MemberException(NO_SUCH_EMAIL));
    }
    private void validateDuplicateEmail(final String email){
        if (consumerRepository.existsByEmail(email)) {
            throw new MemberException(MemberException.MemberErrorCode.DUPLICATE_EMAIL);
        }
    }
    private ConsumerResponse toConsumerResponse(Consumer consumer){
        return new ConsumerResponse(consumer.getMemberId(), consumer.getName(), consumer.getEmail(), consumer.getPhone());
    }
}