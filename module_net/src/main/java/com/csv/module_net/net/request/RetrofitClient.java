package com.csv.module_net.net.request;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.csv.module_net.net.cookie.CookieJarImpl;
import com.csv.module_net.net.cookie.store.PersistentCookieStore;
import com.csv.module_net.net.factory.ConverterFactoryPro;
import com.csv.module_net.net.factory.StringConverterFactory;

import java.math.BigInteger;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import static com.csv.module_net.holobase.Consts.serverPath;

/**
 * @author CSV
 * @describe: 网络请求基类
 * @date: 2021/2/23
 */
public class RetrofitClient {
    public static final String PUB_KEY = "3082010a0282010100d52ff5dd432b3a05113ec1a7065fa5a80308810e4e181cf14f7598c8d553cccb7d5111fdcdb55f6ee84fc92cd594adc1245a9c4cd41cbe407a919c5b4d4a37a012f8834df8cfe947c490464602fc05c18960374198336ba1c2e56d2e984bdfb8683610520e417a1a9a5053a10457355cf45878612f04bb134e3d670cf96c6e598fd0c693308fe3d084a0a91692bbd9722f05852f507d910b782db4ab13a92a7df814ee4304dccdad1b766bb671b6f8de578b7f27e76a2000d8d9e6b429d4fef8ffaa4e8037e167a2ce48752f1435f08923ed7e2dafef52ff30fef9ab66fdb556a82b257443ba30a93fda7a0af20418aa0b45403a2f829ea6e4b8ddbb9987f1bf0203010001";
    private static Context mContext;
    private Retrofit retrofit;
    private OkHttpClient okHttpClient;

    public static synchronized RetrofitClient getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        return new RetrofitClient();
    }

    public RetrofitClient() {
        createRetrofit();
    }

    private void createRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        //不带证书校验的SSL
        X509TrustManager x509TrustManager = new X509TrustManager() {
            @SuppressLint("TrustAllX509TrustManager")
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
                try {
                    if (chain == null) {
                        throw new IllegalArgumentException("checkServerTrusted:x509Certificate array isnull");
                    }

                    if (chain.length <= 0) {
                        throw new IllegalArgumentException("checkServerTrusted: X509Certificate is empty");
                    }

                    if (!(null != authType && authType.equalsIgnoreCase("ECDHE_RSA"))) {
                        throw new CertificateException("checkServerTrusted: AuthType is not RSA");
                    }

                    // Perform customary SSL/TLS checks(执行常规的SSL/TLS检查)
                    try {
                        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
                        tmf.init((KeyStore) null);
                        for (TrustManager trustManager : tmf.getTrustManagers()) {
                            ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
                        }
                    } catch (Exception e) {
                        throw new CertificateException(e);
                    }

                    // Hack ahead: BigInteger and toString(). We know a DER encoded Public Key begins
                    // with 0×30 (ASN.1 SEQUENCE and CONSTRUCTED), so there is no leading 0×00 to drop.
                    RSAPublicKey rsaPublicKey = (RSAPublicKey) chain[0].getPublicKey();

                    String encoded = new BigInteger(1, rsaPublicKey.getEncoded()).toString(16);
                    // Pin it!
                    final boolean expected = PUB_KEY.equalsIgnoreCase(encoded);
                    if (!expected) {
                        throw new CertificateException("checkServerTrusted: Expected public key: "
                                + PUB_KEY + ", got public key:" + encoded);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        //???????
        SSLSocketFactory sslSocketFactory = null;
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //???????
        assert sslSocketFactory != null;
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext)))
                .sslSocketFactory(sslSocketFactory, x509TrustManager)
                .hostnameVerifier(new HostnameVerifier() {
                    //设置用于确认响应证书应用于HTTPS连接请求的主机名的验证器
                    @SuppressLint("BadHostnameVerifier")
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        if (TextUtils.isEmpty(hostname)) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                })
                .build();

        //带证书验证的SSL)RxJavaCallAdapterFactory
//        SSLSocketFactory sSLSocketFactory = null;
//        TrustManagerFactory tmf;
//        TrustManager[] trustManagers = null;
//        // 取到证书的输入流
//        InputStream caInput = mContext.getResources().openRawResource(R.raw.server_cert);
//        try {
//            Certificate ca = CertificateFactory.getInstance("X.509").generateCertificate(caInput);
//            // 创建 Keystore 包含我们的证书
//            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            keyStore.load(null, null);
//            keyStore.setCertificateEntry("ca", ca);
//
//            // 创建一个 TrustManager 仅把 Keystore 中的证书 作为信任的锚点
//            tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            tmf.init(keyStore);
//            trustManagers = tmf.getTrustManagers();
//
//            // 用 TrustManager 初始化一个 SSLContext
//            SSLContext sc = SSLContext.getInstance("TLS");
//            sc.init(null, tmf.getTrustManagers(),
//                    new SecureRandom());
//            sSLSocketFactory = sc.getSocketFactory();
//
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//        try {
//            caInput.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        assert sSLSocketFactory != null;
//        okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(interceptor)
//                .cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext)))
//                .sslSocketFactory(sSLSocketFactory, (X509TrustManager) trustManagers[0])
//                .hostnameVerifier(new HostnameVerifier() {
//                    @SuppressLint("BadHostnameVerifier")
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        if (TextUtils.isEmpty(hostname)) {
//                            return false;
//                        }
//                        return serverPath.contains(hostname);
//                    }
//                })
//                .build();

        //网络框架服务器地址
        retrofit = new Retrofit.Builder()
                .baseUrl(serverPath)
                .client(okHttpClient)
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(ConverterFactoryPro.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<?> clazz) {
        return (T) retrofit.create(clazz);
    }
}
