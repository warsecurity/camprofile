package com.camprofile;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook implements IXposedHookLoadPackage {
    private static final String PKG = "com.oplus.camera";
    private static XSharedPreferences prefs;

    private static String[] cur() {
        int i = 2;
        try { prefs.reload(); i = prefs.getInt("idx", 2); } catch (Throwable ignored) {}
        if (i < 0 || i >= Profiles.ALL.length) i = 2;
        return Profiles.ALL[i];
    }

    private static String watermark() {
        return cur()[0];
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lp) {
        if (!PKG.equals(lp.packageName)) return;
        prefs = new XSharedPreferences("com.camprofile", "profile");
        hookWatermark(lp.classLoader);
        hookExif(lp.classLoader);
    }

    private void hookWatermark(ClassLoader cl) {
        Class<?> u = XposedHelpers.findClassIfExists("com.oplus.camera.util.Util", cl);
        if (u == null) { XposedBridge.log("[CamProfile] Util not found"); return; }
        for (String m : new String[]{"getMarketName", "getVendorMarketName"}) {
            try {
                XposedBridge.hookAllMethods(u, m, new XC_MethodReplacement() {
                    @Override protected Object replaceHookedMethod(MethodHookParam p) { return watermark(); }
                });
            } catch (Throwable t) { XposedBridge.log("[CamProfile] hook " + m + ": " + t); }
        }
        XposedBridge.log("[CamProfile] watermark hooked");
    }

    private void hookExif(ClassLoader cl) {
        Class<?> x = XposedHelpers.findClassIfExists("com.oplus.exif.OplusExifInterface", cl);
        if (x == null) { XposedBridge.log("[CamProfile] OplusExifInterface not found"); return; }
        XposedBridge.hookAllMethods(x, "writeExif", new XC_MethodHook() {
            @Override protected void beforeHookedMethod(MethodHookParam p) {
                if (p.args.length == 0 || !(p.args[0] instanceof byte[])) return;
                String[] pr = cur();
                int n = patch((byte[]) p.args[0], pr);
                try {  // also fix the interface's own copy
                    Object mk = XposedHelpers.callMethod(p.thisObject, "buildTag", 271, pr[1]);
                    if (mk != null) XposedHelpers.callMethod(p.thisObject, "setTag", mk);
                    Object md = XposedHelpers.callMethod(p.thisObject, "buildTag", 272, pr[2]);
                    if (md != null) XposedHelpers.callMethod(p.thisObject, "setTag", md);
                    Object sw = XposedHelpers.callMethod(p.thisObject, "buildTag", 305, pr[3]);
                    if (sw != null) XposedHelpers.callMethod(p.thisObject, "setTag", sw);
                } catch (Throwable t) { XposedBridge.log("[CamProfile] setTag: " + t); }
                XposedBridge.log("[CamProfile] patched " + n + " tag(s) -> " + pr[2]);
            }
        });
        XposedBridge.log("[CamProfile] exif hooked");
    }

    private static int u16(byte[] a, int o, boolean le) {
        return le ? ((a[o] & 0xff) | ((a[o + 1] & 0xff) << 8))
                  : (((a[o] & 0xff) << 8) | (a[o + 1] & 0xff));
    }

    private static long u32(byte[] a, int o, boolean le) {
        long v = le ? ((a[o] & 0xffL) | ((a[o + 1] & 0xffL) << 8) | ((a[o + 2] & 0xffL) << 16) | ((a[o + 3] & 0xffL) << 24))
                    : (((a[o] & 0xffL) << 24) | ((a[o + 1] & 0xffL) << 16) | ((a[o + 2] & 0xffL) << 8) | (a[o + 3] & 0xffL));
        return v & 0xffffffffL;
    }

    /** Patches Make/Model/Software in IFD0 in place. Strings must fit the existing slot. */
    private static int patch(byte[] a, String[] p) {
        try {
            if (a.length < 64 || (a[0] & 0xff) != 0xff || (a[1] & 0xff) != 0xd8
                    || (a[2] & 0xff) != 0xff || (a[3] & 0xff) != 0xe1) return 0;
            final int base = 12;                       // TIFF header after "Exif\0\0"
            boolean le = (a[base] & 0xff) == 0x49;
            int ifd = base + (int) u32(a, base + 4, le);
            int n = u16(a, ifd, le), done = 0;
            for (int i = 0; i < n; i++) {
                int e = ifd + 2 + 12 * i;
                int tag = u16(a, e, le), type = u16(a, e + 2, le), cnt = (int) u32(a, e + 4, le);
                String want = tag == 0x010F ? p[1] : tag == 0x0110 ? p[2] : tag == 0x0131 ? p[3] : null;
                if (want == null || type != 2) continue;
                if (want.length() + 1 > cnt) {
                    XposedBridge.log("[CamProfile] too long for tag 0x" + Integer.toHexString(tag)
                            + " (max " + (cnt - 1) + "): " + want);
                    continue;
                }
                int off = cnt > 4 ? base + (int) u32(a, e + 8, le) : e + 8;
                for (int k = 0; k < cnt; k++) a[off + k] = (byte) (k < want.length() ? want.charAt(k) : 0);
                done++;
            }
            return done;
        } catch (Throwable t) {
            XposedBridge.log("[CamProfile] patch error: " + t);
            return 0;
        }
    }
}
