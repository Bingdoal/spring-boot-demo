package springboot.demo.middleware.filter;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.stream.Collectors;

public class XSSWrapper extends HttpServletRequestWrapper {
    private byte[] rawData;
    private final HttpServletRequest request;
    private final ResettableServletInputStream servletStream;

    public XSSWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
        this.servletStream = new ResettableServletInputStream();
    }

    public String getBody() throws IOException {
        return getReader().lines().collect(Collectors.joining());
    }

    @Override
    public Cookie[] getCookies() {
        Cookie[] cookies = super.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            cookie.setValue(stripXSS(cookie.getValue()));
        }
        return cookies;
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = stripXSS(values[i]);
        }
        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        return stripXSS(value);
    }

    public static String stripXSS(String value) {
        if (value == null
                || StringUtils.isEmpty(value)
                || StringUtils.isBlank(value)) {
            return value;
        }
        return Jsoup.clean(value, Safelist.relaxed());
    }

    public void resetInputStream(byte[] data) {
        servletStream.stream = new ByteArrayInputStream(data);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (rawData == null) {
            rawData = this.request.getInputStream().readAllBytes();
            servletStream.stream = new ByteArrayInputStream(rawData);
        }
        return servletStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (rawData == null) {
            rawData = this.request.getInputStream().readAllBytes();
            servletStream.stream = new ByteArrayInputStream(rawData);
        }
        String encoding = getCharacterEncoding();
        if (encoding != null) {
            return new BufferedReader(new InputStreamReader(servletStream, encoding));
        } else {
            return new BufferedReader(new InputStreamReader(servletStream));
        }
    }

    private class ResettableServletInputStream extends ServletInputStream {
        private InputStream stream;

        @Override
        public int read() throws IOException {
            return stream.read();
        }

        @SneakyThrows
        @Override
        public boolean isFinished() {
            return stream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }
    }
}