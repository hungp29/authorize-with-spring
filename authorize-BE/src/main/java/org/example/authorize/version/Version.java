package org.example.authorize.version;

import org.example.authorize.exception.InvalidValueException;
import org.example.authorize.exception.InvalidVersionException;
import org.springframework.util.StringUtils;

/**
 * Version.
 */
public class Version implements Comparable<Version> {

    private final int major;
    private final int minor;

    public Version(String version) {
        if (StringUtils.isEmpty(version)) {
            throw new InvalidValueException("Version cannot empty");
        }
        String[] tokens = version.split("\\.");

        if (tokens.length != 2) {
            throw new InvalidVersionException("Invalid version " + version + ". The version must have major and minor number.");
        }

        major = Integer.parseInt(tokens[0]);
        minor = Integer.parseInt(tokens[1]);
    }

    @Override
    public int compareTo(Version that) {
        int majorCompare = Integer.compare(this.major, that.major);
        int minorCompare = Integer.compare(this.minor, that.minor);
        return majorCompare != 0 ? majorCompare : minorCompare;
    }

    @Override
    public String toString() {
        return "V" + major + "." + minor;
    }
}
