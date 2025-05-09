// Generated by jextract

package dev.weisz.boba.c.windows.x64;

import java.lang.invoke.*;
import java.lang.foreign.*;
import java.nio.ByteOrder;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static java.lang.foreign.ValueLayout.*;
import static java.lang.foreign.MemoryLayout.PathElement.*;

public class windows_wrapper_h {

    windows_wrapper_h() {
        // Should not be called directly
    }

    static final Arena LIBRARY_ARENA = Arena.ofAuto();
    static final boolean TRACE_DOWNCALLS = Boolean.getBoolean("jextract.trace.downcalls");

    static void traceDowncall(String name, Object... args) {
         String traceArgs = Arrays.stream(args)
                       .map(Object::toString)
                       .collect(Collectors.joining(", "));
         System.out.printf("%s(%s)\n", name, traceArgs);
    }

    static MemorySegment findOrThrow(String symbol) {
        return SYMBOL_LOOKUP.find(symbol)
            .orElseThrow(() -> new UnsatisfiedLinkError("unresolved symbol: " + symbol));
    }

    static MethodHandle upcallHandle(Class<?> fi, String name, FunctionDescriptor fdesc) {
        try {
            return MethodHandles.lookup().findVirtual(fi, name, fdesc.toMethodType());
        } catch (ReflectiveOperationException ex) {
            throw new AssertionError(ex);
        }
    }

    static MemoryLayout align(MemoryLayout layout, long align) {
        return switch (layout) {
            case PaddingLayout p -> p;
            case ValueLayout v -> v.withByteAlignment(align);
            case GroupLayout g -> {
                MemoryLayout[] alignedMembers = g.memberLayouts().stream()
                        .map(m -> align(m, align)).toArray(MemoryLayout[]::new);
                yield g instanceof StructLayout ?
                        MemoryLayout.structLayout(alignedMembers) : MemoryLayout.unionLayout(alignedMembers);
            }
            case SequenceLayout s -> MemoryLayout.sequenceLayout(s.elementCount(), align(s.elementLayout(), align));
        };
    }

    static final SymbolLookup SYMBOL_LOOKUP = SymbolLookup.loaderLookup()
            .or(Linker.nativeLinker().defaultLookup());

    public static final ValueLayout.OfBoolean C_BOOL = ValueLayout.JAVA_BOOLEAN;
    public static final ValueLayout.OfByte C_CHAR = ValueLayout.JAVA_BYTE;
    public static final ValueLayout.OfShort C_SHORT = ValueLayout.JAVA_SHORT;
    public static final ValueLayout.OfInt C_INT = ValueLayout.JAVA_INT;
    public static final ValueLayout.OfLong C_LONG_LONG = ValueLayout.JAVA_LONG;
    public static final ValueLayout.OfFloat C_FLOAT = ValueLayout.JAVA_FLOAT;
    public static final ValueLayout.OfDouble C_DOUBLE = ValueLayout.JAVA_DOUBLE;
    public static final AddressLayout C_POINTER = ValueLayout.ADDRESS
            .withTargetLayout(MemoryLayout.sequenceLayout(java.lang.Long.MAX_VALUE, JAVA_BYTE));
    public static final ValueLayout.OfInt C_LONG = ValueLayout.JAVA_INT;
    public static final ValueLayout.OfDouble C_LONG_DOUBLE = ValueLayout.JAVA_DOUBLE;
    private static final int DUPLICATE_SAME_ACCESS = (int)2L;
    /**
     * {@snippet lang=c :
     * #define DUPLICATE_SAME_ACCESS 2
     * }
     */
    public static int DUPLICATE_SAME_ACCESS() {
        return DUPLICATE_SAME_ACCESS;
    }
    private static final int ENABLE_PROCESSED_INPUT = (int)1L;
    /**
     * {@snippet lang=c :
     * #define ENABLE_PROCESSED_INPUT 1
     * }
     */
    public static int ENABLE_PROCESSED_INPUT() {
        return ENABLE_PROCESSED_INPUT;
    }
    private static final int ENABLE_LINE_INPUT = (int)2L;
    /**
     * {@snippet lang=c :
     * #define ENABLE_LINE_INPUT 2
     * }
     */
    public static int ENABLE_LINE_INPUT() {
        return ENABLE_LINE_INPUT;
    }
    private static final int ENABLE_ECHO_INPUT = (int)4L;
    /**
     * {@snippet lang=c :
     * #define ENABLE_ECHO_INPUT 4
     * }
     */
    public static int ENABLE_ECHO_INPUT() {
        return ENABLE_ECHO_INPUT;
    }
    private static final int ENABLE_PROCESSED_OUTPUT = (int)1L;
    /**
     * {@snippet lang=c :
     * #define ENABLE_PROCESSED_OUTPUT 1
     * }
     */
    public static int ENABLE_PROCESSED_OUTPUT() {
        return ENABLE_PROCESSED_OUTPUT;
    }
    /**
     * {@snippet lang=c :
     * typedef void *HANDLE
     * }
     */
    public static final AddressLayout HANDLE = windows_wrapper_h.C_POINTER;

    private static class GetStdHandle {
        public static final FunctionDescriptor DESC = FunctionDescriptor.of(
            windows_wrapper_h.C_POINTER,
            windows_wrapper_h.C_LONG
        );

        public static final MemorySegment ADDR = windows_wrapper_h.findOrThrow("GetStdHandle");

        public static final MethodHandle HANDLE = Linker.nativeLinker().downcallHandle(ADDR, DESC);
    }

    /**
     * Function descriptor for:
     * {@snippet lang=c :
     * HANDLE GetStdHandle(DWORD nStdHandle)
     * }
     */
    public static FunctionDescriptor GetStdHandle$descriptor() {
        return GetStdHandle.DESC;
    }

    /**
     * Downcall method handle for:
     * {@snippet lang=c :
     * HANDLE GetStdHandle(DWORD nStdHandle)
     * }
     */
    public static MethodHandle GetStdHandle$handle() {
        return GetStdHandle.HANDLE;
    }

    /**
     * Address for:
     * {@snippet lang=c :
     * HANDLE GetStdHandle(DWORD nStdHandle)
     * }
     */
    public static MemorySegment GetStdHandle$address() {
        return GetStdHandle.ADDR;
    }

    /**
     * {@snippet lang=c :
     * HANDLE GetStdHandle(DWORD nStdHandle)
     * }
     */
    public static MemorySegment GetStdHandle(int nStdHandle) {
        var mh$ = GetStdHandle.HANDLE;
        try {
            if (TRACE_DOWNCALLS) {
                traceDowncall("GetStdHandle", nStdHandle);
            }
            return (MemorySegment)mh$.invokeExact(nStdHandle);
        } catch (Throwable ex$) {
           throw new AssertionError("should not reach here", ex$);
        }
    }

    private static class DuplicateHandle {
        public static final FunctionDescriptor DESC = FunctionDescriptor.of(
            windows_wrapper_h.C_INT,
            windows_wrapper_h.C_POINTER,
            windows_wrapper_h.C_POINTER,
            windows_wrapper_h.C_POINTER,
            windows_wrapper_h.C_POINTER,
            windows_wrapper_h.C_LONG,
            windows_wrapper_h.C_INT,
            windows_wrapper_h.C_LONG
        );

        public static final MemorySegment ADDR = windows_wrapper_h.findOrThrow("DuplicateHandle");

        public static final MethodHandle HANDLE = Linker.nativeLinker().downcallHandle(ADDR, DESC);
    }

    /**
     * Function descriptor for:
     * {@snippet lang=c :
     * BOOL DuplicateHandle(HANDLE hSourceProcessHandle, HANDLE hSourceHandle, HANDLE hTargetProcessHandle, LPHANDLE lpTargetHandle, DWORD dwDesiredAccess, BOOL bInheritHandle, DWORD dwOptions)
     * }
     */
    public static FunctionDescriptor DuplicateHandle$descriptor() {
        return DuplicateHandle.DESC;
    }

    /**
     * Downcall method handle for:
     * {@snippet lang=c :
     * BOOL DuplicateHandle(HANDLE hSourceProcessHandle, HANDLE hSourceHandle, HANDLE hTargetProcessHandle, LPHANDLE lpTargetHandle, DWORD dwDesiredAccess, BOOL bInheritHandle, DWORD dwOptions)
     * }
     */
    public static MethodHandle DuplicateHandle$handle() {
        return DuplicateHandle.HANDLE;
    }

    /**
     * Address for:
     * {@snippet lang=c :
     * BOOL DuplicateHandle(HANDLE hSourceProcessHandle, HANDLE hSourceHandle, HANDLE hTargetProcessHandle, LPHANDLE lpTargetHandle, DWORD dwDesiredAccess, BOOL bInheritHandle, DWORD dwOptions)
     * }
     */
    public static MemorySegment DuplicateHandle$address() {
        return DuplicateHandle.ADDR;
    }

    /**
     * {@snippet lang=c :
     * BOOL DuplicateHandle(HANDLE hSourceProcessHandle, HANDLE hSourceHandle, HANDLE hTargetProcessHandle, LPHANDLE lpTargetHandle, DWORD dwDesiredAccess, BOOL bInheritHandle, DWORD dwOptions)
     * }
     */
    public static int DuplicateHandle(MemorySegment hSourceProcessHandle, MemorySegment hSourceHandle, MemorySegment hTargetProcessHandle, MemorySegment lpTargetHandle, int dwDesiredAccess, int bInheritHandle, int dwOptions) {
        var mh$ = DuplicateHandle.HANDLE;
        try {
            if (TRACE_DOWNCALLS) {
                traceDowncall("DuplicateHandle", hSourceProcessHandle, hSourceHandle, hTargetProcessHandle, lpTargetHandle, dwDesiredAccess, bInheritHandle, dwOptions);
            }
            return (int)mh$.invokeExact(hSourceProcessHandle, hSourceHandle, hTargetProcessHandle, lpTargetHandle, dwDesiredAccess, bInheritHandle, dwOptions);
        } catch (Throwable ex$) {
           throw new AssertionError("should not reach here", ex$);
        }
    }

    private static class GetCurrentProcess {
        public static final FunctionDescriptor DESC = FunctionDescriptor.of(
            windows_wrapper_h.C_POINTER    );

        public static final MemorySegment ADDR = windows_wrapper_h.findOrThrow("GetCurrentProcess");

        public static final MethodHandle HANDLE = Linker.nativeLinker().downcallHandle(ADDR, DESC);
    }

    /**
     * Function descriptor for:
     * {@snippet lang=c :
     * HANDLE GetCurrentProcess()
     * }
     */
    public static FunctionDescriptor GetCurrentProcess$descriptor() {
        return GetCurrentProcess.DESC;
    }

    /**
     * Downcall method handle for:
     * {@snippet lang=c :
     * HANDLE GetCurrentProcess()
     * }
     */
    public static MethodHandle GetCurrentProcess$handle() {
        return GetCurrentProcess.HANDLE;
    }

    /**
     * Address for:
     * {@snippet lang=c :
     * HANDLE GetCurrentProcess()
     * }
     */
    public static MemorySegment GetCurrentProcess$address() {
        return GetCurrentProcess.ADDR;
    }

    /**
     * {@snippet lang=c :
     * HANDLE GetCurrentProcess()
     * }
     */
    public static MemorySegment GetCurrentProcess() {
        var mh$ = GetCurrentProcess.HANDLE;
        try {
            if (TRACE_DOWNCALLS) {
                traceDowncall("GetCurrentProcess");
            }
            return (MemorySegment)mh$.invokeExact();
        } catch (Throwable ex$) {
           throw new AssertionError("should not reach here", ex$);
        }
    }

    private static class GetConsoleMode {
        public static final FunctionDescriptor DESC = FunctionDescriptor.of(
            windows_wrapper_h.C_INT,
            windows_wrapper_h.C_POINTER,
            windows_wrapper_h.C_POINTER
        );

        public static final MemorySegment ADDR = windows_wrapper_h.findOrThrow("GetConsoleMode");

        public static final MethodHandle HANDLE = Linker.nativeLinker().downcallHandle(ADDR, DESC);
    }

    /**
     * Function descriptor for:
     * {@snippet lang=c :
     * BOOL GetConsoleMode(HANDLE hConsoleHandle, LPDWORD lpMode)
     * }
     */
    public static FunctionDescriptor GetConsoleMode$descriptor() {
        return GetConsoleMode.DESC;
    }

    /**
     * Downcall method handle for:
     * {@snippet lang=c :
     * BOOL GetConsoleMode(HANDLE hConsoleHandle, LPDWORD lpMode)
     * }
     */
    public static MethodHandle GetConsoleMode$handle() {
        return GetConsoleMode.HANDLE;
    }

    /**
     * Address for:
     * {@snippet lang=c :
     * BOOL GetConsoleMode(HANDLE hConsoleHandle, LPDWORD lpMode)
     * }
     */
    public static MemorySegment GetConsoleMode$address() {
        return GetConsoleMode.ADDR;
    }

    /**
     * {@snippet lang=c :
     * BOOL GetConsoleMode(HANDLE hConsoleHandle, LPDWORD lpMode)
     * }
     */
    public static int GetConsoleMode(MemorySegment hConsoleHandle, MemorySegment lpMode) {
        var mh$ = GetConsoleMode.HANDLE;
        try {
            if (TRACE_DOWNCALLS) {
                traceDowncall("GetConsoleMode", hConsoleHandle, lpMode);
            }
            return (int)mh$.invokeExact(hConsoleHandle, lpMode);
        } catch (Throwable ex$) {
           throw new AssertionError("should not reach here", ex$);
        }
    }

    private static class SetConsoleMode {
        public static final FunctionDescriptor DESC = FunctionDescriptor.of(
            windows_wrapper_h.C_INT,
            windows_wrapper_h.C_POINTER,
            windows_wrapper_h.C_LONG
        );

        public static final MemorySegment ADDR = windows_wrapper_h.findOrThrow("SetConsoleMode");

        public static final MethodHandle HANDLE = Linker.nativeLinker().downcallHandle(ADDR, DESC);
    }

    /**
     * Function descriptor for:
     * {@snippet lang=c :
     * BOOL SetConsoleMode(HANDLE hConsoleHandle, DWORD dwMode)
     * }
     */
    public static FunctionDescriptor SetConsoleMode$descriptor() {
        return SetConsoleMode.DESC;
    }

    /**
     * Downcall method handle for:
     * {@snippet lang=c :
     * BOOL SetConsoleMode(HANDLE hConsoleHandle, DWORD dwMode)
     * }
     */
    public static MethodHandle SetConsoleMode$handle() {
        return SetConsoleMode.HANDLE;
    }

    /**
     * Address for:
     * {@snippet lang=c :
     * BOOL SetConsoleMode(HANDLE hConsoleHandle, DWORD dwMode)
     * }
     */
    public static MemorySegment SetConsoleMode$address() {
        return SetConsoleMode.ADDR;
    }

    /**
     * {@snippet lang=c :
     * BOOL SetConsoleMode(HANDLE hConsoleHandle, DWORD dwMode)
     * }
     */
    public static int SetConsoleMode(MemorySegment hConsoleHandle, int dwMode) {
        var mh$ = SetConsoleMode.HANDLE;
        try {
            if (TRACE_DOWNCALLS) {
                traceDowncall("SetConsoleMode", hConsoleHandle, dwMode);
            }
            return (int)mh$.invokeExact(hConsoleHandle, dwMode);
        } catch (Throwable ex$) {
           throw new AssertionError("should not reach here", ex$);
        }
    }

    private static class GetConsoleScreenBufferInfo {
        public static final FunctionDescriptor DESC = FunctionDescriptor.of(
            windows_wrapper_h.C_INT,
            windows_wrapper_h.C_POINTER,
            windows_wrapper_h.C_POINTER
        );

        public static final MemorySegment ADDR = windows_wrapper_h.findOrThrow("GetConsoleScreenBufferInfo");

        public static final MethodHandle HANDLE = Linker.nativeLinker().downcallHandle(ADDR, DESC);
    }

    /**
     * Function descriptor for:
     * {@snippet lang=c :
     * BOOL GetConsoleScreenBufferInfo(HANDLE hConsoleOutput, PCONSOLE_SCREEN_BUFFER_INFO lpConsoleScreenBufferInfo)
     * }
     */
    public static FunctionDescriptor GetConsoleScreenBufferInfo$descriptor() {
        return GetConsoleScreenBufferInfo.DESC;
    }

    /**
     * Downcall method handle for:
     * {@snippet lang=c :
     * BOOL GetConsoleScreenBufferInfo(HANDLE hConsoleOutput, PCONSOLE_SCREEN_BUFFER_INFO lpConsoleScreenBufferInfo)
     * }
     */
    public static MethodHandle GetConsoleScreenBufferInfo$handle() {
        return GetConsoleScreenBufferInfo.HANDLE;
    }

    /**
     * Address for:
     * {@snippet lang=c :
     * BOOL GetConsoleScreenBufferInfo(HANDLE hConsoleOutput, PCONSOLE_SCREEN_BUFFER_INFO lpConsoleScreenBufferInfo)
     * }
     */
    public static MemorySegment GetConsoleScreenBufferInfo$address() {
        return GetConsoleScreenBufferInfo.ADDR;
    }

    /**
     * {@snippet lang=c :
     * BOOL GetConsoleScreenBufferInfo(HANDLE hConsoleOutput, PCONSOLE_SCREEN_BUFFER_INFO lpConsoleScreenBufferInfo)
     * }
     */
    public static int GetConsoleScreenBufferInfo(MemorySegment hConsoleOutput, MemorySegment lpConsoleScreenBufferInfo) {
        var mh$ = GetConsoleScreenBufferInfo.HANDLE;
        try {
            if (TRACE_DOWNCALLS) {
                traceDowncall("GetConsoleScreenBufferInfo", hConsoleOutput, lpConsoleScreenBufferInfo);
            }
            return (int)mh$.invokeExact(hConsoleOutput, lpConsoleScreenBufferInfo);
        } catch (Throwable ex$) {
           throw new AssertionError("should not reach here", ex$);
        }
    }
    private static final int STD_OUTPUT_HANDLE = (int)4294967285L;
    /**
     * {@snippet lang=c :
     * #define STD_OUTPUT_HANDLE 4294967285
     * }
     */
    public static int STD_OUTPUT_HANDLE() {
        return STD_OUTPUT_HANDLE;
    }
}

