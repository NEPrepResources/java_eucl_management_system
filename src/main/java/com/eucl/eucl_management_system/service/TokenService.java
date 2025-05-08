package com.eucl.eucl_management_system.service;

import com.eucl.eucl_management_system.dto.request.PurchaseRequest;
import com.eucl.eucl_management_system.dto.response.TokenResponse;
import com.eucl.eucl_management_system.entity.Meter;
import com.eucl.eucl_management_system.entity.PurchasedToken;
import com.eucl.eucl_management_system.entity.User;
import com.eucl.eucl_management_system.exception.ResourceNotFoundException;
import com.eucl.eucl_management_system.repository.PurchasedTokenRepository;
import com.eucl.eucl_management_system.repository.UserRepository;
import com.eucl.eucl_management_system.repository.MeterRepository;
import org.hibernate.ResourceClosedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class TokenService {
    @Autowired
    private PurchasedTokenRepository purchasedTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MeterRepository meterRepository;

    @Value("${app.max-token-days}")
    private int maxTokenDays;
    @Value("${app.token-value-per-day}")
    private int tokenValuePerDay;

    public TokenResponse purchaseToken(PurchaseRequest request) {
        if(request.getAmount()%tokenValuePerDay!=0){
            throw new IllegalArgumentException("Amount must be multiple of" + tokenValuePerDay);
        }
        int tokenValueDays = request.getAmount()/tokenValuePerDay;
        if(tokenValueDays>maxTokenDays){
            throw new IllegalArgumentException("Can't purchase more than " + maxTokenDays + "days");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));
        Meter meter = meterRepository.findMeterByMeterNumber(request.getMeterNumber())
                .orElseThrow(()-> new ResourceClosedException("Meter not found"));
        if(!meter.getUser().getId().equals(user.getId())){
            throw new ResourceClosedException("Meter is not owned by the user");
        }

        String token = generateUniqueToken();

        PurchasedToken purchasedToken = new PurchasedToken(
                request.getMeterNumber(),
                token,
                tokenValueDays,
                request.getAmount(),
                user
        );
        PurchasedToken savedToken = purchasedTokenRepository.save(purchasedToken);
        return new TokenResponse(
                savedToken.getId(),
                savedToken.getMeterNumber(),
                savedToken.getToken(),
                savedToken.getTokenStatus().name(),
                savedToken.getTokenValueDays(),
                savedToken.getPurchaseDate(),
                savedToken.getAmount()
        );
    }
    private String generateUniqueToken() {
        Random random = new Random();
        String token;
        do{
            token = String.format("%016d", random.nextLong() & Long.MAX_VALUE);
            token = String.format("%16s", token).replace(' ', '0');
        }while(purchasedTokenRepository.existsByToken(token));
        return token;
    }
    public List<TokenResponse> getUserTokens(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));
        return purchasedTokenRepository.findByUser(user).stream()
                .map(token -> new TokenResponse(
                        token.getId(),
                        token.getMeterNumber(),
                        token.getToken(),
                        token.getTokenStatus().name(),
                        token.getTokenValueDays(),
                        token.getPurchaseDate(),
                        token.getAmount()
                ))
                .collect(Collectors.toList());
    }
    public List<TokenResponse> getMeterTokens(String meterNumber) {
        return purchasedTokenRepository.findByMeterNumber(meterNumber).stream()
                .map(token -> new TokenResponse(
                        token.getId(),
                        token.getMeterNumber(),
                        token.getToken(),
                        token.getTokenStatus().name(),
                        token.getTokenValueDays(),
                        token.getPurchaseDate(),
                        token.getAmount()
                ))
                .collect(Collectors.toList());
    }
    public List<TokenResponse> getAllTokens() {
        return purchasedTokenRepository.findAll().stream()
                .map(token -> new TokenResponse(
                        token.getId(),
                        token.getMeterNumber(),
                        token.getToken(),
                        token.getTokenStatus().name(),
                        token.getTokenValueDays(),
                        token.getPurchaseDate(),
                        token.getAmount()
                ))
                .collect(Collectors.toList());
    }
}
