package org.osgl.oms.xio.undertow;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import org.osgl.exception.UnexpectedIOException;
import org.osgl.http.H;
import org.osgl.http.Http;
import org.osgl.oms.ResponseImplBase;
import org.osgl.oms.app.AppContext;
import org.osgl.util.E;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;

public class UndertowResponse extends ResponseImplBase<UndertowResponse> {
    @Override
    protected Class<UndertowResponse> _impl() {
        return UndertowResponse.class;
    }

    private HttpServerExchange hse;
    private Locale locale;
    private AppContext ctx;
    private volatile OutputStream os;
    private volatile Writer w;


    public UndertowResponse(HttpServerExchange exchange, AppContext ctx) {
        super(ctx);
        E.NPE(exchange);
        hse = exchange;
    }

    @Override
    public void addCookie(H.Cookie cookie) {
        hse.setResponseCookie(CookieConverter.osgl2undertow(cookie));
    }

    @Override
    public boolean containsHeader(String name) {
        return hse.getResponseHeaders().contains(name);
    }

    @Override
    public UndertowResponse characterEncoding(String encoding) {
        hse.getResponseHeaders().put(HttpString.tryFromString(H.Header.Names.ACCEPT_CHARSET), encoding);
        return this;
    }

    @Override
    public UndertowResponse contentLength(long len) {
        hse.setResponseContentLength(len);
        return this;
    }

    @Override
    protected OutputStream createOutputStream() {
        return hse.getOutputStream();
    }

    @Override
    protected void _setContentType(String type) {
        hse.getResponseHeaders().put(HttpString.tryFromString(H.Header.Names.CONTENT_TYPE), type);
    }

    @Override
    protected void _setLocale(Locale loc) {
        if (responseStarted()) {
            return;
        }
        locale = loc;
        hse.getResponseHeaders().put(Headers.CONTENT_LANGUAGE, loc.getLanguage() + "-" + loc.getCountry());
        if (null != charset && null == w) {
        }
    }

    @Override
    public Locale locale() {
        return locale;
    }

    @Override
    public void commit() {
        hse.endExchange();
    }

    @Override
    public UndertowResponse sendError(int sc, String msg) {
        return null;
    }

    @Override
    public UndertowResponse sendError(int sc) {
        return null;
    }

    @Override
    public UndertowResponse sendRedirect(String location) {
        return null;
    }

    @Override
    public UndertowResponse header(String name, String value) {
        return null;
    }

    @Override
    public UndertowResponse status(int sc) {
        return null;
    }

    @Override
    public UndertowResponse addHeader(String name, String value) {
        return null;
    }

    private boolean responseStarted() {
        return hse.isResponseStarted();
    }

}
