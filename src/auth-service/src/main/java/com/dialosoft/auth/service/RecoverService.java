package com.dialosoft.auth.service;

import com.dialosoft.auth.persistence.entity.SeedPhraseEntity;
import com.dialosoft.auth.persistence.entity.UserEntity;
import com.dialosoft.auth.persistence.repository.SeedPhraseRepository;
import com.dialosoft.auth.persistence.repository.UserRepository;
import com.dialosoft.auth.web.config.error.exception.CustomTemplateException;
import com.dialosoft.auth.web.config.jwt.JwtUtil;
import com.dialosoft.auth.web.dto.request.ChangePasswordRequest;
import com.dialosoft.auth.web.dto.request.RecoverChangePasswordRequest;
import com.dialosoft.auth.web.dto.request.RecoverRequest;
import com.dialosoft.auth.web.dto.response.RecoverTokenResponse;
import com.dialosoft.auth.web.dto.response.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecoverService {


    private final BCryptPasswordEncoder bcrypt;
    private final UserRepository userRepository;
    private final SeedPhraseRepository seedPhraseRepository;
    private final UserSecurityService userSecurityService;
    private final JwtUtil jwtUtil;

    private static final String[] WORDS = {
            "apple", "banana", "cherry", "date", "elderberry", "fig", "grape", "honeydew", "kiwi", "lemon",
            "mango", "nectarine", "orange", "papaya", "quince", "raspberry", "strawberry", "tangerine", "ugli", "vine",
            "watermelon", "xigua", "yam", "zucchini", "apricot", "blueberry", "cantaloupe", "dragonfruit", "eggplant", "feijoa",
            "guava", "huckleberry", "ilama", "jackfruit", "kumquat", "lime", "mulberry", "nashi", "olive", "peach",
            "quenepa", "rosehip", "soursop", "tomato", "ube", "voavanga", "walnut", "ximenia", "yucca", "ziziphus",
            "acerola", "bilberry", "currant", "dewberry", "elder", "fingerlime", "gooseberry", "hackberry", "jaboticaba", "kei apple",
            "lychee", "marionberry", "navel", "oat", "persimmon", "quinoa", "rowan", "sapote", "thimbleberry", "ulluco",
            "velvet apple", "wineberry", "xenia", "yellow", "zapote", "almond", "beet", "carrot", "daikon", "endive",
            "fennel", "ginger", "horseradish", "iceberg", "jicama", "kale", "leek", "mustard", "nopales", "okra",
            "parsley", "quail", "rutabaga", "spinach", "turnip", "cool", "vanilla", "wasabi", "xoconostle", "yautia",
            "zedoary", "margarita", "vault", "shield",
    };

    public List<String> generateSeedPhrase(int length) {
        List<String> words = new ArrayList<>(List.of(WORDS));
        Collections.shuffle(words);
        return words.subList(0, length);
    }

    public String hashSeedPhrase(List<String> seedPhrase) {
        String combinedPhrase = String.join(";", seedPhrase);
        return bcrypt.encode(combinedPhrase);
    }

    public Boolean compareHash(List<String> seedPhrase, String savedSeedPhrase) {

        String combinedPhrase = String.join(";", seedPhrase);
        // bcrypt.matches() already encode combinedPhrase to compare with savedSeedPhrase
        return bcrypt.matches(combinedPhrase, savedSeedPhrase);
    }

    public ResponseBody<RecoverTokenResponse> checkHashPhraseAndGetRecoverToken(RecoverRequest request) {

        UserEntity userEntity = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomTemplateException(String.format("User not found with this username: %s", request.getUsername()), null, HttpStatus.NOT_FOUND));

        SeedPhraseEntity seedPhraseEntity = seedPhraseRepository.findSeedPhraseEntityByUserId(userEntity.getId())
                .orElseThrow(() -> new CustomTemplateException(String.format("Seed phrase not found for this user: %s", request.getUsername()), null, HttpStatus.NOT_FOUND));

        UserDetails userDetails = userSecurityService.loadUserByUsername(request.getUsername());
        String recoverToken = jwtUtil.generateRecoverToken(userEntity.getId(), userDetails.getUsername(), userDetails.getAuthorities());
        RecoverTokenResponse response = RecoverTokenResponse.builder()
                .recoverToken(recoverToken)
                .build();

        if (!compareHash(request.seedPhrase, seedPhraseEntity.getHashPhrase())) {

            throw new CustomTemplateException("Recover password failed, incorrect seed phrase", null, HttpStatus.UNAUTHORIZED);
        }

        return ResponseBody.<RecoverTokenResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Recover token generated successfully")
                .data(response)
                .build();

    }

    public ResponseBody<?> applyRecoverChangePassword(RecoverChangePasswordRequest request, String recoverToken) {

        if (recoverToken == null || recoverToken.isEmpty()) {
            throw new CustomTemplateException("Invalid recover token", null, HttpStatus.UNAUTHORIZED);
        }

        if (request.getNewPassword().isEmpty()) {
            throw new CustomTemplateException("New password is empty", null, HttpStatus.BAD_REQUEST);
        }

        try {

            if (!jwtUtil.isValid(recoverToken)) {
                throw new CustomTemplateException("Invalid recover token", null, HttpStatus.UNAUTHORIZED);
            }

            String subjectUserNameFromToken = jwtUtil.getUsername(recoverToken);

            UserEntity userEntity = userRepository.findByUsername(subjectUserNameFromToken)
                    .orElseThrow(() -> new CustomTemplateException(String.format("User not found with this username: %s", subjectUserNameFromToken), null, HttpStatus.NOT_FOUND));

            String passwordHashed = bcrypt.encode(request.getNewPassword());
            userEntity.setPassword(passwordHashed);

            userRepository.save(userEntity);

            return ResponseBody.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Recover password successfully")
                    .data(null)
                    .build();

        } catch (Exception e) {
            throw new CustomTemplateException("Recover password failed", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseBody<?> applyAuthenticatedChangePassword(ChangePasswordRequest request) {

        try {

            // Obtener la autenticación actual
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                // Obtener el nombre de usuario
                String username = authentication.getName();

                UserEntity userEntity = userRepository.findByUsername(username)
                        .orElseThrow(() -> new CustomTemplateException(String.format("User not found with this username: %s", username), null, HttpStatus.NOT_FOUND));

                // TODO: Add again after event
//                SeedPhraseEntity seedPhraseEntity = seedPhraseRepository.findSeedPhraseEntityByUserId(userEntity.getId())
//                        .orElseThrow(() -> new CustomTemplateException(String.format("Seed phrase not found for this user: %s", username), null, HttpStatus.NOT_FOUND));
//
//                if (!compareHash(request.seedPhrase, seedPhraseEntity.getHashPhrase())) {
//
//                    throw new CustomTemplateException("Recover password failed, incorrect seed phrase", null, HttpStatus.UNAUTHORIZED);
//                }

                String passwordHashed = bcrypt.encode(request.getNewPassword());
                userEntity.setPassword(passwordHashed);

                userRepository.save(userEntity);

                return ResponseBody.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("password changed successfully")
                        .data(null)
                        .build();

            } else {
                throw new CustomTemplateException("User from accessToken is not authenticated", null, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            throw new CustomTemplateException("Change password failed", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}