/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.mccue.boba.org.apache.commons.lang3;

/**
 * Helpers for {@link System}.
 *
 * <p>
 * If a system property cannot be read due to security restrictions, the corresponding field in this class will be set
 * to {@code null} and a message will be written to {@code System.err}.
 * </p>
 * <p>
 * #ThreadSafe#
 * </p>
 *
 * @since 1.0
 * @see SystemProperties
 */
public class SystemUtils {

    /**
     * The prefix String for all Windows OS.
     */
    private static final String OS_NAME_WINDOWS_PREFIX = "Windows";

    // System property constants
    // -----------------------------------------------------------------------
    // These MUST be declared first. Other constants depend on this.

    /**
     * The {@code os.name} System Property. Operating system name.
     *
     * <p>
     * Defaults to {@code null} if the runtime does not have security access to read this property or the property does
     * not exist.
     * </p>
     * <p>
     * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
     * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value will be out of
     * sync with that System property.
     * </p>
     *
     * @see SystemProperties#getOsName()
     * @since Java 1.1
     */
    public static final String OS_NAME = SystemProperties.getOsName();

    /**
     * The {@code os.version} System Property. Operating system version.
     *
     * <p>
     * Defaults to {@code null} if the runtime does not have security access to read this property or the property does
     * not exist.
     * </p>
     * <p>
     * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
     * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value will be out of
     * sync with that System property.
     * </p>
     *
     * @see SystemProperties#getOsVersion()
     * @since Java 1.1
     */
    public static final String OS_VERSION = SystemProperties.getOsVersion();

    // Operating system checks
    // -----------------------------------------------------------------------
    // These MUST be declared after those above as they depend on the
    // values being set up
    // Please advise dev@commons.apache.org if you want another added
    // or a mistake corrected

    /**
     * Is {@code true} if this is AIX.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 2.0
     */
    public static final boolean IS_OS_AIX = getOsMatchesName("AIX");

    /**
     * Is {@code true} if this is HP-UX.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 2.0
     */
    public static final boolean IS_OS_HP_UX = getOsMatchesName("HP-UX");

    /**
     * Is {@code true} if this is IBM OS/400.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.3
     */
    public static final boolean IS_OS_400 = getOsMatchesName("OS/400");

    /**
     * Is {@code true} if this is Irix.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 2.0
     */
    public static final boolean IS_OS_IRIX = getOsMatchesName("Irix");

    /**
     * Is {@code true} if this is Linux.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 2.0
     */
    public static final boolean IS_OS_LINUX = getOsMatchesName("Linux") || getOsMatchesName("LINUX");

    /**
     * Is {@code true} if this is Mac.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 2.0
     */
    public static final boolean IS_OS_MAC = getOsMatchesName("Mac");

    /**
     * Is {@code true} if this is Mac.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 2.0
     */
    public static final boolean IS_OS_MAC_OSX = getOsMatchesName("Mac OS X");

    /**
     * Is {@code true} if this is macOS X Cheetah.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.4
     */
    public static final boolean IS_OS_MAC_OSX_CHEETAH = getOsMatches("Mac OS X", "10.0");

    /**
     * Is {@code true} if this is macOS X Puma.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.4
     */
    public static final boolean IS_OS_MAC_OSX_PUMA = getOsMatches("Mac OS X", "10.1");

    /**
     * Is {@code true} if this is macOS X Jaguar.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.4
     */
    public static final boolean IS_OS_MAC_OSX_JAGUAR = getOsMatches("Mac OS X", "10.2");

    /**
     * Is {@code true} if this is macOS X Panther.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.4
     */
    public static final boolean IS_OS_MAC_OSX_PANTHER = getOsMatches("Mac OS X", "10.3");

    /**
     * Is {@code true} if this is macOS X Tiger.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.4
     */
    public static final boolean IS_OS_MAC_OSX_TIGER = getOsMatches("Mac OS X", "10.4");

    /**
     * Is {@code true} if this is macOS X Leopard.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.4
     */
    public static final boolean IS_OS_MAC_OSX_LEOPARD = getOsMatches("Mac OS X", "10.5");

    /**
     * Is {@code true} if this is macOS X Snow Leopard.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.4
     */
    public static final boolean IS_OS_MAC_OSX_SNOW_LEOPARD = getOsMatches("Mac OS X", "10.6");

    /**
     * Is {@code true} if this is macOS X Lion.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.4
     */
    public static final boolean IS_OS_MAC_OSX_LION = getOsMatches("Mac OS X", "10.7");

    /**
     * Is {@code true} if this is macOS X Mountain Lion.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.4
     */
    public static final boolean IS_OS_MAC_OSX_MOUNTAIN_LION = getOsMatches("Mac OS X", "10.8");

    /**
     * Is {@code true} if this is macOS X Mavericks.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.4
     */
    public static final boolean IS_OS_MAC_OSX_MAVERICKS = getOsMatches("Mac OS X", "10.9");

    /**
     * Is {@code true} if this is macOS X Yosemite.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.4
     */
    public static final boolean IS_OS_MAC_OSX_YOSEMITE = getOsMatches("Mac OS X", "10.10");

    /**
     * Is {@code true} if this is macOS X El Capitan.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.5
     */
    public static final boolean IS_OS_MAC_OSX_EL_CAPITAN = getOsMatches("Mac OS X", "10.11");

    /**
     * Is {@code true} if this is macOS X Sierra.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.12.0
     */
    public static final boolean IS_OS_MAC_OSX_SIERRA = getOsMatches("Mac OS X", "10.12");

    /**
     * Is {@code true} if this is macOS X High Sierra.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.12.0
     */
    public static final boolean IS_OS_MAC_OSX_HIGH_SIERRA = getOsMatches("Mac OS X", "10.13");

    /**
     * Is {@code true} if this is macOS X Mojave.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.12.0
     */
    public static final boolean IS_OS_MAC_OSX_MOJAVE = getOsMatches("Mac OS X", "10.14");

    /**
     * Is {@code true} if this is macOS X Catalina.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.12.0
     */
    public static final boolean IS_OS_MAC_OSX_CATALINA = getOsMatches("Mac OS X", "10.15");

    /**
     * Is {@code true} if this is macOS X Big Sur.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.12.0
     */
    public static final boolean IS_OS_MAC_OSX_BIG_SUR = getOsMatches("Mac OS X", "11");

    /**
     * Is {@code true} if this is macOS X Monterey.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     * @since 3.13.0
     */
    public static final boolean IS_OS_MAC_OSX_MONTEREY = getOsMatches("Mac OS X", "12");

    /**
     * Is {@code true} if this is macOS X Ventura.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     * @since 3.13.0
     */
    public static final boolean IS_OS_MAC_OSX_VENTURA = getOsMatches("Mac OS X", "13");

    /**
     * Is {@code true} if this is FreeBSD.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.1
     */
    public static final boolean IS_OS_FREE_BSD = getOsMatchesName("FreeBSD");

    /**
     * Is {@code true} if this is OpenBSD.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.1
     */
    public static final boolean IS_OS_OPEN_BSD = getOsMatchesName("OpenBSD");

    /**
     * Is {@code true} if this is NetBSD.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.1
     */
    public static final boolean IS_OS_NET_BSD = getOsMatchesName("NetBSD");

    /**
     * Is {@code true} if this is OS/2.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 2.0
     */
    public static final boolean IS_OS_OS2 = getOsMatchesName("OS/2");

    /**
     * Is {@code true} if this is Solaris.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 2.0
     */
    public static final boolean IS_OS_SOLARIS = getOsMatchesName("Solaris");

    /**
     * Is {@code true} if this is SunOS.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 2.0
     */
    public static final boolean IS_OS_SUN_OS = getOsMatchesName("SunOS");

    /**
     * Is {@code true} if this is a UNIX like system, as in any of AIX, HP-UX, Irix, Linux, MacOSX, Solaris or SUN OS.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 2.1
     */
    public static final boolean IS_OS_UNIX = IS_OS_AIX || IS_OS_HP_UX || IS_OS_IRIX || IS_OS_LINUX || IS_OS_MAC_OSX
                                             || IS_OS_SOLARIS || IS_OS_SUN_OS || IS_OS_FREE_BSD || IS_OS_OPEN_BSD || IS_OS_NET_BSD;

    /**
     * Is {@code true} if this is Windows.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 2.0
     */
    public static final boolean IS_OS_WINDOWS = getOsMatchesName(OS_NAME_WINDOWS_PREFIX);

    /**
     * Is {@code true} if this is Windows 2000.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 2.0
     */
    public static final boolean IS_OS_WINDOWS_2000 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 2000");

    /**
     * Is {@code true} if this is Windows 2003.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.1
     */
    public static final boolean IS_OS_WINDOWS_2003 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 2003");

    /**
     * Is {@code true} if this is Windows Server 2008.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.1
     */
    public static final boolean IS_OS_WINDOWS_2008 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " Server 2008");

    /**
     * Is {@code true} if this is Windows Server 2012.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.4
     */
    public static final boolean IS_OS_WINDOWS_2012 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " Server 2012");

    /**
     * Is {@code true} if this is Windows 95.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 2.0
     */
    public static final boolean IS_OS_WINDOWS_95 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 95");

    /**
     * Is {@code true} if this is Windows 98.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 2.0
     */
    public static final boolean IS_OS_WINDOWS_98 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 98");

    /**
     * Is {@code true} if this is Windows ME.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 2.0
     */
    public static final boolean IS_OS_WINDOWS_ME = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " Me");

    /**
     * Is {@code true} if this is Windows NT.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 2.0
     */
    public static final boolean IS_OS_WINDOWS_NT = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " NT");

    /**
     * Is {@code true} if this is Windows XP.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 2.0
     */
    public static final boolean IS_OS_WINDOWS_XP = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " XP");

    /**
     * Is {@code true} if this is Windows Vista.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 2.4
     */
    public static final boolean IS_OS_WINDOWS_VISTA = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " Vista");

    /**
     * Is {@code true} if this is Windows 7.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.0
     */
    public static final boolean IS_OS_WINDOWS_7 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 7");

    /**
     * Is {@code true} if this is Windows 8.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.2
     */
    public static final boolean IS_OS_WINDOWS_8 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 8");

    /**
     * Is {@code true} if this is Windows 10.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.5
     */
    public static final boolean IS_OS_WINDOWS_10 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 10");

    /**
     * Is {@code true} if this is Windows 11.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * OpenJDK fixed the return value for {@code os.name} on Windows 11 to versions 8, 11, and 17:
     * </p>
     * <ul>
     * <li>Affects Java versions 7u321, 8u311, 11.0.13-oracle, 17.0.1: https://bugs.openjdk.org/browse/JDK-8274737</li>
     * <li>Fixed in OpenJDK commit https://github.com/openjdk/jdk/commit/97ea9dd2f24f9f1fb9b9345a4202a825ee28e014</li>
     * </ul>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.13.0
     */
    public static final boolean IS_OS_WINDOWS_11 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 11");

    /**
     * Is {@code true} if this is z/OS.
     *
     * <p>
     * The field will return {@code false} if {@code OS_NAME} is {@code null}.
     * </p>
     * <p>
     * This value is initialized when the class is loaded.
     * </p>
     *
     * @since 3.5
     */
    // Values on a z/OS system I tested (Gary Gregory - 2016-03-12)
    // os.arch = s390x
    // os.encoding = ISO8859_1
    // os.name = z/OS
    // os.version = 02.02.00
    public static final boolean IS_OS_ZOS = getOsMatchesName("z/OS");

    /**
     * Decides if the operating system matches.
     *
     * @param osNamePrefix the prefix for the OS name
     * @param osVersionPrefix the prefix for the version
     * @return true if matches, or false if not or can't determine
     */
    private static boolean getOsMatches(final String osNamePrefix, final String osVersionPrefix) {
        return isOSMatch(OS_NAME, OS_VERSION, osNamePrefix, osVersionPrefix);
    }

    /**
     * Decides if the operating system matches.
     *
     * @param osNamePrefix the prefix for the OS name
     * @return true if matches, or false if not or can't determine
     */
    private static boolean getOsMatchesName(final String osNamePrefix) {
        return isOSNameMatch(OS_NAME, osNamePrefix);
    }

    /**
     * Decides if the operating system matches.
     * <p>
     * This method is package private instead of private to support unit test invocation.
     * </p>
     *
     * @param osName the actual OS name
     * @param osVersion the actual OS version
     * @param osNamePrefix the prefix for the expected OS name
     * @param osVersionPrefix the prefix for the expected OS version
     * @return true if matches, or false if not or can't determine
     */
    static boolean isOSMatch(final String osName, final String osVersion, final String osNamePrefix, final String osVersionPrefix) {
        if (osName == null || osVersion == null) {
            return false;
        }
        return isOSNameMatch(osName, osNamePrefix) && isOSVersionMatch(osVersion, osVersionPrefix);
    }

    /**
     * Decides if the operating system matches.
     * <p>
     * This method is package private instead of private to support unit test invocation.
     * </p>
     *
     * @param osName the actual OS name
     * @param osNamePrefix the prefix for the expected OS name
     * @return true if matches, or false if not or can't determine
     */
    static boolean isOSNameMatch(final String osName, final String osNamePrefix) {
        if (osName == null) {
            return false;
        }
        return osName.startsWith(osNamePrefix);
    }

    /**
     * Decides if the operating system version matches.
     * <p>
     * This method is package private instead of private to support unit test invocation.
     * </p>
     *
     * @param osVersion the actual OS version
     * @param osVersionPrefix the prefix for the expected OS version
     * @return true if matches, or false if not or can't determine
     */
    static boolean isOSVersionMatch(final String osVersion, final String osVersionPrefix) {
        if (StringUtils.isEmpty(osVersion)) {
            return false;
        }
        // Compare parts of the version string instead of using String.startsWith(String) because otherwise
        // osVersionPrefix 10.1 would also match osVersion 10.10
        final String[] versionPrefixParts = osVersionPrefix.split("\\.");
        final String[] versionParts = osVersion.split("\\.");
        for (int i = 0; i < Math.min(versionPrefixParts.length, versionParts.length); i++) {
            if (!versionPrefixParts[i].equals(versionParts[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * SystemUtils instances should NOT be constructed in standard programming. Instead, the class should be used as
     * {@code SystemUtils.FILE_SEPARATOR}.
     *
     * <p>
     * This constructor is public to permit tools that require a JavaBean instance to operate.
     * </p>
     */
    public SystemUtils() {
    }

}