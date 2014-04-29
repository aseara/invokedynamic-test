/*
 * Copyright (c) 2010, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package indify;

import java.lang.invoke.*;
import java.util.*;
import static java.lang.invoke.MethodType.*;
import static java.lang.invoke.MethodHandles.*;

/* example class file to transform with indify */
class Example {
    private static final boolean VERBOSE
        = Boolean.getBoolean("indify.Example.VERBOSE");
    private static final boolean ALLOW_UNTRANSFORMED
        = Boolean.getBoolean("indify.Example.ALLOW_UNTRANSFORMED");
    private static void verbose(Object x) {
        if (!VERBOSE)  return;
        System.out.println(x);
    }
    private static void verboseUntransformed(Object x) {
        // calls to this guy should be transformed away
        if (x == null && ALLOW_UNTRANSFORMED)  return;
        x = "[UNTRANSFORMED] " + (x == null ? "call to indified method": x);
        if (!ALLOW_UNTRANSFORMED)  throw new RuntimeException(x.toString());
        verbose(x);
    }
    @SuppressWarnings("unused")
    public static Integer adder(Integer x, Integer y) { return x + y; }
    @SuppressWarnings("unused")
    public static Object mybsm(Object x, Object y, Object z) throws Throwable {
        verbose("mybsm"+Arrays.asList(x,y,z));
        MethodHandle mh = MH_adder();
        if (!z.equals(mh.type())) {
            verbose("spreading "+mh+" to "+z);
            mh = MH_adder().asSpreader(Object[].class, 2).asType(methodType(Object.class, Object[].class));
        }
        verbose("mh="+mh);
        return new ConstantCallSite(mh);
    }

    // exercise:
    public static void main(String... av) throws Throwable {
        System.out.println("MT = "+MT_gen1());
        System.out.println("MH = "+MH_adder());
        System.out.println("adder(1,2) = "+MH_adder().invokeWithArguments(1, 2));
        System.out.print("calling indy: ");
        if (true)
            System.out.println((Integer) INDY_tester().invokeExact((Integer)40,(Integer)2));
        else
            System.out.println(INDY_tester().invokeExact(new Object[]{40,2}));
    }

    // declarations:
    private static MethodType MT_gen1() {
        verboseUntransformed(null);
        return methodType(Object.class, Object.class);
    }
    private static MethodType MT_gen3() {
        // fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", null);
        verboseUntransformed(null);
        return methodType(Object.class, Object.class, Object.class, Object.class);
    }
    private static MethodHandle MH_adder() throws ReflectiveOperationException {
        verboseUntransformed(null);
        return lookup().findStatic(Example.class, "adder", methodType(Integer.class, Integer.class, Integer.class));
    }
    private static MethodHandle MH_mybsm() throws ReflectiveOperationException {
        verboseUntransformed(null);
        return lookup().findStatic(Example.class, "mybsm", MT_gen3());
    }
    private static MethodHandle INDY_tester;
    private static MethodHandle INDY_tester() throws Throwable {
        if (INDY_tester != null)  return INDY_tester;
        CallSite cs = (CallSite) MH_mybsm().invokeWithArguments(lookup(), "myindyname",
                fromMethodDescriptorString("(Ljava/lang/Integer;Ljava/lang/Integer;)Ljava/lang/Integer;", null));
        verboseUntransformed("BSM produces CS: "+cs);
        return cs.dynamicInvoker();
    }
}
