package xyz.yuanjin.project.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yuanjin.project.common.config.MyHostnameVerifier;
import xyz.yuanjin.project.common.config.MyX509TrustManager;
import xyz.yuanjin.project.common.consts.Const;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yuanjin
 */
@SuppressWarnings("unused")
public class HttpUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    private static MyX509TrustManager myX509TrustManager = new MyX509TrustManager();
    private static MyHostnameVerifier myHostnameVerifier = new MyHostnameVerifier();


    public static String post(String url, Map<String, String> params) throws IOException {
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> pairs = new ArrayList<>();
        params.forEach((key, value) -> pairs.add(new BasicNameValuePair(key, value)));

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        httpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));

        CloseableHttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity httpEntity = response.getEntity();
            return EntityUtils.toString(httpEntity);
        }
        return null;
    }

    /**
     * 支持 http(s) 的GET、POST请求
     *
     * @param urlString url
     * @param method    GET/POST
     * @param params    参数（可为null）
     * @return 返回字符串
     */
    public static String request(String urlString, String method, Map<String, String> params) {

        try {
            String body = formatParamMap(params);

            return request(urlString, method, body, null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 支持 http(s) 的GET、POST请求
     *
     * @param urlString url
     * @param method    GET/POST
     * @param params    参数（可为null）
     * @return 返回字符串
     */
    public static String request(String urlString, String method, Map<String, String> params, Map<String, String> headerMap) {

        try {
            String body = formatParamMap(params);

            return request(urlString, method, body, headerMap);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 支持 http(s) 的GET、POST请求
     *
     * @param urlString url
     * @param method    GET/POST
     * @param params    参数（可为null）
     * @return 返回字符串
     */
    public static String request(String urlString, String method, Map<String, String> params, Map<String, String> headerMap, String readCharset) {

        try {
            String body = formatParamMap(params);

            return request(urlString, method, body, headerMap, readCharset);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 支持 http(s) 的GET、POST请求
     *
     * @param urlString url
     * @param method    GET/POST
     * @param body      参数（可为null）
     * @return 返回字符串
     */
    public static String request(String urlString, String method, String body) {
        return request(urlString, method, body, null);
    }

    /**
     * 支持 http(s) 的GET、POST请求
     *
     * @param urlString url
     * @param method    GET/POST
     * @param body      参数（可为null）
     * @return 返回字符串
     */
    public static String request(String urlString, String method, String body, Map<String, String> headerMap) {
        return request(urlString, method, body, headerMap, StandardCharsets.UTF_8.name());
    }

    /**
     * 支持 http(s) 的GET、POST请求
     *
     * @param urlString url
     * @param method    GET/POST
     * @param body      参数（可为null）
     * @return 返回字符串
     */
    public static String request(String urlString, String method, String body, Map<String, String> headerMap, String readCharset) {

        try {

            URL url;
            if (Const.METHOD_GET.equals(method) && body != null) {
                url = new URL(urlString.concat("?").concat(body));
            } else {
                url = new URL(urlString);
            }

            URLConnection conn = getURLConnection(url, method, headerMap);

            if (body != null) {
                if (Const.METHOD_POST.equals(method)) {
                    OutputStream os = conn.getOutputStream();
                    os.write(body.getBytes(StandardCharsets.UTF_8));
                }
            }

            InputStream is = conn.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] bytes = new byte[1024];
            int length;
            while ((length = is.read(bytes)) != -1) {
                bos.write(bytes, 0, length);
            }

            return bos.toString(readCharset);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    private static String formatParamMap(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.name()))
                        .append("&");
            }
        }

        if (sb.length() > 0) {
            return sb.substring(0, sb.length() - 1);
        }

        return null;
    }

    private static URLConnection getURLConnection(URL url, String method) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        return getURLConnection(url, method, null);
    }

    private static URLConnection getURLConnection(URL url, String method, Map<String, String> headerMap) throws IOException, NoSuchAlgorithmException, KeyManagementException {

        URLConnection conn = url.openConnection();

        if (Const.HTTPS.equals(url.getProtocol())) {
            HttpsURLConnection connHttps = (HttpsURLConnection) conn;
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManager[] trustAllCerts = new TrustManager[]{myX509TrustManager};
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            connHttps.setDoOutput(true);
            connHttps.setDoInput(true);
            connHttps.setRequestMethod(method);
            connHttps.setSSLSocketFactory(ssf);
            connHttps.setHostnameVerifier(myHostnameVerifier);
        } else {
            HttpURLConnection connHttp = (HttpURLConnection) conn;
            connHttp.setDoOutput(true);
            connHttp.setDoInput(true);
            connHttp.setRequestMethod(method);
        }

        if (CollectionUtil.isNotEmpty(headerMap)) {
            headerMap.forEach(conn::setRequestProperty);
        }

        conn.connect();
        return conn;
    }

    /**
     * 上传文件
     *
     * @param actionUrl 上传的url
     * @param fileMap   文件
     * @return 上传结果
     * @throws IOException 异常
     */
    public static String upLoadFilePost(String actionUrl, Map<String, File> fileMap) throws IOException {
        String boundary = java.util.UUID.randomUUID().toString();
        String prefix = "--", lineEnd = "\r\n";
        String charset = "UTF-8";
        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(5 * 1000);
        // 允许输入
        conn.setDoInput(true);
        // 允许输出
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        // Post方式
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

        DataOutputStream outStream = new DataOutputStream(
                conn.getOutputStream());
        // 发送文件数据
        if (fileMap != null) {
            for (Map.Entry<String, File> file : fileMap.entrySet()) {
                String sb1 = prefix +
                        boundary + lineEnd +
                        "Content-Disposition: form-data; name=\"file\"; filename=\"" + URLEncoder.encode(file.getKey(), StandardCharsets.UTF_8.name()) + "\"" + lineEnd +
                        "Content-Type: application/octet-stream; charset=" + charset + lineEnd +
                        lineEnd;
                outStream.write(sb1.getBytes());
                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }

                is.close();
                outStream.write(lineEnd.getBytes());
            }
        }

        // 请求结束标志
        byte[] endData = (prefix + boundary + prefix + lineEnd).getBytes();
        outStream.write(endData);
        outStream.flush();

        // 得到响应码
        int res = conn.getResponseCode();
        int successCode = 200;
        if (res == successCode) {
            InputStream in = conn.getInputStream();
            InputStreamReader isReader = new InputStreamReader(in);
            BufferedReader bufReader = new BufferedReader(isReader);
            String line;

            StringBuilder data = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                data.append(line);
            }
            outStream.close();
            conn.disconnect();
            return data.toString();
        }
        outStream.close();
        conn.disconnect();
        return null;
    }


    public static void sendGzipData(String url, String requestMethod, String gzipData) {
        try {
            long startTime = System.currentTimeMillis();
            String response = request(url, requestMethod, gzipData);
            long endTime = System.currentTimeMillis();
            LOGGER.info("上报数据，接口URL：{},上报数据大小：{}字节；消耗时长：{}ms; 返回: {}", url, gzipData.getBytes().length, endTime - startTime, response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void download(String url, String targetFilePath) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        if (new File(targetFilePath).exists()) {
            throw new FileAlreadyExistsException("文件已存在：" + targetFilePath);
        }
        URLConnection conn = getURLConnection(new URL(url), "GET", null);

        try (InputStream is = conn.getInputStream();
             FileOutputStream fos = new FileOutputStream(targetFilePath)) {

            byte[] bytes = new byte[1024 * 1024];
            int length;
            while ((length = is.read(bytes)) != -1) {
                fos.write(bytes, 0, length);
            }
            fos.flush();
        } catch (Exception e) {
            throw e;
        }

    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, KeyManagementException {
        String url = "https://img.linovelib.com/2/2679/126532/114298.jpg";
        String path = "/Users/yuanjin/Documents/tmp/001.jpg";
        download(url, path);
    }

}
