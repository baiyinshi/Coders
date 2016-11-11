package com.bys.coder.data.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.bys.coder.common.baseapp.BaseApplication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;


class OkHttpUtils {
    private final static File RESPONSE_CACHE = NetUtils.netConfig.RESPONSE_CACHE;
    private final static int RESPONSE_CACHE_SIZE = NetUtils.netConfig.RESPONSE_CACHE_SIZE;
    private final static int HTTP_CONNECT_TIMEOUT = NetUtils.netConfig.HTTP_CONNECT_TIMEOUT;
    private final static int HTTP_READ_TIMEOUT = NetUtils.netConfig.HTTP_READ_TIMEOUT;
    private final static int MAX_CACHE_AGE = NetUtils.netConfig.MAX_CACHE_AGE;
    private static OkHttpClient singleton;

    static OkHttpClient getInstance(final Context context) {
        if (singleton == null) {
            synchronized (OkHttpUtils.class) {
                if (singleton == null) {

                    singleton = new OkHttpClient().newBuilder()
                            .cache(new Cache(RESPONSE_CACHE != null ? RESPONSE_CACHE : context.getCacheDir(), RESPONSE_CACHE_SIZE))
                            .connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                            .readTimeout(HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS)
                            .addInterceptor(new LoggingInterceptor())
                            .addNetworkInterceptor(new HeaderInterceptor())
                            .build();
                }
            }
        }
        return singleton;
    }


    /**
     * in ok3 unUseless ..
     */
    private static class CacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request.Builder builder = chain.request().newBuilder();

            builder.cacheControl(CacheControl.FORCE_NETWORK);

            return chain.proceed(builder.build());
        }
    }

    /**
     * 添加证书
     */
    private static class MySSLSocketFactory {
        public static SSLSocketFactory getSocketFactory(List<InputStream> certificates) {
            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null);
                int index = 0;
                for (InputStream certificate : certificates) {
                    String certificateAlias = Integer.toString(index++);
                    keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                    if (certificate != null) {
                        certificate.close();
                    }
                }
                SSLContext sslContext = SSLContext.getInstance("TLS");
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);
                sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
                return sslContext.getSocketFactory();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private static class HeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetWorkUtils.isNetConnected(BaseApplication.getAppContext())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetWorkUtils.isNetConnected(BaseApplication.getAppContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", String.format("max-age=%d", MAX_CACHE_AGE))
                        .removeHeader("Pragma")
                        .build();
            }
        }
    }

    /**
     * Log打印
     */
    private static class LoggingInterceptor implements Interceptor {

        private String bodyToString(final Request request) {
            if (request.body().contentType().type().equals("multipart")) {
                return "上传文件";
            } else {
                try {
                    final Request copy = request.newBuilder().build();
                    final Buffer buffer = new Buffer();
                    copy.body().writeTo(buffer);
                    return buffer.readUtf8();
                } catch (final IOException e) {
                    return "did not work";
                }
            }
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            if (NetUtils.netConfig.LOG) {
                long t1 = System.nanoTime();
                String TAG = NetUtils.netConfig.LOG_TAG;
                Request request = chain.request();
                String param = "post".equalsIgnoreCase(request.method()) ? "---REQ:" + "\n" + "       " + bodyToString(request) + "\n" : "";
                String beautyPrint;
                Response response;
                try {
                    response = chain.proceed(request);
                } catch (IOException e) {
                    beautyPrint = "--------------REQUEST START1------------" + "\n"
                            + String.format("---URL:%s %s Access Error", request.url(), request.method()) + "\n"
                            + param
                            + String.format("---Res:%s", !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : e.getClass().getSimpleName()) + "\n";
                    Log.e(TAG, beautyPrint);
                    Log.e(TAG, "--------------REQUEST END--------------");
                    throw new IOException(e);
                }
                String bodyString = response.body().string();
                long t2 = System.nanoTime();
                if (bodyString.startsWith("{") || bodyString.startsWith("[")) {
                    beautyPrint = "--------------REQUEST START2------------" + "\n"
                            + String.format("---URL:%s %s in %.1fms", request.url(), request.method(), (t2 - t1) / 1e6d) + "\n"
                            + param
                            + String.format("---RES:%s %d %s", response.protocol().toString(), response.code(), response.message()) + "\n";
                    Log.d(TAG, beautyPrint);
                    if (NetUtils.netConfig.PRINT_BODY)
                        JsonPrinter.json(bodyString);
                    Log.d(TAG, "--------------REQUEST END--------------");
                    return response.newBuilder()
                            .body(ResponseBody.create(response.body().contentType(), bodyString))
                            .build();
                } else {
                    beautyPrint = "--------------REQUEST START3------------" + "\n"
                            + String.format("---URL:%s %s in %.1fms", request.url(), request.method(), (t2 - t1) / 1e6d) + "\n"
                            + param
                            + String.format("---RES:%s %d %s", response.protocol().toString(), response.code(), response.message()) + "\n"
                            + "--------------REQUEST END--------------";
                    Log.d(TAG, beautyPrint);
                    return response.newBuilder()
                            .body(ResponseBody.create(response.body().contentType(), bodyString))
                            .build();
                }
            }
            return chain.proceed(chain.request().newBuilder().build());
        }
    }
}
