package com.pocketpick.user.domain.domain;

import com.pocketpick.user.domain.domain.constants.UserPolicyConstants;
import com.pocketpick.user.domain.domain.exception.InvalidNicknameException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile {
    private String nickname;
    private String profileImageUrl;
    private String region;

    public UserProfile(String nickname, String profileImageUrl, String region) {
        validateNickname(nickname);
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.region = region;
    }

    private static void validateNickname(String nickname) {
        if (nickname == null || nickname.length() < UserPolicyConstants.MIN_NICKNAME_LENGTH || nickname.length() > UserPolicyConstants.MAX_NICKNAME_LENGTH) {
            throw new InvalidNicknameException();
        }
    }
}
