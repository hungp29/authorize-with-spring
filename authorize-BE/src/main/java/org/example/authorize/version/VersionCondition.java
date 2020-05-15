package org.example.authorize.version;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.condition.AbstractRequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Version Condition.
 */
public class VersionCondition extends AbstractRequestCondition<VersionCondition> {

    private List<Version> versions;
    private String acceptMediaType;

    public VersionCondition(String... version) {
        this.versions = Arrays.stream(version).map(Version::new).collect(Collectors.toList());
    }

    public VersionCondition(List<Version> version) {
        this.versions = version;
    }

    @Override
    protected Collection<?> getContent() {
        return versions;
    }

    @Override
    protected String getToStringInfix() {
        return "], [";
    }

    @Override
    public VersionCondition combine(VersionCondition other) {
        return other;
    }

    @Override
    public VersionCondition getMatchingCondition(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        Pattern regexPattern = Pattern.compile("(.*)-v(\\d+\\.\\d+).*");
        Matcher matcher = regexPattern.matcher(accept);

        if (matcher.matches()) {
            String APIMediaType = matcher.group(1);

            if (StringUtils.isEmpty(acceptMediaType) || acceptMediaType.equals(APIMediaType)) {
                Version APIVersion = new Version(matcher.group(2));

                if (versions.stream().anyMatch(compareVersion -> compareVersion.compareTo(APIVersion) == 0)) {
                    return this;
                }
            }
        }
        return null;
    }

    @Override
    public int compareTo(VersionCondition other, HttpServletRequest request) {
        return 0;
    }

    /**
     * Set Accept Media Type.
     *
     * @param acceptMediaType the accept media type
     */
    public void setAcceptMediaType(String acceptMediaType) {
        this.acceptMediaType = acceptMediaType;
    }
}
