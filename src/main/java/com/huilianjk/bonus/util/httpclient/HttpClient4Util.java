package com.huilianjk.bonus.util.httpclient;

import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class HttpClient4Util {
    private static Logger logger = LoggerFactory.getLogger(HttpClient4Util.class);
    private CloseableHttpClient httpClient;
    private static HttpClient4Util httpClient4Util;
    private static HttpClient4Util httpsClient4Util;
    public static final int CONN_TIMEOUT = 70000;
    public static final int READ_TIMEOUT = 70000;

    public static HttpClient4Util getDefault() {
        if (httpClient4Util == null) {
            httpClient4Util = new HttpClient4Util();
        }
        return httpClient4Util;
    }

    public static HttpClient4Util getHttpsClient() {
        if (httpsClient4Util == null) {
            httpsClient4Util = new HttpClient4Util(true);
        }
        return httpsClient4Util;
    }

    public static CloseableHttpClient getHttpsClientNoKey() {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] arg0,
                                               String arg1) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] arg0,
                                               String arg1) throws CertificateException {
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());

            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", new PlainConnectionSocketFactory())
                    .register("https", sslsf)
                    .build();
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(200);
            cm.setDefaultMaxPerRoute(50);
            return HttpClients.custom()
                    .setConnectionManager(cm)
                    .setKeepAliveStrategy(getConnectionKeepAliveStrategy())
                    .build();
        } catch (Exception ex) {
            logger.warn("http client init", ex);
            return null;
        }
    }


    public static HttpClient4Util genHttpsClientNoHostnameVerifier() {

        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] arg0,
                                               String arg1) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] arg0,
                                               String arg1) throws CertificateException {
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", new PlainConnectionSocketFactory())
                    .register("https", sslsf)
                    .build();
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(200);
            cm.setDefaultMaxPerRoute(50);
            ConnectionKeepAliveStrategy keepAliveStrategy = getConnectionKeepAliveStrategy();

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(cm)
                    .setKeepAliveStrategy(keepAliveStrategy)
                    .build();
            return new HttpClient4Util(httpClient);
        } catch (Exception ex) {
            logger.warn("http client init", ex);
            return null;
        }
    }


    public static HttpClient4Util genHttpsClientWithKey(String keyStorePath, String keyStorePwd) {

        FileInputStream inStream = null;
        try {
            //get trustStore
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            inStream = new FileInputStream(new File(keyStorePath));
            trustStore.load(inStream, keyStorePwd.toCharArray());
            //get TrustManagerFactory
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("sunx509");
            tmf.init(trustStore);
            TrustManager[] tm = tmf.getTrustManagers();

            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, tm, null);

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());

            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", new PlainConnectionSocketFactory())
                    .register("https", sslsf)
                    .build();
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(200);
            cm.setDefaultMaxPerRoute(50);
            ConnectionKeepAliveStrategy keepAliveStrategy = getConnectionKeepAliveStrategy();

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(cm)
                    .setKeepAliveStrategy(keepAliveStrategy)
                    .build();
            return new HttpClient4Util(httpClient);
        } catch (Exception ex) {
            logger.warn("http client init", ex);
            return null;
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ConnectionKeepAliveStrategy getConnectionKeepAliveStrategy() {
        return new ConnectionKeepAliveStrategy() {
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                // Honor 'keep-alive' header
                HeaderElementIterator it = new BasicHeaderElementIterator(
                        response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                long timeOut = -1;
                while (it.hasNext()) {
                    HeaderElement he = it.nextElement();
                    String param = he.getName();
                    String value = he.getValue();

                    if (value != null && param.equalsIgnoreCase("max")) {
                        try {
                            if (Integer.parseInt(value) == 1) {
                                return -1L;
                            }
                        } catch (NumberFormatException ignore) {
                        }
                    }
                    if (value != null && param.equalsIgnoreCase("timeout")) {
                        try {
                            timeOut = Long.parseLong(value) * 1000;
                        } catch (NumberFormatException ignore) {
                        }
                    }
                }
                return timeOut;
            }
        };
    }

    public HttpClient4Util(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpClient4Util(boolean ssl) {
        if (ssl) {
            this.httpClient = HttpClient4Util.getHttpsClientNoKey();
        } else {
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(200);
            cm.setDefaultMaxPerRoute(50);
            httpClient = HttpClients.custom()
                    .setConnectionManager(cm)
                    .setKeepAliveStrategy(getConnectionKeepAliveStrategy())
                    .build();
        }
    }

    public static HttpClient4Util genHttpsClientOfTrustSelf(String keyStorePath, String keyStorePwd, String keyType) {

        FileInputStream inStream = null;
        try {
            //get trustStore
            KeyStore trustStore = KeyStore.getInstance(keyType);
            inStream = new FileInputStream(new File(keyStorePath));
            trustStore.load(inStream, keyStorePwd.toCharArray());

            //Trust own CA and all self-signed certs
            SSLContext ctx = SSLContexts.custom()
                    .loadKeyMaterial(trustStore, keyStorePwd.toCharArray())
                    .build();

            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    ctx,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());

            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", new PlainConnectionSocketFactory())
                    .register("https", sslsf)
                    .build();
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(200);
            cm.setDefaultMaxPerRoute(50);
            ConnectionKeepAliveStrategy keepAliveStrategy = getConnectionKeepAliveStrategy();

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(cm)
                    .setKeepAliveStrategy(keepAliveStrategy)
                    .build();
            return new HttpClient4Util(httpClient);
        } catch (Exception ex) {
            logger.warn("http client init", ex);
            return null;
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public HttpClient4Util() {
        this(false);
    }

    /**
     * 简化的get
     *
     * @param url
     * @return
     * @throws org.apache.http.client.ClientProtocolException
     * @throws java.io.IOException
     */
    public HttpResp doGet(String url) throws
            IOException {
        return this.doGet(url, null, null);
    }

    /**
     * 详细get
     *
     * @param url
     * @param httpParameter 参考{@link HttpParameter}
     * @param charset       参数编码
     * @return 参考{@link HttpResp}
     * @throws org.apache.http.client.ClientProtocolException
     * @throws java.io.IOException
     */
    public HttpResp doGet(String url, HttpParameter httpParameter,
                          String charset) throws IOException {
        return this.doGet(url, httpParameter, charset, READ_TIMEOUT, CONN_TIMEOUT);
    }

    /**
     * 详细get
     *
     * @param url
     * @param httpParameter  参考{@link HttpParameter}
     * @param charset        参数编码
     * @param soTimeOut      传输超时
     * @param connectTimeOut 连接超时
     * @return 参考{@link HttpResp}
     * @throws org.apache.http.client.ClientProtocolException
     * @throws java.io.IOException
     */
    public HttpResp doGet(String url, HttpParameter httpParameter,
                          String charset, int soTimeOut, int connectTimeOut) throws IOException {
        StringBuilder sb = new StringBuilder(url);
        if (httpParameter != null && !httpParameter.isAllParameterEmpty()) {
            if (url.indexOf("?") == -1) {
                sb.append("?");
            }
            if (sb.charAt(sb.length() - 1) != ('?')) {
                sb.append("&");
            }
            for (BasicParameter o : httpParameter.getBasicParameters()) {
                sb.append(URLEncoder.encode(o.getName(), charset));
                sb.append("=");
                sb.append(URLEncoder.encode(o.getValue(), charset));
                sb.append("&");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        HttpGet httpGet = new HttpGet(sb.toString());
        if (httpParameter != null && !httpParameter.isEmptyHeader()) {
            Set<Entry<String, String>> set = httpParameter.getHeaderMap()
                    .entrySet();
            for (Entry<String, String> e : set) {
                httpGet.addHeader(e.getKey(), e.getValue());
            }
        }
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(soTimeOut).setConnectTimeout(connectTimeOut).build();//设置请求和传输超时时间
        httpGet.setConfig(requestConfig);
        return this.execute(httpGet);
    }

    /**
     * post 字符数据
     *
     * @param url     post的字符串数据
     * @param charset 参数编码
     * @return
     * @throws org.apache.http.client.ClientProtocolException
     * @throws java.io.IOException
     */
    public HttpResp doPostStringBody(String url, String string, String charset)
            throws IOException {
        // string="{\"action\":\"Balance\",\"account\":\"MBDOYMED\",\"timeStamp\":\"1497845206\",\"sign\":\"b19cca304eb98fa2dba53626594850bb\"}";
        HttpEntity entity = new StringEntity(string, charset);
        return this.doPostBody(url, entity);
    }

    /**
     * @param url        路由
     * @param string     字符串数据
     * @param HeaderName header字段名
     * @param HeaderVal  header字段值
     * @param charset    参数编码
     * @return
     * @throws IOException
     */
    public HttpResp doPostStringBodyWithHeader(String url, String string, String HeaderName, String HeaderVal, String charset)
            throws IOException {
        HttpEntity entity = new StringEntity(string, charset);
        return this.doPostBodyWithHeader(url, HeaderName, HeaderVal, entity);
    }

    public HttpResp doPostBody(String url, HttpEntity entity)
            throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(READ_TIMEOUT).setConnectTimeout(CONN_TIMEOUT).build();//设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        return this.execute(httpPost);
    }

    private HttpResp doPostBodyWithHeader(String url, String HeaderName, String HeaderVal, HttpEntity entity)
            throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        httpPost.addHeader(HeaderName, HeaderVal);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(READ_TIMEOUT).setConnectTimeout(CONN_TIMEOUT).build();//设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        return this.execute(httpPost);
    }

    /**
     * post
     *
     * @param url
     * @param httpParameter 参数信息 参考{@link HttpParameter}
     * @param charset       参数编码
     * @return 参考{@link HttpResp}
     * @throws org.apache.http.client.ClientProtocolException
     * @throws java.io.IOException
     */
    private HttpPost getHttpPost(String url, HttpParameter httpParameter,
                                 String charset) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        if (httpParameter.isFileParameterEmpty()) {
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            for (BasicParameter e : httpParameter.getBasicParameters()) {
                nameValuePairs.add(new BasicNameValuePair(e.getName(), e
                        .getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, charset));
        } else {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            for (FileParameter e : httpParameter.getFileParameters()) {
                if (e.getFile() != null) {
                    builder.addPart(e.getName(), new FileBody(e.getFile()));
                } else {
                    builder.addPart(e.getName(),
                            new ByteArrayBody(e.getData(), e.getFileName()));
                }
            }
            for (BasicParameter e : httpParameter.getBasicParameters()) {
                builder.addPart(e.getName(), new StringBody(e.getValue(), ContentType.create("text/plain", Charset.forName(charset))));
            }
            httpPost.setEntity(builder.build());
        }
        if (!httpParameter.isEmptyHeader()) {
            Set<Entry<String, String>> set = httpParameter.getHeaderMap()
                    .entrySet();
            for (Entry<String, String> e : set) {
                httpPost.addHeader(e.getKey(), e.getValue());
            }
        }
        return httpPost;
    }


    /**
     * post
     *
     * @param url
     * @param httpParameter 参数信息 参考{@link HttpParameter}
     * @param charset       参数编码
     * @return 参考{@link HttpResp}
     * @throws org.apache.http.client.ClientProtocolException
     * @throws java.io.IOException
     */
    public HttpResp doPost(String url, HttpParameter httpParameter,
                           String charset) throws IOException {
        return doPost(url, httpParameter, charset, CONN_TIMEOUT);
    }

    public HttpResp doPost(String url, HttpParameter httpParameter,
                           String charset, int connectTimeOut) throws IOException {
        return doPost(url, httpParameter, charset, connectTimeOut, READ_TIMEOUT);
    }

    public HttpResp doPost(String url, HttpParameter httpParameter,
                           String charset, int connectTimeOut, int soTimeOut) throws IOException {
        HttpPost httpPost = this.getHttpPost(url, httpParameter, charset);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(soTimeOut).setConnectTimeout(connectTimeOut).build();//设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        return this.execute(httpPost);
    }

    /**
     * 具体执行过程
     *
     * @param request
     * @return
     * @throws org.apache.http.client.ClientProtocolException
     * @throws java.io.IOException
     */
    private HttpResp execute(HttpRequestBase request)
            throws IOException {
        HttpEntity entity = null;
        try {
            HttpResponse httpResponse = httpClient.execute(request);
            HttpResp httpResp = new HttpResp();
            httpResp.setStatusCode(httpResponse.getStatusLine().getStatusCode());
            entity = httpResponse.getEntity();
            httpResp.setBytes(EntityUtils.toByteArray(entity));
            return httpResp;
        } catch (IOException e) {
            throw e;
        } finally {
            EntityUtils.consume(entity);
            logger.debug("consume entity");
            request.abort();
        }
    }

    /**
     * 最终可释放资源调用
     */
    public void shutdown() throws IOException {
        this.httpClient.close();
    }
}