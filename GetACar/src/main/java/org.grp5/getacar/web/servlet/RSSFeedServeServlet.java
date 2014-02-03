package org.grp5.getacar.web.servlet;

import com.google.inject.Inject;
import org.grp5.getacar.web.guice.annotation.AbsoluteRSSFeedPath;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static com.google.common.io.Files.newInputStreamSupplier;

/**
 *
 */
public class RSSFeedServeServlet extends HttpServlet {

    private final String absoluteRSSFeedPath;

    @Inject
    public RSSFeedServeServlet(@AbsoluteRSSFeedPath String absoluteRSSFeedPath) {
        this.absoluteRSSFeedPath = absoluteRSSFeedPath;
    }

    private static final String CONTENT_TYPE = "application/rss+xml";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.processRequest(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.processRequest(request, response);
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        File file = new File(absoluteRSSFeedPath);
        if (!file.exists()) {
            throw new IOException("RSS Feed XML file can not be found");
        }
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType(CONTENT_TYPE);
        response.setContentLength((int) file.length());

        byte[] buffer = new byte[8 * 1024]; // 8k buffer
        FileInputStream inputStream = newInputStreamSupplier(file).getInput();
        new DataInputStream(new FileInputStream(file));
        int length = 0;
        try {
            while ((inputStream != null) && ((length = inputStream.read(buffer)) != -1)) {
                outputStream.write(buffer, 0, length);
            }
        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            response.setContentType("text/plain");
            response.getOutputStream().print(stringWriter.toString());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            outputStream.flush();
            outputStream.close();
        }
    }
}