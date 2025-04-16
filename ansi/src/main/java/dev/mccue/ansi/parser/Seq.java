package dev.mccue.ansi.parser;

import java.util.ArrayList;

public final class Seq {
    private Seq() {
    }

    // Shift and masks for sequence parameters and intermediates.
    public static final int MarkerShift = 8;
    static final int IntermedShift = 16;
    static final int CommandMask = 0xff;
    static final int HasMoreFlag = Integer.MIN_VALUE;
    static final int ParamMask = ~HasMoreFlag;
    static final int MissingParam = ParamMask;
    static final int MissingCommand = MissingParam;
    static final int MaxParam = 65535;

    // MaxParamsSize is the maximum number of parameters a sequence can have.
    static final int MaxParamsSize = 32;

    // DefaultParamValue is the default value used for missing parameters.
    static final int DefaultParamValue = 0;

    // Marker returns the marker byte of the sequence.
    // This is always gonna be one of the following '<' '=' '>' '?' and in the
    // range of 0x3C-0x3F.
    // Zero is returned if the sequence does not have a marker.
    static int Marker(int cmd) {
        return (cmd >> MarkerShift) & CommandMask;
    }

    // Intermediate returns the intermediate byte of the sequence.
    // An intermediate byte is in the range of 0x20-0x2F. This includes these
    // characters from ' ', '!', '"', '#', '$', '%', '&', ”', '(', ')', '*', '+',
    // ',', '-', '.', '/'.
    // Zero is returned if the sequence does not have an intermediate byte.
    static int Intermediate(int cmd) {
        return (cmd >> IntermedShift) & CommandMask;
    }

    // Command returns the command byte of the CSI sequence.
    static int Command(int cmd) {
        return cmd & CommandMask;
    }

    // Param returns the parameter at the given index.
    // It returns -1 if the parameter does not exist.
    static int Param(int[] params, int i) {
        if (params.length == 0 || i < 0 || i >= params.length) {
            return -1;
        }

        var p = params[i] & ParamMask;
        if (p == MissingParam) {
            return -1;
        }

        return p;
    }

    // HasMore returns true if the parameter has more sub-parameters.
    static boolean HasMore(int[] params, int i) {
        if (params.length == 0 || i >= params.length) {
            return false;
        }

        return (params[i] & HasMoreFlag) != 0;
    }

    // Subparams returns the sub-parameters of the given parameter.
    // It returns nil if the parameter does not exist.
    static int[] Subparams(int[] params, int i) {
        if (params.length == 0 || i < 0 || i >= params.length) {
            return null;
        }

        // Count the number of parameters before the given parameter index.
        int count = 0;
        int j;
        for (j = 0; j < params.length; j++) {
            if (count == i) {
                break;
            }
            if (!HasMore(params, j)) {
                count++;
            }
        }

        if (count > i || j >= params.length) {
            return null;
        }

        ArrayList<Integer> subs = new ArrayList<>();

        for (; j < params.length; j++) {
            if (!HasMore(params, j)) {
                break;
            }
            var p = Param(params, j);
            if (p == -1) {
                p = DefaultParamValue;
            }
            subs.add(p);
        }

        var p = Param(params, j);
        if (p == -1) {
            p = DefaultParamValue;
        }

        subs.add(p);

        int[] ret = new int[subs.size()];
        for (int idx = 0; idx < ret.length; idx++) {
            ret[idx] = subs.get(idx);
        }
        return ret;
    }

    // Len returns the number of parameters in the sequence.
    // This will return the number of parameters in the sequence, excluding any
    // sub-parameters.
    static int Len(int[] params) {
        var n = 0;
        for (int i = 0; i < params.length; i++) {
            if (!HasMore(params, i)) {
                n++;
            }
        }
        return n;
    }

    // Range iterates over the parameters of the sequence and calls the given
    // function for each parameter.
    // The function should return false to stop the iteration.
    interface RangeCallback<E extends Exception> {
        boolean test(int i, int param, boolean hasMore) throws E;
    }

    static <E extends Exception> void Range(int[] params, RangeCallback<E> fn) throws E {
        for (int i = 0; i < params.length; i++) {
            if (!fn.test(i, Param(params, i), HasMore(params, i))) {
                break;
            }
        }
    }
}