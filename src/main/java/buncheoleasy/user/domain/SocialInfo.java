package buncheoleasy.user.domain;

import buncheoleasy.global.exception.domain.BusinessException;
import buncheoleasy.global.exception.domain.ErrorCode;
import java.util.Objects;
import java.util.regex.Pattern;

public record SocialInfo(SocialProvider provider, String providerId) {

    private static final Pattern PROVIDER_ID_REGEX = Pattern.compile("^[a-zA-Z0-9_-]+$");
    private static final int MAX_LENGTH = 100;

    public SocialInfo {
        validateProviderId(providerId);
    }

    public static SocialInfo of(final String providerValue, final String providerId) {
        SocialProvider provider = SocialProvider.from(providerValue);
        return new SocialInfo(provider, providerId);
    }

    private void validateProviderId(final String providerId) {
        if (providerId == null || providerId.isBlank()) {
            throw new BusinessException(ErrorCode.USER_SOCIAL_ID_REQUIRED);
        }
        if (providerId.length() > MAX_LENGTH) {
            throw new BusinessException(ErrorCode.USER_SOCIAL_ID_LENGTH_INVALID);
        }
        if (!PROVIDER_ID_REGEX.matcher(providerId).matches()) {
            throw new BusinessException(ErrorCode.USER_SOCIAL_ID_FORMAT_INVALID);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SocialInfo socialInfo = (SocialInfo) o;

        if (provider != socialInfo.provider) {
            return false;
        }
        return Objects.equals(providerId, socialInfo.providerId);
    }

    @Override
    public int hashCode() {
        int result = provider != null ? provider.hashCode() : 0;
        result = 31 * result + (providerId != null ? providerId.hashCode() : 0);
        return result;
    }
}
